package com.telnet.analyse.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import feign.RequestInterceptor;
import feign.RequestTemplate;
@Component
public class JwtRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() != null) {
            String jwtToken = (String) authentication.getCredentials();
            System.out.println("Adding JWT token to Feign request: " + jwtToken);
            template.header("Authorization", "Bearer " + jwtToken);
        } else {
            System.out.println("No JWT token found in SecurityContext");
        }
    }

}



