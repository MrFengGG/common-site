package com.feng.home.common;

import com.feng.home.common.actiator.GeneralHealthIndicator;
import com.feng.home.common.response.ExceptionAdvice;
import com.feng.home.common.response.ResponseAdvice;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(HealthIndicator.class)
public class CommonWebStarterAutoConfiguration {
    @Resource
    private DataSource dataSource;


    @Bean(name = "dbHealthIndicator")
    HealthIndicator dbHealthIndicator() {
        return new GeneralHealthIndicator(dataSource);
    }

    @Bean
    ExceptionAdvice exceptionAdvice(){
        return new ExceptionAdvice();
    }

    @Bean
    ResponseAdvice responseAdvice(){
        return new ResponseAdvice();
    }
}
