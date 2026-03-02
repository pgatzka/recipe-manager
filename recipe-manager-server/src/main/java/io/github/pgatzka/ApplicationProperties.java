package io.github.pgatzka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "recipe-manager")
public class ApplicationProperties {

    private JWT jwt;

    @Getter
    @Setter
    public static class JWT {

        private long expiration;

        private String issuer;

        private String secret;

    }


}
