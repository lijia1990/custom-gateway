package com.custom.gateway.model.core;

import com.github.pagehelper.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 返回包装类
 *
 * @author LiJia
 */
@Data
public class PageBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private long total; //总记录数
    private List<T> records; //结果集
    private int page; //第几页
    private int size; //每页记录数
    private int pages; // 总页数

    public PageBean(List<T> list) {
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.page = page.getPageNum();
            this.size = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.records = page;
        }
    }

}

