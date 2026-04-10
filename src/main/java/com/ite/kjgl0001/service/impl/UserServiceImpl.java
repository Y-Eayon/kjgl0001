package com.ite.kjgl0001.service.impl;

import com.ite.kjgl0001.mapper.UserMapper;
import com.ite.kjgl0001.pojo.User;
import com.ite.kjgl0001.service.UserService;
import com.ite.kjgl0001.util.AgeUtil;
import com.ite.kjgl0001.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户服务实现类
 * 实现用户相关的业务逻辑，包括登录、增删改查等功能
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 用户登录验证
     * 验证用户账号和密码是否正确
     * 
     * @param userId 用户账号
     * @param password 用户密码
     * @return 登录成功返回User对象，失败返回null
     */
    @Override
    public User login(String userId, String password) {
        // 参数校验：账号不能为空
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }
        // 参数校验：密码不能为空
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        
        // 根据账号查询用户
        User user = userMapper.findByUserId(userId);
        
        // 验证用户是否存在且密码匹配
        if (user != null && password.equals(user.getUserPassword())) {
            return user;
        }
        return null;
    }
    
    /**
     * 分页查询用户列表（支持多条件模糊查询）
     * 业务流程：
     * 1. 查询符合条件的总记录数
     * 2. 设置分页参数（总记录数、总页数）
     * 3. 计算起始位置，查询当前页数据
     * 4. 动态计算每个用户的年龄
     * 
     * @param userName 用户名（模糊查询，可为null）
     * @param userType 用户类型：0-学员/1-老师/2-管理员（可为null）
     * @param userPhone 手机号（模糊查询，可为null）
     * @param userSex 性别：0-女/1-男（可为null）
     * @param pageUtil 分页工具对象
     * @return 当前页的用户列表
     */
    @Override
    public List<User> findUserList(String userName, String userType, String userPhone, String userSex, PageUtil pageUtil) {
        System.out.println("====== Service: findUserList ======");
        System.out.println("userName: " + userName);
        System.out.println("userType: " + userType);
        System.out.println("userPhone: " + userPhone);
        System.out.println("userSex: " + userSex);
        System.out.println("currentPage: " + pageUtil.getCurrentPage());
        System.out.println("pageSize: " + pageUtil.getPageSize());
        
        // 第1步：查询符合条件的总记录数
        int totalSize = (int) userMapper.count(userName, userType, userPhone, userSex);
        System.out.println("查询到的总数据量: " + totalSize);
        
        // 第2步：设置分页参数（用于前端显示分页信息）
        pageUtil.setTotalSize(totalSize);
        
        // 第3步：计算SQL查询的起始位置（LIMIT start, pageSize）
        int start = (pageUtil.getCurrentPage() - 1) * pageUtil.getPageSize();
        System.out.println("计算 start: " + start);
        
        // 第4步：查询当前页的数据
        List<User> list = userMapper.findList(userName, userType, userPhone, userSex, start, pageUtil.getPageSize());
        System.out.println("查询到的数据条数: " + (list != null ? list.size() : 0));
        
        // 第5步：动态计算每个用户的年龄（根据出生日期）
        for (User user : list) {
            if (user.getBirthday() != null && !user.getBirthday().trim().isEmpty()) {
                user.setUserAge(AgeUtil.calculateAge(user.getBirthday()));
            }
        }
        
        System.out.println("===============================");
        
        return list;
    }
    
    /**
     * 添加新用户
     * 业务流程：
     * 1. 检查用户账号是否已存在（防止重复）
     * 2. 设置创建时间为当前时间
     * 3. 执行插入操作
     * 
     * @param user 用户对象
     * @return 添加成功返回true，失败返回false
     */
    @Override
    public boolean addUser(User user) {
        try {
            System.out.println("====== Service: addUser ======");
            
            // 第1步：检查用户账号是否已存在
            if (userMapper.findByUserId(user.getUserId()) != null) {
                System.out.println("用户账号已存在: " + user.getUserId());
                return false;
            }
            
            // 第2步：设置创建时间为当前时间（格式：yyyy-MM-dd HH:mm:ss）
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            user.setCreateTime(sdf.format(new Date()));
            
            // 第3步：执行插入操作
            System.out.println("开始插入用户数据");
            int result = userMapper.insert(user);
            System.out.println("插入结果: " + result);
            System.out.println("========================");
            
            // 判断插入是否成功（影响行数>0表示成功）
            return result > 0;
        } catch (Exception e) {
            System.out.println("添加用户异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 检查用户账号是否已存在
     * 
     * @param userId 用户账号
     * @return 已存在返回true，不存在返回false
     */
    @Override
    public boolean checkUserIdExists(String userId) {
        return userMapper.findByUserId(userId) != null;
    }
    
    /**
     * 根据用户ID查询用户详情
     * 查询后会动态计算用户年龄
     * 
     * @param userId 用户账号
     * @return 用户对象，不存在返回null
     */
    @Override
    public User findUserById(String userId) {
        // 查询用户基本信息
        User user = userMapper.findByUserId(userId);
        
        // 如果有出生日期，动态计算年龄
        if (user != null && user.getBirthday() != null && !user.getBirthday().trim().isEmpty()) {
            user.setUserAge(AgeUtil.calculateAge(user.getBirthday()));
        }
        
        return user;
    }
    
    /**
     * 更新用户信息
     * 
     * @param user 用户对象（包含更新后的数据）
     * @return 更新成功返回true，失败返回false
     */
    @Override
    public boolean updateUser(User user) {
        try {
            // 执行更新操作，返回影响行数
            return userMapper.update(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 删除用户
     * 
     * @param userId 要删除的用户账号
     * @return 删除成功返回true，失败返回false
     */
    @Override
    public boolean deleteUser(String userId) {
        try {
            // 执行删除操作，返回影响行数
            return userMapper.delete(userId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
