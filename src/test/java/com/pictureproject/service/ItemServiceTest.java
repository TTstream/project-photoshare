package com.pictureproject.service;

import com.pictureproject.dto.ItemFormDto;
import com.pictureproject.entity.Item;
import com.pictureproject.entity.ItemImg;
import com.pictureproject.repository.ItemImgRepository;
import com.pictureproject.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception{

        List<MultipartFile> multipartFileList=new ArrayList<>();

        for(int i=0;i<5;i++){
            String path="C:/shop/item/";
            String imageName="image"+i+".jpg";
            MockMultipartFile multipartFile=new MockMultipartFile(path,imageName,"image/jpg",new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "test",roles = "USER")
    void saveItemTest() throws Exception{
        ItemFormDto itemFormDto=new ItemFormDto();
        itemFormDto.setTitle("테스트타이틀");
        itemFormDto.setItemDetail("테스트 상세 설명 입니다.");

        List<MultipartFile> multipartFileList=createMultipartFiles();
        Long itemId= itemService.saveItem(itemFormDto,multipartFileList);

        List<ItemImg> itemImgList=itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        Item item=itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getTitle(),item.getTitle());
        assertEquals(itemFormDto.getItemDetail(),item.getItemDetail());
        assertEquals(multipartFileList.get(0).getOriginalFilename(),itemImgList.get(0).getOriImgName());
    }

}