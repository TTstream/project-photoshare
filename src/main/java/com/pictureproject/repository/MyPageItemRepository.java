package com.pictureproject.repository;

import com.pictureproject.entity.MyPageItem;
import org.springframework.data.jpa.repository.JpaRepository;

//MyPageItem 엔티티에 들어갈 상품 저장
public interface MyPageItemRepository extends JpaRepository<MyPageItem,Long> {

}
