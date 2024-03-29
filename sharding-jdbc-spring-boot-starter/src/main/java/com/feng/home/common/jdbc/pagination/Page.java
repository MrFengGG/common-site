package com.feng.home.common.jdbc.pagination;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Page<T> {

    @NotNull
    @Min(1)
    private long pageNo = 1;

    @NotNull
    @Min(1)
    private long pageSize = 10;

    private long total;

    private String sortBy;

    private String rank = "DESC";

    private List<T> data;
}
