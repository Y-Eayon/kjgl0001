package com.ite.kjgl0001.service.impl;

import com.ite.kjgl0001.mapper.UserMapper;
import com.ite.kjgl0001.pojo.User;
import com.ite.kjgl0001.service.UserService;
import com.ite.kjgl0001.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public User login(String userId, String password) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        
        User user = userMapper.findByUserId(userId);
        if (user != null && password.equals(user.getUserPassword())) {
            return user;
        }
        return null;
    }
    
    @Override
    public List<User> findUserList(String userName, String userType, String userPhone, PageUtil pageUtil) {
        System.out.println("====== Service: findUserList ======");
        System.out.println("userName: " + userName);
        System.out.println("userType: " + userType);
        System.out.println("userPhone: " + userPhone);
        System.out.println("currentPage: " + pageUtil.getCurrentPage());
        System.out.println("pageSize: " + pageUtil.getPageSize());
        
        int totalSize = (int) userMapper.count(userName, userType, userPhone);
        System.out.println("查询到的总数据量: " + totalSize);
        pageUtil.setTotalSize(totalSize);
        
        int start = (pageUtil.getCurrentPage() - 1) * pageUtil.getPageSize();
        System.out.println("计算 start: " + start);
        
        List<User> list = userMapper.findList(userName, userType, userPhone, start, pageUtil.getPageSize());
        System.out.println("查询到的数据条数: " + (list != null ? list.size() : 0));
        System.out.println("===============================");
        
        return list;
    }
    
    @Override
    public boolean addUser(User user) {
        try {
            System.out.println("====== Service: addUser ======");
            
            // 检查用户账号是否已存在
            if (userMapper.findByUserId(user.getUserId()) != null) {
                System.out.println("用户账号已存在: " + user.getUserId());
                return false;
            }
            
            // 设置创建时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            user.setCreateTime(sdf.format(new Date()));
            
            System.out.println("开始插入用户数据");
            int result = userMapper.insert(user);
            System.out.println("插入结果: " + result);
            System.out.println("========================");
            
            return result > 0;
        } catch (Exception e) {
            System.out.println("添加用户异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean checkUserIdExists(String userId) {
        return userMapper.findByUserId(userId) != null;
    }
    
    @Override
    public User findUserById(String userId) {
        return userMapper.findByUserId(userId);
    }
    
    @Override
    public boolean updateUser(User user) {
        try {
            return userMapper.update(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteUser(String userId) {
        try {
            return userMapper.delete(userId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
