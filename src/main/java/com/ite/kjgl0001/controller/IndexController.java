package com.ite.kjgl0001.controller;

import com.ite.kjgl0001.pojo.User;
import com.ite.kjgl0001.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 首页控制器
 * 处理登录、登出、首页等基础功能
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    /**
     * 根路径访问，重定向到登录页面
     * @return 重定向到登录页
     */
    @GetMapping("/")
    public String init() {
        System.out.println("访问根路径，重定向到登录页");
        return "redirect:/login.html";
    }

    /**
     * 显示登录页面
     * @return 登录页面视图
     */
    @GetMapping("/login.html")
    public String loginPage() {
        System.out.println("访问登录页面");
        return "login.html";
    }

    /**
     * 处理用户登录请求
     * @param username 用户名
     * @param password 密码
     * @param session HTTP会话对象
     * @return 登录成功跳转到首页，失败返回登录页并显示错误信息
     * @throws UnsupportedEncodingException URL编码异常
     */
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                       @RequestParam("password") String password,
                       HttpSession session) throws UnsupportedEncodingException {
        
        System.out.println("收到登录请求: username=" + username + ", password=" + password);
        
        // 验证用户名是否为空
        if (username == null || username.trim().isEmpty()) {
            System.out.println("用户名为空");
            return "redirect:/login.html";
        }
        
        // 验证密码是否为空
        if (password == null || password.trim().isEmpty()) {
            System.out.println("密码为空");
            return "redirect:/login.html";
        }
        
        // 调用Service层进行登录验证
        User user = userService.login(username, password);
        
        if (user != null) {
            // 登录成功，将用户信息存入Session
            System.out.println("登录成功: " + user.getUserName());
            session.setAttribute("loginUser", user);
            session.setAttribute("userName", user.getUserName());
            session.setAttribute("userType", user.getUserType());
            return "redirect:/index.html";
        } else {
            // 登录失败，返回错误信息
            System.out.println("登录失败: 用户名或密码错误");
            return "redirect:/login.html?error=" + URLEncoder.encode("用户名或密码错误", "UTF-8");
        }
    }

    /**
     * 显示首页（需要登录）
     * @param session HTTP会话对象
     * @return 首页视图或重定向到登录页
     */
    @GetMapping("/index.html")
    public String index(HttpSession session) {
        // 检查用户是否已登录
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }
        return "index.html";
    }

    /**
     * 密码修改页面（需要登录）
     * @param session HTTP会话对象
     * @return 密码修改页面视图
     */
    @GetMapping("/password.html")
    public String passwordPage(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }
        return "password.html";
    }

    /**
     * 用户登出，清除Session
     * @param session HTTP会话对象
     * @return 重定向到登录页
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 使Session失效
        return "redirect:/login.html";
    }
}
