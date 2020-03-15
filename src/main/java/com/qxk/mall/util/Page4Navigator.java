package com.qxk.mall.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Page4Navigator<T> {
    Page<T> pageFromJPA;
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

    Integer[] navigatepageNums;

    public Page4Navigator(Page<T> pageFromJPA, int navigatePages) {
        this.pageFromJPA = pageFromJPA;
        this.navigatePages = navigatePages;

        totalPages = (int) pageFromJPA.getPages();

        number = (int) pageFromJPA.getCurrent();

        totalElements = pageFromJPA.getTotal();

        size = (int) pageFromJPA.getSize();

        //numberOfElements = pageFromJPA.getNumberOfElements();

        content = pageFromJPA.getRecords();

        //isHasContent = pageFromJPA.hasContent();

        //first = pageFromJPA.isFirst();
        //
        //last = pageFromJPA.isLast();

        isHasNext = pageFromJPA.hasNext();

        isHasPrevious  = pageFromJPA.hasPrevious();
        calcNavigatepageNums();

    }

    private void calcNavigatepageNums() {
        Integer navigatepageNums[];
        int totalPages = getTotalPages();
        int num = getNumber();
        //当总页数小于或等于导航页码数时
        if (totalPages <= navigatePages) {
            navigatepageNums = new Integer[totalPages];
            for (int i = 0; i < totalPages; i++) {
                navigatepageNums[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatepageNums = new Integer[navigatePages];
            int startNum = num - navigatePages / 2;
            int endNum = num + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > totalPages) {
                endNum = totalPages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            }
        }
        this.navigatepageNums = navigatepageNums;
    }

}


