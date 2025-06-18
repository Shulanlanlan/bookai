package com.zsl.bookai.controller;

import com.zsl.bookai.entity.UserEntity;
import com.zsl.bookai.model.ApiResult;
import com.zsl.bookai.model.UserLogin;
import com.zsl.bookai.repo.UserRepo;
import com.zsl.bookai.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {
    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ApiResult<String> Login(@RequestBody UserLogin userLogin) {
        var user = userRepo.getByUsername(userLogin.username);
        if (user.isPresent()) {
            var user1 = user.get();
            if (user1.password.equals(userLogin.password)) {
                return success(jwtUtil.generateToken(user1.username));
            }
        }
        return fail("login failed");
    }

    @PostMapping("/register")
    public ApiResult<String> register(@RequestBody UserLogin newUser) {
        var user = userRepo.getByUsername(newUser.username);
        if (user.isPresent()) {
            return fail("user already exists");
        }
        try {
            var user1 = userRepo.save(new UserEntity(null, newUser.username, newUser.password));
            return success(jwtUtil.generateToken(user1.username));
        } catch (Exception e) {
            return fail("failed");
        }
    }
}
