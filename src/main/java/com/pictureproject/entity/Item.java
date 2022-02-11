package com.pictureproject.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Getter @Setter
@ToString
public class Item extends BaseEntity{

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title; // 제목

    private String itemDetail; //상품 상세 설명
    
}
