package com.pictureproject.dto;

import com.pictureproject.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper=new ModelMapper();

    //상품 수정시 값을 조회하기 위해 ItemImg 엔티티를 ItemImgDto로 변환
    public static ItemImgDto of(ItemImg itemImg){
        return modelMapper.map(itemImg,ItemImgDto.class);
    }

}
