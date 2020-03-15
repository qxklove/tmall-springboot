package com.qxk.mall.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.NoArgsConstructor;

/**
 * @author: laijianzhen
 * @description:
 * @create: 2019/07/25
 */

@NoArgsConstructor
public class PageUtil<T> extends Page<T> {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final int DEFAULT_MAX_PAGE_SIZE = 100;

    public PageUtil(int page, int pageSize) {
        if (page <= 0) {
            page = DEFAULT_PAGE;
        }
        pageSize = (pageSize > 0 && pageSize <= DEFAULT_MAX_PAGE_SIZE) ? pageSize
                : DEFAULT_PAGE_SIZE;
        this.setCurrent(page);
        this.setSize(pageSize);
    }
}
