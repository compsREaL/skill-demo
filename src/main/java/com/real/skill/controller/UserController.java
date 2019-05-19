package com.real.skill.controller;

import com.real.skill.domain.SkillUser;
import com.real.skill.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: mabin
 * @create: 2019/5/16 18:09
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ResponseBody
    public Result<SkillUser> info(Model model,SkillUser user){
        return Result.success(user);
    }
}
