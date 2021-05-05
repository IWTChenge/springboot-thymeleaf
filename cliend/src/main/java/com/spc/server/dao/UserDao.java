package com.spc.server.dao;

import com.spc.server.pojo.AuthToken;
import com.spc.server.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserDao {
     User searchUser(User user);
//@Select(value = "select * from user")
  List<User> getAllUser();
   User verifyUser(String name,String password);
    //增加
    @SelectKey(statement="call identity()", keyProperty="userId", before=false, resultType=int.class)
    int addUser(User[] users);

 int addOneUser(User user);
    //删
    int DeleteUser(int id);
    //改
    int update(User user);
    //查
    User selectUser(@Param("column") String column, @Param("value") String value);

    User selectByUserId(int id);

    User selectByColumn(@Param("column") String column, @Param("value") String value);

    AuthToken findByToken(String accessToken);

    void updateExpireTime(AuthToken token);

    int save(AuthToken authToken);

    User searchUserByToken(String token);

    Set<String> getRoleByUid(int userId);

    Set<String> findPermissionByRole(@Param("value")Set<String> roles);

    int cleanToken(@Param("token")String token);
}

