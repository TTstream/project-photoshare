package com.pictureproject.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDetailDto {

    private Long id;
    private String title;
    private String itemDetail;
    private String imgUrl;
    //private boolean register;
}
