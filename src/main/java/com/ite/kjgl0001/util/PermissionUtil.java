package com.ite.kjgl0001.util;

import com.ite.kjgl0001.pojo.User;

import javax.servlet.http.HttpSession;

public class PermissionUtil {

    public static final String USER_TYPE_STUDENT = "0";
    public static final String USER_TYPE_TEACHER = "1";
    public static final String USER_TYPE_ADMIN = "2";

    public static boolean isAdmin(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return false;
        }

        if (loginUser instanceof User) {
            User user = (User) loginUser;
            return USER_TYPE_ADMIN.equals(user.getUserType());
        }

        return false;
    }

    public static boolean isLogin(HttpSession session) {
        return session.getAttribute("loginUser") != null;
    }

    public static User getLoginUser(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser instanceof User) {
            return (User) loginUser;
        }
        return null;
    }
}
