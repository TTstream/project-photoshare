package com.pictureproject.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageItemDto {

    private Long mypageitemid;

    private String title;

    private String itemDetail;

    private String imgUrl;

    @QueryProjection
    public MyPageItemDto(Long mypageitemid, String title,String itemDetail,String imgUrl){
        this.mypageitemid=mypageitemid;
        this.title=title;
        this.itemDetail=itemDetail;
        this.imgUrl=imgUrl;
    }

}
