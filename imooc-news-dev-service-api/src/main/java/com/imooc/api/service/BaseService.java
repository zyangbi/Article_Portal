package com.imooc.api.service;

import com.github.pagehelper.PageInfo;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public class BaseService {

    public PagedGridResult setPagedGridResult(List<?> list) {
        PageInfo<?> pageList = new PageInfo<>(list);

        PagedGridResult result = new PagedGridResult();
        result.setPage(pageList.getPageNum());
        result.setRows(pageList.getList());
        result.setRecords(pageList.getTotal());
        result.setTotal(pageList.getPages());
        return result;
    }

    public static final String REDIS_FANS_COUNT = "count:fans:";
    public static final String REDIS_FOLLOW_COUNT = "count:follow:";

}
