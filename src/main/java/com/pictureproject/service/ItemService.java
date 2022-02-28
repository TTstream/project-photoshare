package com.pictureproject.service;

import com.pictureproject.dto.*;
import com.pictureproject.entity.Item;
import com.pictureproject.entity.ItemImg;
import com.pictureproject.paging.Criteria;
import com.pictureproject.repository.ItemImgRepository;
import com.pictureproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    private final HttpSession httpSession;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        SessionUser user=(SessionUser) httpSession.getAttribute("user");

        //상품 등록
        itemFormDto.setRegister(user.getEmail());
        Item item= itemFormDto.createItem();
        itemRepository.save(item);
        
        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg=new ItemImg();
            itemImg.setItem(item);
            if(i==0) {
                itemImg.setRepImgYn("Y");
            } else{
                itemImg.setRepImgYn("N");
            }
            itemImgService.saveItemImg(itemImg,itemImgFileList.get(i));
        }

        return item.getId();
    }

    //물품을 불러오는 메소드
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList=itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList=new ArrayList<>();

        for(ItemImg itemImg:itemImgList){ //조회한 itemImg 엔티티를 ItemImgDto 객체로 만들어서 추가
            ItemImgDto itemImgDto=ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item=itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto=ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;

    }

    //물품 수정 메소드
    public Long updateItem(ItemFormDto itemFormDto,
                       List<MultipartFile> itemImgFileList) throws Exception{

        //물품 수정
        Item item=itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds=itemFormDto.getItemImgIds(); //상품 이미지 아이디 조회

        //이미지 수정
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),itemImgFileList.get(i));
        }

        return item.getId();
    }

    //물품 삭제
    public void deleteItem(Long itemId){
        Item item=itemRepository.findById(itemId).get();
        itemRepository.delete(item);
    }

    /* Test @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto,
                                             Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto,pageable);
    }*/

    //메인페이지 총 게시물 개수
    @Transactional(readOnly = true)
    public List<MainItemDto> getMainItemListPage(ItemSearchDto itemSearchDto){
        return itemRepository.getMainItemListPage(itemSearchDto);
    }

    //메인페이지 데이터를 가지고 올 시작 인데스 및 한 번에 가지고 올 최대 개수
    @Transactional(readOnly = true)
    public List<MainItemDto> getMainItemListShowPage(ItemSearchDto itemSearchDto,Criteria criteria){
        return itemRepository.getMainItemListShowPage(itemSearchDto,criteria);
    }
}
