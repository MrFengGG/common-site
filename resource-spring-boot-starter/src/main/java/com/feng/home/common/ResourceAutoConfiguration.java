package com.feng.home.common;

import com.feng.home.common.auth.AuthContext;
import com.feng.home.common.auth.ResourceAuthHandler;
import com.feng.home.common.auth.service.AccessUserService;
import com.feng.home.common.auth.service.AuthenticateService;
import com.feng.home.common.auth.service.impl.HttpAuthenticateService;
import com.feng.home.common.auth.service.impl.HttpTokenAccessUserService;
import com.feng.home.common.bean.BeanUtils;
import com.feng.home.common.resource.base.ResourceHandler;
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
    @ConditionalOnMissingBean
    public AccessUserService accessUserService(){
        return new HttpTokenAccessUserService();
    }

    @ConditionalOnMissingBean
    @Bean
    public AuthenticateService authenticateService(){
        return new HttpAuthenticateService();
    }

    @Bean
    public AuthContext authContext(){
        return new AuthContext();
    }

    @Bean("authHandler")
    public ResourceHandler authHandler(){
        return new ResourceAuthHandler();
    }

    @Bean
    public ResourceContext resourceContext(){
        return new ResourceContext();
    }
}
