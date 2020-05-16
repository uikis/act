package com.example.act.permission.domain;

import java.util.List;

public class Page<T> {
    private List<T> object;
    private int pageNo;
    private int maxCount;
    private int maxpageNo;

    public List<T> getObject() {
        return object;
    }

    public void setObject(List<T> object) {
        this.object = object;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMaxpageNo() {
        return maxpageNo;
    }

    public void setMaxpageNo(int maxpageNo) {
        this.maxpageNo = maxpageNo;
    }
}
