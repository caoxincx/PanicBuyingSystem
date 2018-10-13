package it.caoxin.controller;

import it.caoxin.domain.User;
import it.caoxin.result.CodeMsg;
import it.caoxin.result.Result;
import it.caoxin.service.UserService;
import it.caoxin.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/demo")
public class TestController {

    @Autowired
    private UserService userService;

    @RequestMapping("hello")
    @ResponseBody
    public Result<CodeMsg> helloSpringBoot(){
        return Result.success(CodeMsg.SUCCESS);
    }


    @RequestMapping("error")
    @ResponseBody
    public Result<CodeMsg> errorTest(){
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("hellothemaleaf")
    public String themaleaf(Model model){
        model.addAttribute("name","caoxin");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet(){
        User user = userService.getUserById(18320338949L);
        return Result.success(user);
    }

    @PostMapping("/db/post")
    @ResponseBody
    public Result<CodeMsg>dbPost(){
        for(int i=0;i<5000;i++) {
            User user = new User();
            user.setId(13000000000L+i);
            user.setLoginCount(1);
            user.setNickname("user"+i);
            user.setRegisterDate(new Date());
            user.setSalt("1a2b3c");
            user.setPassword(MD5Util.inputPasswordToDBPass("123456", user.getSalt()));
            userService.insert(user);
        }
        return Result.success(CodeMsg.SUCCESS);
    }

}
