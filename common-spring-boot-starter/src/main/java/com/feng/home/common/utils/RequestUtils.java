package com.feng.home.common.utils;

import com.feng.home.common.common.StringUtil;
import com.feng.home.common.jdbc.pagination.Page;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
    public static <T> Page<T> getPage(HttpServletRequest request){
        Page<T> page = new Page<>();
        String pageNoString = request.getParameter("pageNo");
        int pageNo = StringUtil.isEmpty(pageNoString) ? 1 : Integer.parseInt(pageNoString);
        page.setPageNo(pageNo);

        String pageSizeString = request.getParameter("pageSize");
        int pageSize = StringUtil.isEmpty(pageSizeString) ? 10 : Integer.parseInt(pageSizeString);
        page.setPageSize(pageSize);

        return page;
    }
}
