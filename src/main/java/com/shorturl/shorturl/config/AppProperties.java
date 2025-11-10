package com.shorturl.shorturl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private String redirectUrl;
    private int secondsOfTtl;
    private int redirectAmount;
}