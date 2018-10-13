package it.caoxin.controller;

import it.caoxin.result.Result;
import it.caoxin.service.UserService;
import it.caoxin.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {
    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/loginIn")
    @ResponseBody
    public Result<String> LoginIn(HttpServletResponse response, @Valid LoginVo loginVo){
        log.info(loginVo.toString());
        String token = userService.login(response, loginVo);
        return Result.success(token);
    }
}
