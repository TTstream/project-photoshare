package com.pictureproject.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "mypage")
@Getter @Setter
@ToString
public class MyPage {

    @Id
    @Column(name = "mypage_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    public static MyPage createMypage(User user){
        MyPage myPage=new MyPage();
        myPage.setUser(user);
        return myPage;
    }

}
