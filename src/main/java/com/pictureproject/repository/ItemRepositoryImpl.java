package com.pictureproject.repository;

import com.pictureproject.dto.*;
import com.pictureproject.dto.QMainItemDto;
import com.pictureproject.dto.QMyPageItemDto;
import com.pictureproject.entity.QItem;
import com.pictureproject.entity.QItemImg;
import com.pictureproject.entity.QMyPageItem;
import com.pictureproject.entity.QUser;
import com.pictureproject.paging.Criteria;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.thymeleaf.util.StringUtils;
import javax.persistence.EntityManager;
import java.util.List;

public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null: QItem.item.title.like("%"+searchQuery+"%");
    }

    @Override
    public List<MainItemDto> getMainItemListPage(ItemSearchDto itemSearchDto) {
        QItem item=QItem.item;
        QItemImg itemImg=QItemImg.itemImg;

                return queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.title,
                                item.itemDetail,
                                itemImg.imgUrl)
                )
                .from(itemImg)
                .join(itemImg.item,item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetch();
    }

    @Override
    public List<MainItemDto> getMainItemListShowPage(ItemSearchDto itemSearchDto, Criteria criteria) {
        QItem item=QItem.item;
        QItemImg itemImg=QItemImg.itemImg;
        
        return queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.title,
                                item.itemDetail,
                                itemImg.imgUrl)
                )
                .from(itemImg)
                .join(itemImg.item,item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(criteria.getPageStart()) //데이터를 가지고 올 시작 인데스
                .limit(criteria.getPerPageNum()) //한 번에 가지고 올 최대 개수
                .fetch();
    }

    //사진관리페이지 총 게시물 수
    @Override
    public List<MyPageItemDto> getItemMngListPage(Long mypageId) {

        QMyPageItem myPageItem=QMyPageItem.myPageItem;
        QItemImg itemImg=QItemImg.itemImg;

        return queryFactory
                .select(
                        new QMyPageItemDto(
                                myPageItem.id,
                                myPageItem.item.title,
                                myPageItem.item.itemDetail,
                                itemImg.imgUrl)
                )
                .from(myPageItem,itemImg)
                .join(myPageItem.item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(myPageItem.myPage.id.eq(mypageId))
                .where(itemImg.item.id.eq(myPageItem.item.id))
                .fetch();

    }

    //사진관리페이지에 데이터를 가지고 올 시작 인데스 및 한 번에 가지고 올 최대 개수
    @Override
    public List<MyPageItemDto> getItemMngListShowPage(Criteria criteria,Long mypageId) {

        QMyPageItem myPageItem=QMyPageItem.myPageItem;
        QItemImg itemImg=QItemImg.itemImg;

        return queryFactory
                .select(
                        new QMyPageItemDto(
                                myPageItem.id,
                                myPageItem.item.title,
                                myPageItem.item.itemDetail,
                                itemImg.imgUrl)
                )
                .from(myPageItem,itemImg)
                .join(myPageItem.item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(myPageItem.myPage.id.eq(mypageId))
                .where(itemImg.item.id.eq(myPageItem.item.id))
                .orderBy(myPageItem.regTime.desc())
                .offset(criteria.getPageStart())
                .limit(criteria.getPerPageNum())
                .fetch();

    }

}
