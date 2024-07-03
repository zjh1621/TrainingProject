package com.example.moon.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO {
    private List<QuestionDTO> questionDTOList;//文章列表
    private boolean showPrevious;//是否显示 上一页 按钮
    private boolean showNext;//是否显示 下一页 按钮
    private boolean showFirstPage;//是否显示 第一页 按钮
    private boolean showEndPage;//是否显示 最后一页 按钮

    private Integer totalPage;//当前所处页码
    private Integer page;//当前所处页码
    private List<Integer> pages = new ArrayList<>();//当前页码列表中显示的页码

    public void setPagination(Integer totalCount, Integer page, Integer size) {

        Integer totalPage = 0;
        //计算总页码
        if(totalCount % size ==0){
            totalPage = totalCount / size;
        }
        else{
            totalPage = totalCount / size + 1;
        }
        this.page=page;
        this.totalPage=totalPage;
        pages.add(page);
        for(int i=1;i<=2;i++){
            if(page-i>0){
                pages.add(0,page-i);
            }
            if(page+i<=totalPage){
                pages.add(page+i);
            }
        }

        //根据当前页码，决定是否显示 上一页 按钮
        if(page==1){
            showPrevious=false;
        }
        else {
            showPrevious=true;
        }
        //根据当前页码，决定是否显示 下一页 按钮
        if(page==totalPage){
            showNext=false;
        }
        else{
            showNext=true;
        }
        //根据当前页码，决定是否显示 第一页 按钮
        if(pages.contains(1)){
            showFirstPage=false;
        }
        else{
            showFirstPage=true;
        }
        //根据当前页码，决定是否显示 最后一页 按钮
        if(pages.contains(totalPage)){
            showEndPage=false;
        }
        else{
            showEndPage=true;
        }

    }
}
