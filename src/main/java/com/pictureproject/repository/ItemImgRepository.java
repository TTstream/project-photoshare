package com.pictureproject.repository;

import com.pictureproject.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg,Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemid);

    List<ItemImg> findByItemId(Long itemId);

    @Transactional
    @Modifying
    @Query("delete from ItemImg im where im.id in :ItemImgIds")
    void deleteAllByIdQuery(@Param("ItemImgIds") List<Long> ItemImgIds);
}
