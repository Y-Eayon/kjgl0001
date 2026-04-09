package com.ite.kjgl0001.controller;

import com.ite.kjgl0001.pojo.User;
import com.ite.kjgl0001.service.UserService;
import com.ite.kjgl0001.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userList.html")
    public String userList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                          @RequestParam(value = "username", required = false) String username,
                          @RequestParam(value = "userType", required = false) String userType,
                          @RequestParam(value = "userPhone", required = false) String userPhone,
                          @RequestParam(value = "userSex", required = false) String userSex,
                          Model model,
                          HttpSession session) {
        
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }

        PageUtil pageUtil = new PageUtil();
        pageUtil.setCurrentPage(pageNum);
        pageUtil.setPageSize(pageSize);
        
        System.out.println("====== 开始查询用户列表 ======");
        System.out.println("pageNum: " + pageNum + ", pageSize: " + pageSize);
        System.out.println("username: " + username + ", userType: " + userType + ", userPhone: " + userPhone + ", userSex: " + userSex);
        
        List<User> userList = userService.findUserList(username, userType, userPhone, userSex, pageUtil);
        
        System.out.println("查询到的用户数量: " + (userList != null ? userList.size() : 0));
        System.out.println("总数据量: " + pageUtil.getTotalSize());
        System.out.println("总页数: " + pageUtil.getTotalPage());
        System.out.println("==============================");
        
        model.addAttribute("userList", userList);
        model.addAttribute("pageUtil", pageUtil);
        model.addAttribute("username", username);
        model.addAttribute("userType", userType);
        model.addAttribute("userPhone", userPhone);
        model.addAttribute("userSex", userSex);
        return "userList.html";
    }

    @GetMapping("/userView.html")
    public String viewUser(@RequestParam("userId") String userId, Model model, HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }

        User user = userService.findUserById(userId);
        if (user == null) {
            return "redirect:/userList.html?error=用户不存在";
        }
        
        System.out.println("====== 查看用户详情 ======");
        System.out.println("userId: " + userId);
        System.out.println("用户信息: " + user);
        System.out.println("===========================");
        
        model.addAttribute("user", user);
        return "userView.html";
    }

    @GetMapping("/userAdd.html")
    public String addUserPage(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }
        return "userAdd.html";
    }

    @PostMapping("/user/add")
    public String addUser(User user, HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }

        System.out.println("====== 添加用户 ======");
        System.out.println("userId: " + user.getUserId());
        System.out.println("userName: " + user.getUserName());
        System.out.println("userType: " + user.getUserType());
        
        try {
            boolean success = userService.addUser(user);
            if (success) {
                System.out.println("添加成功");
                return "redirect:/userList.html";
            } else {
                System.out.println("添加失败");
                return "redirect:/userAdd.html?error=添加失败，请稍后重试";
            }
        } catch (Exception e) {
            System.out.println("添加用户异常: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/userAdd.html?error=" + e.getMessage();
        }
    }

    @GetMapping("/user/checkUserId")
    @ResponseBody
    public Map<String, Object> checkUserId(@RequestParam("userId") String userId) {
        Map<String, Object> result = new HashMap<>();
        boolean exists = userService.checkUserIdExists(userId);
        result.put("available", !exists);
        return result;
    }

    @GetMapping("/userUpdate.html")
    public String updateUserPage(@RequestParam("userId") String userId, Model model, HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }

        User user = userService.findUserById(userId);
        if (user == null) {
            return "redirect:/userList.html?error=用户不存在";
        }
        
        System.out.println("====== 查询用户信息用于修改 ======");
        System.out.println("userId: " + userId);
        System.out.println("用户信息: " + user);
        System.out.println("===============================");
        
        model.addAttribute("user", user);
        return "userUpdate.html";
    }

    @PostMapping("/user/update")
    public String updateUser(User user, HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }

        System.out.println("====== 更新用户信息 ======");
        System.out.println("userId: " + user.getUserId());
        System.out.println("userName: " + user.getUserName());
        System.out.println("userSex: " + user.getUserSex());
        System.out.println("userType: " + user.getUserType());
        
        boolean success = userService.updateUser(user);
        System.out.println("更新结果: " + success);
        System.out.println("===========================");
        
        if (success) {
            return "redirect:/userList.html";
        } else {
            return "redirect:/userUpdate.html?userId=" + user.getUserId() + "&error=修改失败，请稍后重试";
        }
    }

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam("userId") String userId, HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }

        boolean success = userService.deleteUser(userId);
        return "redirect:/userList.html";
    }
}
