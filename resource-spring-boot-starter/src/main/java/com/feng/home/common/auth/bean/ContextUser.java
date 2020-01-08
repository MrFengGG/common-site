package com.feng.home.common.auth.bean;

import com.feng.home.common.collection.Dict;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContextUser {
    private Collection<String> roleList;

    private String username;

    private String loginIp;

    private Dict extend;
}
