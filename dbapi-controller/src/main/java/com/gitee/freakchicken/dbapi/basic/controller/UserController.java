package com.gitee.freakchicken.dbapi.basic.controller;

import com.gitee.freakchicken.dbapi.basic.service.UserService;
import com.gitee.freakchicken.dbapi.basic.domain.User;
import com.gitee.freakchicken.dbapi.basic.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gitee.freakchicken.dbapi.common.ResponseDto;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public ResponseDto login(String username, String password) {

        User user = userService.getUser(username, password);
        if (user == null) {
            return ResponseDto.fail("Username or password incorrect!");
        } else {
            String token = JwtUtils.createToken(user.getId().toString(), user.getPassword());
            user.setToken(token);
            return ResponseDto.successWithData(user);
        }

    }

    @RequestMapping("/resetPassword")
    public ResponseDto resetPassword(String userId, String oldPassword, String newPassword) {

        return userService.resetPassword(userId, oldPassword, newPassword);

    }

}
