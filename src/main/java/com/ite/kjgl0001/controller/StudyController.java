package com.ite.kjgl0001.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * 课件管理控制器
 * 处理课件的相关页面跳转
 */
@Controller
public class StudyController {

    /**
     * 课件列表页面（需要登录）
     * @param session HTTP会话对象
     * @return 课件列表页面视图
     */
    @GetMapping("/studyList.html")
    public String studyList(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }
        return "studyList.html";
    }

    /**
     * 添加课件页面（需要登录）
     * @param session HTTP会话对象
     * @return 添加课件页面视图
     */
    @GetMapping("/studyAdd.html")
    public String studyAddPage(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }
        return "studyAdd.html";
    }

    /**
     * 查看课件详情页面（需要登录）
     * @param session HTTP会话对象
     * @return 课件详情页面视图
     */
    @GetMapping("/studyView.html")
    public String studyViewPage(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }
        return "studyView.html";
    }

    /**
     * 修改课件页面（需要登录）
     * @param session HTTP会话对象
     * @return 修改课件页面视图
     */
    @GetMapping("/studyUpdate.html")
    public String studyUpdatePage(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }
        return "studyUpdate.html";
    }
}
