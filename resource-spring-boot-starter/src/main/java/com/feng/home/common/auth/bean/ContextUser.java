package com.feng.home.common.auth.bean;

import com.feng.home.common.collection.Dict;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Builder
@Data
public class ContextUser {
    private Collection<String> roleList;

    private String username;

    private Dict extend;
}
