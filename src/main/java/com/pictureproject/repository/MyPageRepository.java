package com.pictureproject.repository;

import com.pictureproject.entity.MyPage;
import org.springframework.data.jpa.repository.JpaRepository;

//현재 로그인한 회원의 MyPage 엔티티를 찾기 위해
public interface MyPageRepository extends JpaRepository<MyPage,Long> {
    MyPage findByUserId(Long userId);
}
