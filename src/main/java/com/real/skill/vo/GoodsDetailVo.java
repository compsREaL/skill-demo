package com.real.skill.vo;

import com.real.skill.domain.SkillUser;

/**
 * @author: mabin
 * @create: 2019/5/17 13:07
 */
public class GoodsDetailVo {

    private int skillStatus;

    private int remainSeconds;

    private GoodsVo goods;

    private SkillUser user;

    public int getSkillStatus() {
        return skillStatus;
    }

    public void setSkillStatus(int skillStatus) {
        this.skillStatus = skillStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public SkillUser getUser() {
        return user;
    }

    public void setUser(SkillUser user) {
        this.user = user;
    }
}
