package com.pictureproject.controller;

import com.pictureproject.dto.ItemFormDto;
import com.pictureproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/user/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/user/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult
    , Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty()&&itemFormDto.getId()==null){
            model.addAttribute("errorMessage","첫번쨰 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto,itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    //물품 수정시 물품 조회
    @GetMapping(value = "/user/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId,Model model){

        try{
            ItemFormDto itemFormDto=itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto",itemFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 물품 입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";

    }

    //물품 수정 저장
    @PostMapping(value = "/user/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto,BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,Model model){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty()&&itemFormDto.getId()==null){
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto,itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","물품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";

    }

}
