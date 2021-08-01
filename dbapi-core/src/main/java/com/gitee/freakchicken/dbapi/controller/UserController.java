package com.gitee.freakchicken.dbapi.controller;

import com.gitee.freakchicken.dbapi.service.UserService;
import com.gitee.freakchicken.dbapi.domain.User;
import com.gitee.freakchicken.dbapi.util.JwtUtils;
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
            return ResponseDto.fail("用户名或者密码错误");
        } else {
            String token = JwtUtils.createToken(user.getId().toString(), user.getPassword());
            return ResponseDto.successWithMsg(token);
        }

    }

    @RequestMapping("/resetPassword")
    public void resetPassword(String password) {

        userService.resetPassword(password);

    }

}
