package com.mycompany.onlineexam.service;

import com.mycompany.onlineexam.domain.Role;
import com.mycompany.onlineexam.domain.User;
import com.mycompany.onlineexam.web.model.RefreshTokenModel;
import com.mycompany.onlineexam.web.model.TokenModel;

import java.util.List;

public interface UserService {


    User findUserByUsername(String username);

    User save(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    List<User> getAllUsers();

    TokenModel getToken(String username, String password);

    TokenModel createNewTokenByRefreshToken(RefreshTokenModel refreshTokenModel);
}
