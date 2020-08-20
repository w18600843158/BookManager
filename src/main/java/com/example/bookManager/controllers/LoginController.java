package com.example.bookManager.controllers;

import com.example.bookManager.biz.LoginBiz;
import com.example.bookManager.model.User;
import com.example.bookManager.service.UserService;
import com.example.bookManager.util.CookieUtil;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @Autowired
    private LoginBiz loginBiz; // 登录的时候需要一个登录的逻辑过程

    @Autowired
    private UserService userService;

    /**
     * 注册需要 POST 方法, 而默认是 GET 方法, 所以需要一个跳转
     *
     * @return
     */
    @RequestMapping(path = {"/users/register"}, method = {RequestMethod.GET})
    public String register() {
        return "login/register";
    }

    /**
     * 真正的注册方法
     */
    @RequestMapping(path = {"/users/register/do"}, method = {RequestMethod.POST})
    public String doRegister(
            Model model,
            HttpServletResponse response,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        try {
            String t = loginBiz.register(user); // 注册, 生成一个 t 票
            CookieUtil.writeCookie("t", t, response); //t票字符串放入Cookie中
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "404";
        }
    }

    @RequestMapping(path = {"/users/login"}, method = {RequestMethod.GET})
    public String login() {
        return "login/login";
    }

    @RequestMapping(path = {"/users/login/do"}, method = {RequestMethod.POST})
    public String doLogin(
            Model model,
            HttpServletResponse response,
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        try {
            String t = loginBiz.login(email, password);
            CookieUtil.writeCookie("t", t, response);
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "404";
        }
    }

    @RequestMapping(path = {"/users/logout/do"}, method = {RequestMethod.GET})
    public String doLogout(
            @CookieValue("t") String t
    ) {

        loginBiz.logout(t);
        return "redirect:/index";
    }
}
