package com.real.skill.service;

import com.real.skill.domain.OrderInfo;
import com.real.skill.domain.SkillUser;
import com.real.skill.vo.GoodsVo;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/16 16:28
 */
public interface SkillService {

    /**
     * 生成秒杀订单
     *
     * @param user
     * @param goods
     * @return
     */
    OrderInfo skill(SkillUser user, GoodsVo goods);

    /**
     * 重置数据库与redis相关数据，用于Jmeter压测
     *
     * @param goodsList
     */
    void reset(List<GoodsVo> goodsList);

    /**
     * 从redis缓存中获取秒杀接口数据并进行验证
     *
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    boolean check(SkillUser user, long goodsId, String path);

    /**
     * 根据用户的id和秒杀商品的id生成随机秒杀地址，并存储于redis缓存中
     *
     * @param user
     * @param goodsId
     * @return
     */
    String createPath(SkillUser user, long goodsId);

    /**
     * 生成验证码图片
     *
     * @param user
     * @param goodsId
     * @return
     */
    BufferedImage createVerifyCodeImg(SkillUser user, long goodsId);

    /**
     * 验证验证码是否正确
     *
     * @param verifyCodeActual
     * @param user
     * @param goodsId
     * @return
     */
    boolean checkVerifyCode(int verifyCodeActual, SkillUser user, long goodsId);
}
