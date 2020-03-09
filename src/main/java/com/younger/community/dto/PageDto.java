package com.younger.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
辅助实现展示分页功能
将原来的questionlsit封装到此类，再添加展示页面的属性
 */

@Data
public class PageDto {
    private List<QuestionDto> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setpage(Integer totalCount, Integer page, Integer size) {

        if(totalCount % size == 0) {
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        this.page = page;
        pages.add(page);
        for(int i = 1; i <= 3; i++) {
            if(page-i>0) {
                pages.add(0,page-i);
            }
            if(page+i <= totalPage) {
                pages.add(page+i);
            }
        }

        //当处于第一页时，不展示前一页
        if(page == 1) {
            showPrevious = false;
        }else {
            showPrevious = true;
        }

        //当位于最后一页时，不展示后一页
        if(page == totalPage) {
            showNext = false;
        }else {
            showNext = true;
        }

        //当包含第一页时，不展示首页
        if(pages.contains(1)) {
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }

        //当包含最后一页时，不展示尾页
        if(pages.contains(totalPage)) {
            showEndPage = false;
        }else {
            showEndPage = true;
        }
    }
}
