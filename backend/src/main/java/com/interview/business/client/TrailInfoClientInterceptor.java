package com.interview.business.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

import static org.springframework.http.HttpHeaders.USER_AGENT;

public class TrailInfoClientInterceptor {

    @Bean
    public RequestInterceptor requestInterceptor()
    {
        return template -> template.header(USER_AGENT, "HikingApp");
    }
}
