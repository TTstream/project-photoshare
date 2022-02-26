package com.pictureproject.dto;

import com.pictureproject.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title; //제목

    @NotBlank(message = "상세 설명은 필수 입력 값입니다.")
    private String itemDetail; //물품 상세 설명
    
    private String register; //등록자

    //물품 수정할 때 물품 이미지 정보를 저장하는 리스트
    private List<ItemImgDto> itemImgDtoList=new ArrayList<>();

    //물품 수정할 때 물품의 이미지 아이디를 저장하는 리스트
    private List<Long> itemImgIds=new ArrayList<>();

    private static ModelMapper modelMapper=new ModelMapper();

    //물품 생성시 ItemFormDto를 Item으로 변환후 값 저장
    public Item createItem(){
        return modelMapper.map(this,Item.class);
    }

    //물품 조회시 Item을 ItemFormDto로 변환 후 값 저장
    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }

}
