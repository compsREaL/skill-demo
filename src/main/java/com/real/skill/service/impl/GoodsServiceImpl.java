package com.real.skill.service.impl;

import com.real.skill.dao.GoodsDao;
import com.real.skill.domain.Goods;
import com.real.skill.domain.SkillGoods;
import com.real.skill.service.GoodsService;
import com.real.skill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/16 14:24
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public boolean reduceStock(GoodsVo goods) {
        SkillGoods g = new SkillGoods();
        g.setGoodsId(goods.getId());
        int effectRows = goodsDao.decreaseStock(g);
        return effectRows>0;
    }

    @Override
    public List<GoodsVo> listGoodsVo() {
        return goodsDao.selectGoodVoList();
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.selectGoodsByGoodsId(goodsId);
    }

    @Override
    public void resetStock(List<GoodsVo> goodsList) {
        for (GoodsVo goods:goodsList){
            SkillGoods skillGoods = new SkillGoods();
            skillGoods.setGoodsId(goods.getId());
            skillGoods.setStockCount(goods.getStockCount());
            goodsDao.resetStock(skillGoods);
        }
    }
}
