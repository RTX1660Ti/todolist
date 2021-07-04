package org.example.common;

import lombok.Data;


public class PageInfo {

    private static final int maxPageSize = 100;

    public Integer page;
    public Integer pageSize;

    public Integer getPage() {
        return  page == null ? 1 : page >= 1 ? page : 1;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize == null ? 10 : (pageSize > 0 ? (pageSize <= maxPageSize ? pageSize : maxPageSize) : 10);
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
