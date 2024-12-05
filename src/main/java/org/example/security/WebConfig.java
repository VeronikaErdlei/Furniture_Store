package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

@Configuration
public class WebConfig {
    @Bean(name = "customMvcConversionService")
    public ConversionService mvcConversionService() {
        return new DefaultFormattingConversionService();
    }
}
