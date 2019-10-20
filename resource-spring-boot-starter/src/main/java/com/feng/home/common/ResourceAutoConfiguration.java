package com.feng.home.common;

import com.feng.home.common.auth.service.AccessUserService;
import com.feng.home.common.auth.service.AuthenticateService;
import com.feng.home.common.auth.service.impl.HttpAuthenticateService;
import com.feng.home.common.auth.service.impl.HttpTokenAccessUserService;
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
    public AccessUserService accessUserService(){
        return new HttpTokenAccessUserService();
    }

    @ConditionalOnMissingBean(value = AuthenticateService.class)
    @Bean
    public AuthenticateService authenticateService(){
        return new HttpAuthenticateService();
    }
}
