package com.real.skill.vo;

import com.real.skill.domain.Goods;

import java.util.Date;

/**
 * @author: mabin
 * @create: 2019/5/16 14:14
 */
public class GoodsVo extends Goods {

    private Double skillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

    public Double getSkillPrice() {
        return skillPrice;
    }

    public void setSkillPrice(Double skillPrice) {
        this.skillPrice = skillPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
