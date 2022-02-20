package com.pictureproject.controller;

import com.pictureproject.dto.ItemSearchDto;
import com.pictureproject.dto.MainItemDto;
import com.pictureproject.dto.SessionUser;
import com.pictureproject.paging.Criteria;
import com.pictureproject.paging.Paging;
import com.pictureproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    private final HttpSession httpSession;

    @GetMapping(value = "/")
    public String main(Criteria cri, ItemSearchDto itemSearchDto, Model model){

        //로그인 시 유저 이름을 보여주는 코드
        SessionUser user=(SessionUser) httpSession.getAttribute("user");
        if(user!=null){
            model.addAttribute("userName",user.getName());
        }else{
            model.addAttribute("userName","로그인");
        }

        /*Pageable pageable= PageRequest.of(page.isPresent() ? page.get() : 0,1);
        Page<MainItemDto> items=itemService.getMainItemPage(itemSearchDto,pageable);*/

        List<MainItemDto> itemtotal=itemService.getMainItemListPage(itemSearchDto); //총 게시물개수

        List<MainItemDto> itemsShow=itemService.getMainItemListShowPage(itemSearchDto,cri); //

        Paging paging=new Paging();
        paging.setCri(cri);
        paging.setTotalCount(itemtotal.size());

        System.out.println(paging.toString());

        model.addAttribute("paging", paging);
        model.addAttribute("itemtotal",itemtotal.size());
        model.addAttribute("items",itemsShow);

        /*Pageable pageable= PageRequest.of(page.isPresent() ? page.get() : 0,1);
        Page<MainItemDto> items=itemService.getMainItemPage(itemSearchDto,pageable);
        model.addAttribute("items",items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("totalPage",items.getTotalPages());*/
        return "main";
    }
}
