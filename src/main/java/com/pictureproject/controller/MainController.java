package com.pictureproject.controller;

import com.pictureproject.dto.ItemSearchDto;
import com.pictureproject.dto.MainItemDetailDto;
import com.pictureproject.dto.MainItemDto;
import com.pictureproject.dto.SessionUser;
import com.pictureproject.paging.Criteria;
import com.pictureproject.paging.Paging;
import com.pictureproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        //메인페이지 총 게시물개수
        List<MainItemDto> itemtotal=itemService.getMainItemListPage(itemSearchDto);

        //메인페이지 데이터를 가지고 올 시작 인덱스 및 한 번에 가지고 올 최대 개수
        List<MainItemDto> itemsShow=itemService.getMainItemListShowPage(itemSearchDto,cri);

        Paging paging=new Paging();
        paging.setCri(cri);
        paging.setTotalCount(itemtotal.size());

        System.out.println(paging.toString());

        model.addAttribute("paging", paging);
        model.addAttribute("itemtotal",itemtotal.size());
        model.addAttribute("items",itemsShow);

        double lastPage=Math.ceil((double) itemtotal.size()/cri.getPerPageNum()); //; 전체 페이지의 마지막 페이지
        model.addAttribute("lastPage",lastPage);

        return "main";
    }

    //메인화면 사진 클릭시 처리될 api
    @GetMapping("/api/item")
    public ResponseEntity<?> MainItemInfo(MainItemDetailDto mainItemDetailDto){
        return new ResponseEntity<>(mainItemDetailDto, HttpStatus.OK);
    }

    /*
    마이페이지에서 할 부분
    @GetMapping("/api/item/{itemId}")
    public ResponseEntity<?> ItemInfo(@PathVariable Long itemId, MainItemDetailDto mainItemDetailDto){
        return new ResponseEntity<>(mainService.getMainItemDetailDto(itemId,mainItemDetailDto), HttpStatus.OK);
    }*/
}
