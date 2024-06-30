package com.example.moon.controller;

import com.example.moon.DTO.AccessTokenDTO;
import com.example.moon.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @GetMapping("callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8887/callback");
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id("Ov23li8yntZn5bSXzZH2");
        accessTokenDTO.setClient_secret("c338f844abdb82272d25ec3b1387fb705606cf53");
        githubProvider.getAccessToken(accessTokenDTO);
        return "index";
    }
}
