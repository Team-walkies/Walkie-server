package site.walkies.walkie.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.walkies.walkie.domain.member.service.MemberLoginService;
import site.walkies.walkie.global.auth.utils.JWTProvider;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final JWTProvider jwtProvider;
    private final MemberLoginService memberLoginService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

        // OPTIONS 요청은 그냥 필터 통과 (개발용)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // JWT가 필요 없는 API 리스트 (로그인, 회원가입 등)
        List<String> excludedUrls = List.of(
                "/api/v1/auth/login",
                "/api/v1/auth/signup",
                "/api/v1/auth/refresh",
                "/api/v1/swagger-ui.html",
                "/api/v1/v3/api-docs",
                "/api/v1/v3/api-docs/**",
                "/swagger-ui/**",
                "/api/v1/files/**",
                "/api/v1/file/**"
        );

        // JWT 없이 접근 가능한 API는 필터를 건너뜀
        if (excludedUrls.contains(requestURI)) {
            log.info("[JwtFilter] '{}' 요청이므로 JWT 검증을 건너뜁니다.", requestURI);
            filterChain.doFilter(request, servletResponse);
            log.info("[JwtFilter] 필터를 통과한 후 요청이 정상적으로 전달되었습니다.");
            return;
        }

        String jwt = resolveToken(request);

        // ✅ JWT가 없으면 인증하지 않고 그냥 넘어가도록 변경
        if (!StringUtils.hasText(jwt)) {
            log.info("[JwtFilter] JWT 없음. 인증하지 않고 요청 진행.");
            filterChain.doFilter(request, servletResponse);
            return;
        }

        try{
            if (jwtProvider.validateToken(jwt)) {
                Long memberId = jwtProvider.getMemberId(jwt);
                String providerId = jwtProvider.getProviderId(jwt);

                Member member = memberLoginService.findMemberById(memberId);

                if (Boolean.TRUE.equals(member.getDeleteCd())) {
                    log.warn("[JwtFilter] 탈퇴 유저의 접근 차단: memberId={}, uri={}", member.getId(), requestURI);
                    throw new CustomException(ErrorCode.DELETED_USER_CANNOT_ACCESS);
                }

                UserDetails userDetails = MemberPrincipal.createMemberAuthority(member);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                sendUnauthorized(response, "만료된 AccessToken 입니다. 새 토큰을 발급받으세요.");
                return;
            }
        } catch (Exception e) {
            sendUnauthorized(response, "유효하지 않은 AccessToken 입니다.");
            return;
        }

        filterChain.doFilter(request, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("""
        {
          "status": 401,
          "message": "%s"
        }
        """.formatted(message));
    }

}
