package com.feng.home.common.auth.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.feng.home.common.ResourceConfiguration;
import com.feng.home.common.auth.bean.ContextUser;
import com.feng.home.common.auth.service.AccessUserService;
import com.feng.home.common.collection.Dict;
import com.feng.home.common.common.StringUtil;
import com.feng.home.common.http.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpTokenAccessUserService implements AccessUserService {
    @Autowired
    private ResourceConfiguration resourceConfiguration;

    @Override
    public Optional<ContextUser> accessUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        ContextUser contextUser = null;
        if(StringUtil.isEmpty(token)){
            return Optional.ofNullable(contextUser);
        }
        return accessUser(token);
    }

    private Optional<ContextUser> accessUser(String token){
        ContextUser contextUser = null;
        try {
            JSONObject jsonObject = HttpUtil.get(resourceConfiguration.getCheckTokenUrl(), new Dict().set("token", token),new Dict());
            if(jsonObject.getInteger("code") > 0){
                JSONObject userData = jsonObject.getJSONObject("data");
                if(userData != null) {
                    contextUser.setRoleList(userData.getJSONArray("roles").stream().map(String::valueOf).collect(Collectors.toList()));
                    contextUser.setUsername(userData.getString("username"));
                    contextUser.setExtend(new Dict(userData.getInnerMap()));
                }
            }
        } catch (IOException e) {
            throw new UnsupportedOperationException("token校验失败", e);
        }
        return Optional.ofNullable(contextUser);
    }
}
