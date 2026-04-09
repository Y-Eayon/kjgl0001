package com.ite.kjgl0001.controller;

import com.ite.kjgl0001.pojo.User;
import com.ite.kjgl0001.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String init() {
        System.out.println("访问根路径，重定向到登录页");
        return "redirect:/login.html";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                       @RequestParam("password") String password,
                       HttpSession session) {
        
        System.out.println("收到登录请求: username=" + username + ", password=" + password);
        
        if (username == null || username.trim().isEmpty()) {
            System.out.println("用户名为空");
            return "redirect:/login.html";
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.out.println("密码为空");
            return "redirect:/login.html";
        }
        
        User user = userService.login(username, password);
        
        if (user != null) {
            System.out.println("登录成功: " + user.getUserName());
            session.setAttribute("loginUser", user);
            session.setAttribute("userName", user.getUserName());
            session.setAttribute("userType", user.getUserType());
            return "redirect:/index.html";
        } else {
            System.out.println("登录失败: 用户名或密码错误");
            return "redirect:/login.html";
        }
    }

    @GetMapping("/index.html")
    public String index(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }
        return "index.html";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login.html";
    }
}
