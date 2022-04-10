package com.example.urlprocessing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "controller")
public class ControllerConfiguration {

    private String requestMapping;

    private String processPath;

    private String resultPath;
}
