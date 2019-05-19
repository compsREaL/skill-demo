package com.real.skill.service;

import com.real.skill.vo.GoodsVo;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/16 14:13
 */
public interface GoodsService {


    public boolean reduceStock(GoodsVo goods);

    public List<GoodsVo> listGoodsVo();

    GoodsVo getGoodsVoByGoodsId(long goodsId);

    void resetStock(List<GoodsVo> goodsList);
}
