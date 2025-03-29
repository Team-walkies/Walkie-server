package site.walkies.walkie.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Base64;
import java.util.List;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.KeyFactory;

@Getter
public class ApplePublicKeyResponse {
    @JsonProperty("keys")
    private List<ApplePublicKey> keys;

    @Getter
    public static class ApplePublicKey {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;

        public RSAPublicKey toRSAPublicKey() {
            try {
                BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
                BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));
                RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return (RSAPublicKey) keyFactory.generatePublic(spec);
            } catch (Exception ex) {
                throw new RuntimeException("[ApplePublicKey] RSA 공개키 생성 실패", ex);
            }
        }
    }
}