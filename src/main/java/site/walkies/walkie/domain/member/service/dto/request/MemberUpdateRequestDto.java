package site.walkies.walkie.domain.member.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequestDto {
    private String memberNickname;

    @Builder
    public MemberUpdateRequestDto(String memberNickname) {
        this.memberNickname = memberNickname;
    }
}
