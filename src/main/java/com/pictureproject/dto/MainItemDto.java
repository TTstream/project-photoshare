package com.pictureproject.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDto {

    private Long id;

    private String title;

    private String itemDetail;

    private String imgUrl;

    @QueryProjection
    public MainItemDto(Long id, String title,String itemDetail,String imgUrl){
        this.id=id;
        this.title=title;
        this.itemDetail=itemDetail;
        this.imgUrl=imgUrl;
    }
}
