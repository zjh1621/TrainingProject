package com.example.moon.controller;

import com.example.moon.DTO.AccessTokenDTO;
import com.example.moon.DTO.GithubUser;
import com.example.moon.mapper.UserMapper;
import com.example.moon.model.User;
import com.example.moon.provider.GithubProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String client_id;
    @Value("${github.client.secret}")
    private String client_secret;
    @Value("${github.redirect.uri}")
    private String redirect_uri;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccount_id(String.valueOf(githubUser.getId()));
            user.setGmt_Create(System.currentTimeMillis());
            user.setGmt_Modified(user.getGmt_Create());
            userMapper.insert(user);
            //登陆成功，写cookie和session
            request.getSession().setAttribute("githubUser",githubUser);
            return "redirect:/";
        }
        else{
            //登录失败，重新登录
            return "redirect:/";
        }
    }
}
