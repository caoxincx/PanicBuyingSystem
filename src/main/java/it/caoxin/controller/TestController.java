package it.caoxin.controller;

import it.caoxin.domain.User;
import it.caoxin.result.CodeMsg;
import it.caoxin.result.Result;
import it.caoxin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        User user = new User();
        user.setId(12345431L);
        user.setNickname("caoxin");
        user.setPassword("caoxin");

        int insert = userService.insert(user);
        return Result.success(CodeMsg.SUCCESS);
    }

}
