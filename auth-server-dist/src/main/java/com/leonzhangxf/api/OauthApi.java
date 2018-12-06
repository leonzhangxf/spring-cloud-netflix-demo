package com.leonzhangxf.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Api(tags = "oauth2 apis")
@RestController
public class OauthApi {

    @ApiOperation("userinfo")
    @RequestMapping("/userinfo")
    public Principal userInfo(Principal user) {
        return user;
    }
}
