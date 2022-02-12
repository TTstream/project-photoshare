package com.pictureproject.service;

import com.pictureproject.dto.ItemFormDto;
import com.pictureproject.entity.Item;
import com.pictureproject.entity.ItemImg;
import com.pictureproject.repository.ItemImgRepository;
import com.pictureproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        
        //상품 등록
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

}
