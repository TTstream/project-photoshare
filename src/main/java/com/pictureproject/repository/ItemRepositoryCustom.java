package com.pictureproject.repository;

import com.pictureproject.dto.ItemSearchDto;
import com.pictureproject.dto.MainItemDto;
import com.pictureproject.paging.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    //메인페이지 총 개시물 수
    List<MainItemDto> getMainItemListPage(ItemSearchDto itemSearchDto);

    //메인페이지 데이터를 가지고 올 시작 인데스 및 한 번에 가지고 올 최대 개수
    List<MainItemDto> getMainItemListShowPage(ItemSearchDto itemSearchDto,Criteria criteria);
}
