package com.leonzhangxf;

import com.leonzhangxf.configuration.SwaggerConfiguration;
import com.leonzhangxf.dao.UserRepository;
import com.leonzhangxf.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@Import({
        SwaggerConfiguration.class,
        AuthorizationServerConfiguration.class,
        WebSecurityConfiguration.class,
        ResourceServerConfiguration.class
})
@Controller
public class AuthServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthServerApplication.class).web(WebApplicationType.SERVLET).run(args);
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("userinfo")
    @ResponseBody
    public User userInfo() {
        List<User> all = userRepository.findAll();
        System.out.println(all);
        User user = userRepository.findByUsername("admin");
        System.out.println(user);
        return user;
    }
}
