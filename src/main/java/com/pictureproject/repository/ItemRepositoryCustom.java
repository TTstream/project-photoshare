package com.pictureproject.repository;

import com.pictureproject.dto.ItemSearchDto;
import com.pictureproject.dto.MainItemDto;
import com.pictureproject.dto.MyPageItemDto;
import com.pictureproject.paging.Criteria;
import java.util.List;

public interface ItemRepositoryCustom {

    //메인페이지 총 게시물 수
    List<MainItemDto> getMainItemListPage(ItemSearchDto itemSearchDto);

    //메인페이지에 데이터를 가지고 올 시작 인데스 및 한 번에 가지고 올 최대 개수
    List<MainItemDto> getMainItemListShowPage(ItemSearchDto itemSearchDto,Criteria criteria);

    //사진관리페이지 총 게시물 수
    List<MyPageItemDto> getItemMngListPage(Long mypageId);

    //사진관리페이지에 데이터를 가지고 올 시작 인데스 및 한 번에 가지고 올 최대 개수
    List<MyPageItemDto> getItemMngListShowPage(Criteria criteria,Long mypageId);

}
