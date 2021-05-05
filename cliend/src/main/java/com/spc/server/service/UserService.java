package com.spc.server.service;


import com.spc.server.pojo.AuthToken;
import com.spc.server.pojo.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<User> getAllUser();

    User verifyUser(String username, String password);

    //增加
    String addUsers(User[] users);

    int addOneUser(User user);

    //删
    String DeleteUser(int id);

    //改
    String update(User user);

    //查
    User selectUser(int id);

    User selectUserByColumn(String column, String value);

    Set<String> findRoleByUsername(String userName);

    Set<String > findPermissionByRole(Set<String > roles);

    AuthToken createToken(String  userName);

    void logout(String token);

    AuthToken findByToken(String accessToken);

    Set<String> getRoleByUid(int userId);

    void updateExpireTime(AuthToken token);

    User verify( String token);

    User selectUserByToken(String token);
    int cleanToken(String token);
}
