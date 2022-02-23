package com.pictureproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "mypage_item")
public class MyPageItem extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mypage_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mypage_id")
    private MyPage myPage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public static MyPageItem createMyPageItem(MyPage myPage,Item item){
        MyPageItem myPageItem=new MyPageItem();
        myPageItem.setMyPage(myPage);
        myPageItem.setItem(item);
        return myPageItem;
    }

}
