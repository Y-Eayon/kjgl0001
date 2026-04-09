package com.ite.kjgl0001.mapper;

import com.ite.kjgl0001.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    
    /**
     * 根据用户ID查询用户
     */
    User findByUserId(@Param("userId") String userId);
    
    /**
     * 登录验证
     */
    User login(@Param("userId") String userId, @Param("userPassword") String userPassword);
    
    /**
     * 分页查询用户列表（支持多条件）
     */
    List<User> findList(@Param("userName") String userName, 
                       @Param("userType") String userType,
                       @Param("userPhone") String userPhone,
                       @Param("userSex") String userSex,
                       @Param("start") int start, 
                       @Param("pageSize") int pageSize);
    
    /**
     * 统计用户数量（支持多条件）
     */
    long count(@Param("userName") String userName,
               @Param("userType") String userType,
               @Param("userPhone") String userPhone,
               @Param("userSex") String userSex);

    /**
     * 添加用户
     */
    int insert(User user);
    
    /**
     * 更新用户
     */
    int update(User user);
    
    /**
     * 删除用户
     */
    int delete(@Param("userId") String userId);
}
