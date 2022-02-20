package com.pictureproject.repository;

import com.pictureproject.dto.ItemSearchDto;
import com.pictureproject.dto.MainItemDto;
import com.pictureproject.dto.QMainItemDto;
import com.pictureproject.entity.QItem;
import com.pictureproject.entity.QItemImg;
import com.pictureproject.paging.Criteria;
import com.pictureproject.paging.Paging;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item=QItem.item;
        QItemImg itemImg=QItemImg.itemImg;

        QueryResults<MainItemDto> results=queryFactory
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content=results.getResults();
        long total=results.getTotal();
        return new PageImpl<>(content,pageable,total);
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
}
