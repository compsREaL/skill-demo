package com.real.skill.dao;

import com.real.skill.domain.SkillGoods;
import com.real.skill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/16 14:12
 */
@Mapper
public interface GoodsDao {

    @Select("select sg.skill_price,sg.stock_count,sg.start_date,sg.end_date,g.id,g.goods_name,g.goods_title,g.goods_img,g.goods_detail,g.goods_price,g.goods_stock" +
            "  from skill_goods sg left join goods g on sg.goods_id=g.id")
    public List<GoodsVo> selectGoodVoList();

    @Select("select sg.skill_price,sg.stock_count,sg.start_date,sg.end_date,g.id,g.goods_name,g.goods_title,g.goods_img,g.goods_detail,g.goods_price,g.goods_stock" +
            "  from skill_goods sg left join goods g on sg.goods_id=g.id where g.id=#{goodsId}")
    GoodsVo selectGoodsByGoodsId(@Param("goodsId") long goodsId);

    @Update("update skill_goods set stock_count = stock_count-1 where goods_id=#{goodsId} and stock_count>0")
    public int decreaseStock(SkillGoods g);

    @Update("update skill_goods set stock_count = #{stockCount} where goods_id = #{goodsId}")
    void resetStock(SkillGoods skillGoods);
}
