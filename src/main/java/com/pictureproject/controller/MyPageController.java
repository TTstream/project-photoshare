package com.pictureproject.controller;

import com.pictureproject.dto.MyPageItemDto;
import com.pictureproject.dto.SessionUser;
import com.pictureproject.paging.Criteria;
import com.pictureproject.paging.Paging;
import com.pictureproject.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final HttpSession httpSession;
    private final MyPageService myPageService;

    @GetMapping(value = "user/items") //마이페이지
    public String itemMng(Criteria cri, Model model){

        SessionUser user=(SessionUser) httpSession.getAttribute("user");
        //System.out.println(user.getEmail());

        try {
            //마이페이지 총 게시물개수
            List<MyPageItemDto> itemtotal = myPageService.getItemMngListPage(user.getEmail());
            //사진관리페이지에 데이터를 가지고 올 시작 인데스 및 한 번에 가지고 올 최대 개수
            List<MyPageItemDto> itemsShow = myPageService.getItemMngListShowPage(cri,user.getEmail());

            Paging paging=new Paging();
            paging.setCri(cri);
            paging.setTotalCount(itemtotal.size());

            model.addAttribute("paging", paging);
            model.addAttribute("itemtotal",itemtotal.size()); //내가 업로드한 게시물 개수
            model.addAttribute("items",itemsShow);
            model.addAttribute("userName",user.getName()); //유저이름
            model.addAttribute("userEmail",user.getEmail()); //유저이메일
            model.addAttribute("userPicture",user.getPicture());//유저사진
            
            double lastPage=Math.ceil((double) itemtotal.size()/cri.getPerPageNum()); //; 전체 페이지의 마지막 페이지
            model.addAttribute("lastPage",lastPage);

        }catch (IllegalStateException e){
            model.addAttribute("userName",user.getName()); //유저이름
            model.addAttribute("userEmail",user.getEmail()); //유저이메일
            model.addAttribute("userPicture",user.getPicture());//유저사진
            model.addAttribute("itemtotal",0); //게시물 개수
            model.addAttribute("errorMessage",e.getMessage());
            return "item/itemMng";
        }

        return "item/itemMng";

    }

}
