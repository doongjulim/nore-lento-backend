package io.github.dongjulim.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String uploadDir;

    public WebConfig(@Value("${upload.dir}") String uploadDir){
        this.uploadDir = uploadDir;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String resourceLocation = String.format("file://%s/", uploadDir);
        registry.addResourceHandler("/images/**")
                .addResourceLocations(resourceLocation)
                .setCachePeriod(3600)
                ;
    }
}
