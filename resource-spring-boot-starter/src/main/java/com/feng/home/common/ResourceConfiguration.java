package com.feng.home.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties("resource")
public class ResourceConfiguration {
    //平台用户名
    private String plateClientUser;
    //平台密码
    private String plateClientSecret;
    //资源推送路径
    private String resourcePushUrl;
    //校验token路径
    private String checkTokenUrl;
    //登录路径
    private String loginUrl;
    //鉴权模式local:本地,remote:远程
    private String authModel;
    //白名单角色
    private Set<String> whiteListRole;
}
