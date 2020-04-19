package com.qxk.mall.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author laijianzhen
 */
@Getter
@Setter
@NoArgsConstructor
public class Page4Navigator<T> {
    Page<T> page;
    Integer navigatePages;

    Integer totalPages;

    Integer number;

    Long totalElements;

    Integer size;

    Integer numberOfElements;

    List<T> content;

    boolean isHasContent;

    boolean first;

    boolean last;

    boolean isHasNext;

    boolean isHasPrevious;

    Integer[] navigatePageNums;

    public Page4Navigator(Page<T> page, int navigatePages) {
        this.page = page;
        this.navigatePages = navigatePages;

        totalPages = (int) page.getPages();

        number = (int) page.getCurrent();

        totalElements = page.getTotal();

        size = (int) page.getSize();

        numberOfElements = (int)(page.getTotal());

        isHasContent = page.getSize()>0;

        first = !page.hasPrevious();

        last = !page.hasNext();

        isHasNext = page.hasNext();

        isHasPrevious  = page.hasPrevious();
        calcNavigatePageNums();

    }

    private void calcNavigatePageNums() {
        Integer[] navigatePageNums;
        int totalPages = getTotalPages();
        int num = getNumber();
        //当总页数小于或等于导航页码数时
        if (totalPages <= navigatePages) {
            navigatePageNums = new Integer[totalPages];
            for (int i = 0; i < totalPages; i++) {
                navigatePageNums[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatePageNums = new Integer[navigatePages];
            int startNum = num - navigatePages / 2;
            int endNum = num + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNums[i] = startNum++;
                }
            } else if (endNum > totalPages) {
                endNum = totalPages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatePageNums[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNums[i] = startNum++;
                }
            }
        }
        this.navigatePageNums = navigatePageNums;
    }

}


