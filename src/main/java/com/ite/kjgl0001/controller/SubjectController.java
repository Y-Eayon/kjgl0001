package com.ite.kjgl0001.controller;

import com.ite.kjgl0001.util.PermissionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * 科目管理控制器
 * 处理科目的相关页面跳转
 */
@Controller
public class SubjectController {

    /**
     * 科目列表页面（需要登录）
     * @param session HTTP会话对象
     * @param model Spring MVC模型对象
     * @return 科目列表页面视图
     */
    @GetMapping("/subjectList.html")
    public String subjectList(HttpSession session, Model model) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }
        return "subjectList.html";
    }

    /**
     * 添加科目页面（需要登录）
     * @param session HTTP会话对象
     * @return 添加科目页面视图
     */
    @GetMapping("/subjectAdd.html")
    public String subjectAddPage(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login.html";
        }
        return "subjectAdd.html";
    }
}
