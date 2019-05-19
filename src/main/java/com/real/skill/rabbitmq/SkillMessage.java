package com.real.skill.rabbitmq;

import com.real.skill.domain.SkillUser;

/**
 * @author: mabin
 * @create: 2019/5/17 19:24
 */
public class SkillMessage {

    private SkillUser user;

    private Long goodsId;

    public SkillUser getUser() {
        return user;
    }

    public void setUser(SkillUser user) {
        this.user = user;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
