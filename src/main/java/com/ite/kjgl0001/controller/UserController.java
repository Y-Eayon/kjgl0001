package com.ite.kjgl0001.controller;

import com.ite.kjgl0001.pojo.User;
import com.ite.kjgl0001.service.UserService;
import com.ite.kjgl0001.util.PageUtil;
import com.ite.kjgl0001.util.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户管理控制器
 * 处理用户的增删改查操作
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户列表页面（支持分页和条件查询）
     * @param pageNum 当前页码，默认第1页
     * @param pageSize 每页条数，默认10条
     * @param username 用户名（模糊查询）
     * @param userType 用户类型（0-学员/1-老师/2-管理员）
     * @param userPhone 手机号（模糊查询）
     * @param userSex 性别（0-女/1-男）
     * @param model Spring MVC模型对象，用于传递数据到视图
     * @param session HTTP会话对象
     * @return 用户列表页面视图
     */
    @GetMapping("/userList.html")
    public String userList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                          @RequestParam(value = "username", required = false) String username,
                          @RequestParam(value = "userType", required = false) String userType,
                          @RequestParam(value = "userPhone", required = false) String userPhone,
                          @RequestParam(value = "userSex", required = false) String userSex,
                          Model model,
                          HttpSession session) {
        
        // 权限检查：必须登录才能访问
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }

        // 创建分页工具类对象
        PageUtil pageUtil = new PageUtil();
        pageUtil.setCurrentPage(pageNum);
        pageUtil.setPageSize(pageSize);
        
        System.out.println("====== 开始查询用户列表 ======");
        System.out.println("pageNum: " + pageNum + ", pageSize: " + pageSize);
        System.out.println("username: " + username + ", userType: " + userType + ", userPhone: " + userPhone + ", userSex: " + userSex);
        
        // 调用Service层查询用户列表（带分页和条件）
        List<User> userList = userService.findUserList(username, userType, userPhone, userSex, pageUtil);
        
        System.out.println("查询到的用户数量: " + (userList != null ? userList.size() : 0));
        System.out.println("总数据量: " + pageUtil.getTotalSize());
        System.out.println("总页数: " + pageUtil.getTotalPage());
        System.out.println("==============================");
        
        // 将数据传递到视图层
        model.addAttribute("userList", userList);
        model.addAttribute("pageUtil", pageUtil);
        model.addAttribute("username", username);
        model.addAttribute("userType", userType);
        model.addAttribute("userPhone", userPhone);
        model.addAttribute("userSex", userSex);
        return "userList.html";
    }

    /**
     * 查看用户详情
     * @param userId 用户ID
     * @param model Spring MVC模型对象
     * @param session HTTP会话对象
     * @return 用户详情页面视图
     */
    @GetMapping("/userView.html")
    public String viewUser(@RequestParam("userId") String userId, Model model, HttpSession session) {
        // 权限检查
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }

        // 根据ID查询用户
        User user = userService.findUserById(userId);
        if (user == null) {
            return "redirect:/userList.html?error=" + encodeParam("用户不存在");
        }
        
        System.out.println("====== 查看用户详情 ======");
        System.out.println("userId: " + userId);
        System.out.println("用户信息: " + user);
        System.out.println("===========================");
        
        model.addAttribute("user", user);
        return "userView.html";
    }

    /**
     * 显示添加用户页面（仅管理员可访问）
     * @param session HTTP会话对象
     * @return 添加用户页面视图
     */
    @GetMapping("/userAdd.html")
    public String addUserPage(HttpSession session) {
        // 权限检查：只有管理员可以访问
        if (!PermissionUtil.isAdmin(session)) {
            return "redirect:/index.html?error=" + encodeParam("没有权限访问");
        }
        return "userAdd.html";
    }

    /**
     * 处理添加用户请求（仅管理员可操作）
     * @param user 用户对象（从表单自动绑定）
     * @param session HTTP会话对象
     * @return 成功跳转到用户列表，失败返回添加页面并显示错误
     */
    @PostMapping("/user/add")
    public String addUser(User user, HttpSession session) {
        // 权限检查
        if (!PermissionUtil.isAdmin(session)) {
            return "redirect:/index.html?error=" + encodeParam("没有权限执行此操作");
        }

        System.out.println("====== 添加用户 ======");
        System.out.println("userId: " + user.getUserId());
        System.out.println("userName: " + user.getUserName());
        System.out.println("userType: " + user.getUserType());
        
        try {
            // 先检查用户账号是否已存在
            boolean exists = userService.checkUserIdExists(user.getUserId());
            if (exists) {
                System.out.println("添加失败：用户账号已存在");
                return "redirect:/userAdd.html?error=" + encodeParam("该用户账号已存在，请更换其他账号");
            }
            
            // 执行添加操作
            boolean success = userService.addUser(user);
            if (success) {
                System.out.println("添加成功");
                return "redirect:/userList.html";
            } else {
                System.out.println("添加失败");
                return "redirect:/userAdd.html?error=" + encodeParam("添加失败，请稍后重试");
            }
        } catch (Exception e) {
            System.out.println("添加用户异常: " + e.getMessage());
            e.printStackTrace();
            // 处理数据库唯一性约束异常
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("Duplicate")) {
                errorMsg = "该用户账号已存在，请更换其他账号";
            }
            return "redirect:/userAdd.html?error=" + encodeParam(errorMsg);
        }
    }

    /**
     * 异步检查用户账号是否已存在（AJAX接口）
     * @param userId 用户账号
     * @return JSON格式：{"available": true/false}
     */
    @GetMapping("/user/checkUserId")
    @ResponseBody
    public Map<String, Object> checkUserId(@RequestParam("userId") String userId) {
        Map<String, Object> result = new HashMap<>();
        boolean exists = userService.checkUserIdExists(userId);
        result.put("available", !exists); // available=true表示账号可用
        return result;
    }

    /**
     * 显示修改用户页面（仅管理员可访问）
     * @param userId 用户ID
     * @param model Spring MVC模型对象
     * @param session HTTP会话对象
     * @return 修改用户页面视图
     */
    @GetMapping("/userUpdate.html")
    public String updateUserPage(@RequestParam("userId") String userId, Model model, HttpSession session) {
        // 权限检查
        if (!PermissionUtil.isAdmin(session)) {
            return "redirect:/index.html?error=" + encodeParam("没有权限访问");
        }

        // 查询要修改的用户信息
        User user = userService.findUserById(userId);
        if (user == null) {
            return "redirect:/userList.html?error=" + encodeParam("用户不存在");
        }
        
        System.out.println("====== 查询用户信息用于修改 ======");
        System.out.println("userId: " + userId);
        System.out.println("用户信息: " + user);
        System.out.println("===============================");
        
        model.addAttribute("user", user);
        return "userUpdate.html";
    }

    /**
     * 处理更新用户请求（仅管理员可操作）
     * @param user 用户对象（包含更新后的数据）
     * @param session HTTP会话对象
     * @return 成功跳转到用户列表，失败返回修改页面
     */
    @PostMapping("/user/update")
    public String updateUser(User user, HttpSession session) {
        // 权限检查
        if (!PermissionUtil.isAdmin(session)) {
            return "redirect:/index.html?error=" + encodeParam("没有权限执行此操作");
        }

        System.out.println("====== 更新用户信息 ======");
        System.out.println("userId: " + user.getUserId());
        System.out.println("userName: " + user.getUserName());
        System.out.println("userSex: " + user.getUserSex());
        System.out.println("userType: " + user.getUserType());
        
        // 执行更新操作
        boolean success = userService.updateUser(user);
        System.out.println("更新结果: " + success);
        System.out.println("===========================");
        
        if (success) {
            return "redirect:/userList.html";
        } else {
            return "redirect:/userUpdate.html?userId=" + user.getUserId() + "&error=" + encodeParam("修改失败，请稍后重试");
        }
    }

    /**
     * 处理删除用户请求（仅管理员可操作）
     * @param userId 要删除的用户ID
     * @param session HTTP会话对象
     * @return 重定向到用户列表页面
     */
    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam("userId") String userId, HttpSession session) {
        // 权限检查
        if (!PermissionUtil.isAdmin(session)) {
            return "redirect:/index.html?error=" + encodeParam("没有权限执行此操作");
        }

        // 执行删除操作
        boolean success = userService.deleteUser(userId);
        return "redirect:/userList.html";
    }

    /**
     * URL参数编码工具方法（处理中文乱码）
     * @param param 需要编码的参数
     * @return 编码后的字符串
     */
    private String encodeParam(String param) {
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return param;
        }
    }
}
