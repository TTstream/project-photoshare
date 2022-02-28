package com.pictureproject.controller;

import com.pictureproject.dto.MainItemDetailDto;
import com.pictureproject.dto.MyPageItemDto;
import com.pictureproject.dto.SessionUser;
import com.pictureproject.paging.Criteria;
import com.pictureproject.paging.Paging;
import com.pictureproject.service.ItemImgService;
import com.pictureproject.service.ItemService;
import com.pictureproject.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final ItemService itemService ;
    private final ItemImgService itemImgService;
    private final HttpSession httpSession;
    private final MyPageService myPageService;

    @GetMapping(value = "/user/items") //마이페이지
    public String itemMng(Criteria cri, Model model){

        SessionUser user=(SessionUser) httpSession.getAttribute("user");

        try {
            //마이페이지 총 게시물개수
            List<MyPageItemDto> itemtotal = myPageService.getItemMngListPage(user.getEmail());
            //사진관리페이지에 데이터를 가지고 올 시작 인데스 및 한 번에 가지고 올 최대 개수
            List<MyPageItemDto> itemsShow = myPageService.getItemMngListShowPage(cri,user.getEmail());

            Paging paging=new Paging();
            paging.setCri(cri);
            paging.setTotalCount(itemtotal.size());

            model.addAttribute("paging", paging); //페이징 정보
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

    //마이페이지에서 사진(게시물) 자세히보기
    @GetMapping(value = "/user/item/detail/{itemId}")
    public ResponseEntity<?> ItemInfo(@PathVariable Long itemId, MainItemDetailDto mainItemDetailDto){
        return new ResponseEntity<>(myPageService.getMyPageItemInfo(itemId,mainItemDetailDto), HttpStatus.OK);
    }

    //마이페이지 사진(게시물) 삭제
   @DeleteMapping(value = "/user/item/delete/{itemId}")
    public ResponseEntity<?> ItemDelete(@PathVariable Long itemId) throws Exception {

        //로컬에 저장되어있는 사진, ItemImg 엔티티의 값 삭제
       itemImgService.deleteItemImg(itemId);

       //MyPageItem 엔티티의 값 삭제
       myPageService.deleteMyPageItem(itemId);

       //Item 엔티티의 값 삭제
       itemService.deleteItem(itemId);

        return new ResponseEntity<>(itemId,HttpStatus.OK);
    }

}
