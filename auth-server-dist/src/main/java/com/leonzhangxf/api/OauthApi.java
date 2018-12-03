package com.leonzhangxf.api;

import com.leonzhangxf.dao.UserRepository;
import com.leonzhangxf.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Api(tags = "oauth2 apis")
@RestController
public class OauthApi {

    private UserRepository userRepository;

    @ApiOperation("userinfo")
    @RequestMapping("/userinfo")
    public User userInfo(Principal user) {
        return userRepository.findByUsername(user.getName());
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
