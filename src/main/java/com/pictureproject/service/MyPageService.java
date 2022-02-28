package com.pictureproject.service;

import com.pictureproject.dto.*;
import com.pictureproject.entity.Item;
import com.pictureproject.entity.MyPage;
import com.pictureproject.entity.MyPageItem;
import com.pictureproject.entity.User;
import com.pictureproject.paging.Criteria;
import com.pictureproject.repository.ItemRepository;
import com.pictureproject.repository.MyPageItemRepository;
import com.pictureproject.repository.MyPageRepository;
import com.pictureproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final MyPageRepository myPageRepository;
    private final MyPageItemRepository myPageItemRepository;
    private final HttpSession httpSession;

    //마이페이지 엔티티,마이페이지아이템 엔티티 생성 및 아이템 추가
    public Long addMypage(Long itemId, String email){
        
        //마이페이지에 넣을 Item 엔티티 조회
        Item item=itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        //현재 로그인한 회원 엔티티 조회
        User user=userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        //현재 로그인한 회원의 마이페이지 엔티티 조회
        MyPage myPage=myPageRepository.findByUserId(user.getId());
        if(myPage==null){ //물품을 처음으로 마이페이지에 담을 경우 마이페이지 생성
            myPage=MyPage.createMypage(user);
            myPageRepository.save(myPage);
        }

        //마이페이지 엔티티, 물품 엔티티, 마이페이지아이템 엔티티 생성
        MyPageItem myPageItem=MyPageItem.createMyPageItem(myPage,item);

        //마이페이지아이템 엔티티에 들어갈 물품 저장
        myPageItemRepository.save(myPageItem);
        return myPageItem.getId();

    }

    @Transactional(readOnly = true)
    public List<MyPageItemDto> getItemMngListPage(String email) {
        
        User user=userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        //현재 로그인한 회원의 마이페이지 엔티티 조회
        MyPage myPage=myPageRepository.findByUserId(user.getId());
        if(myPage==null){
            throw new IllegalStateException("업로드한 사진이 없습니다.");
        }

        //장바구니에 담겨있는 물품 정보 조회
        return itemRepository.getItemMngListPage(myPage.getId());
    }

    @Transactional(readOnly = true)
    public List<MyPageItemDto> getItemMngListShowPage(Criteria criteria,String email) {

        User user=userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        //현재 로그인한 회원의 마이페이지 엔티티 조회
        MyPage myPage=myPageRepository.findByUserId(user.getId());

        //장바구니에 담겨있는 물품 정보 조회
        return itemRepository.getItemMngListShowPage(criteria,myPage.getId());
    }
    
    //마이페이지 사진 자세히보기
    @Transactional(readOnly = true)
    public MainItemDetailDto getMyPageItemInfo(Long itemId,MainItemDetailDto mainItemDetailDto){

        Item item=itemRepository.findById(itemId).get();
        SessionUser user=(SessionUser) httpSession.getAttribute("user");

        if(item.getRegister().equals(user.getEmail())){
            mainItemDetailDto.setRegister(true);
        }else{
            mainItemDetailDto.setRegister(false);
        }
        return mainItemDetailDto;
    }

    //MyPageItem 삭제
    public void deleteMyPageItem(Long itemId) {
        MyPageItem myPageItem=myPageItemRepository.findByItemId(itemId);
        myPageItemRepository.delete(myPageItem);
    }

}
