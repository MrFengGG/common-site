package com.feng.home.common;

import com.feng.home.common.resource.context.ResourceContext;
import com.feng.home.common.resource.service.RemoteResourcePusher;
import com.feng.home.common.resource.service.ResourceProcessor;
import com.feng.home.common.resource.aspect.ResourceAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ResourceConfiguration.class)
public class ResourceAutoConfiguration {

    @ConditionalOnMissingBean(value = ResourceProcessor.class)
    @Bean
    public ResourceProcessor resourceProcessor(){
        return new RemoteResourcePusher();
    }

    @Bean
    public ResourceAspect resourceAspect(){
        return new ResourceAspect();
    }

    @Bean
    public ResourceContext resourceContext(){
        return new ResourceContext();
    }
}
