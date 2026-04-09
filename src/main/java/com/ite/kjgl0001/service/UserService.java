package com.ite.kjgl0001.service;

import com.ite.kjgl0001.pojo.User;
import com.ite.kjgl0001.util.PageUtil;

import java.util.List;

public interface UserService {
    
    User login(String userId, String password);
    
    List<User> findUserList(String userName, String userType, String userPhone, PageUtil pageUtil);
    
    boolean addUser(User user);
    
    boolean checkUserIdExists(String userId);
    
    User findUserById(String userId);
    
    boolean updateUser(User user);
    
    boolean deleteUser(String userId);
}
