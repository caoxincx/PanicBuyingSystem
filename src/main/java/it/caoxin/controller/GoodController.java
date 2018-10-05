package it.caoxin.controller;

import it.caoxin.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodController {
    @RequestMapping("/list")
    public String good(Model model, User user){
        model.addAttribute("user",user);
        return "good";
    }
}
