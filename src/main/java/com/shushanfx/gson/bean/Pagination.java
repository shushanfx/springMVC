package com.shushanfx.gson.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by dengjianxin on 2015/11/25.
 */
public class Pagination<E> implements Serializable{
    private int pageIndex = 1;
    private int pageSize = 10;
    private int total = 0;
    private List<? extends E> list = null;

    public Pagination(Integer pageIndex, Integer pageSize, List<? extends E> list){
        setPageIndex(pageIndex);
        setPageSize(pageSize);
        this.list = list;
        if(list!=null){
            this.total = list.size();
        }
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        if(Objects.isNull(pageIndex) || pageIndex <= 0){
            pageIndex = 1;
        }
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if(Objects.isNull(pageSize) || pageSize <=0 ){
            pageSize = 10;
        }
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageTotal(){
        if(total > 0){
            return total % pageSize > 0 ? (total / pageSize + 1) : total / pageSize;
        }
        return 0;
    }

    public List<? extends E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public <E> List<E> realList(){
        int temp = 0;
        if(Objects.isNull(list) || list.size() == 0){
            return Collections.emptyList();
        }
        int temp2 = list.size();
        int start = Math.min(temp2, (pageIndex - 1) * pageSize);
        int end = Math.min(temp2, (pageSize * pageIndex));
        if(start >= end){
            return Collections.emptyList();
        }
        return (List<E>) list.subList(start, end);
    }
}
