package com.pictureproject.service;

import com.pictureproject.entity.ItemImg;
import com.pictureproject.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}") // itemImgLocation=C:/shop/item
    private String itemImgLocaiton;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName=itemImgFile.getOriginalFilename();
        String imgName="";
        String imgUrl="";

        //파일업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName= fileService.uploadFile(itemImgLocaiton,oriImgName,itemImgFile.getBytes());
            imgUrl="/images/item/"+imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName,imgName,imgUrl);
        itemImgRepository.save(itemImg);

    }

    //물품 이미지 수정
    public void updateItemImg(Long itemImgId,MultipartFile itemImgFile) throws Exception{

        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg=itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(savedItemImg.getImgName())){
                fileService.deleteFile(itemImgLocaiton+"/"+savedItemImg.getImgName());;
            }

            String oriImgName=itemImgFile.getOriginalFilename();
            String imgName= fileService.uploadFile(itemImgLocaiton,oriImgName,itemImgFile.getBytes());;
            String imgUrl="/images/item/"+imgName;
            savedItemImg.updateItemImg(oriImgName,imgName,imgUrl); //수정된 물품 이미지 업데이트

        }

    }

    //사진 삭제 및 ItemImg 데이터 삭제
    public void deleteItemImg(Long itemId) throws Exception {
        List<ItemImg> itemImgs=itemImgRepository.findByItemId(itemId);

        List<Long> ItemImgIds=new ArrayList<>();

        //사진 삭제
        for(ItemImg itemImg:itemImgs){
            if(itemImg.getImgUrl()!=null){
                fileService.deleteFile(itemImgLocaiton+"/"+itemImg.getImgName());
            }
        }

        //ItemImg 데이터 삭제
        for(ItemImg itemImg:itemImgs){
            ItemImgIds.add(itemImg.getId());
        }

        itemImgRepository.deleteAllByIdQuery(ItemImgIds);

    }

}
