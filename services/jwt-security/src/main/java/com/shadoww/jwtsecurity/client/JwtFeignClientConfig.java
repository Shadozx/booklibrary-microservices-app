package com.shadoww.jwtsecurity.client;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class JwtFeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // Retrieve the JWT token from the SecurityContextHolder
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            System.out.println("jwt feign client config work!");
            if (authentication != null && authentication.getCredentials() instanceof String jwtToken) {

                System.out.println(jwtToken);
                template.header("Authorization", "Bearer " + jwtToken);
            }
            System.out.println("Request template in jwt feign client config file:" + template);
        };
    }

//    @Bean
//    public ErrorDecoder errorDecoder() {
//        return new FeignErrorDecoder();
//    }
}