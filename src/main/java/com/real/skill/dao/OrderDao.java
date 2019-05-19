package com.real.skill.dao;

import com.real.skill.domain.OrderInfo;
import com.real.skill.domain.SkillOrder;
import org.apache.ibatis.annotations.*;

/**
 * @author: mabin
 * @create: 2019/5/16 16:33
 */
@Mapper
public interface OrderDao {

    /**
     * 根据用户id和商品id查询订单
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @Select("select id,user_id,order_id,goods_id from skill_order where user_id=#{userId} and goods_id = #{goodsId}")
    SkillOrder selectOrderByUserIdAndGoodsId(@Param("userId") long userId,@Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id,goods_id,delivery_addr_id,goods_name,goods_count,goods_price,order_channel,status,create_date,pay_date)" +
            "values(#{userId},#{goodsId},#{deliveryAddrId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate},#{payDate})")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    public long insertOrderInfo(OrderInfo orderInfo);

    @Insert("insert into skill_order(user_id,order_id,goods_id) values (#{userId},#{orderId},#{goodsId})")
    public void insertSkillOrder(SkillOrder skillOrder);

    @Select("select id,user_id,goods_id,delivery_addr_id,goods_name,goods_count,goods_price,order_channel,status,create_date,pay_date from order_info" +
            " where id=#{orderId}")
    OrderInfo selectOrderById(long orderId);

    @Delete("delete from order_info")
    void deleteOrders();

    @Delete("delete from skill_order")
    void deleteSkillOrders();
}
