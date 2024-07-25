package com.example.moon.exception;


import com.alibaba.fastjson.JSON;
import com.example.moon.DTO.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {

    //自定义-错误处理函数
    @ExceptionHandler(Exception.class)
    Object handle(HttpServletRequest request, HttpServletResponse response, Throwable e, Model model) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO;
            //返回json
            if (e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                //未知异常
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.UNKNOWN_ERROR);
            }

            try {
                response.setContentType("application/JSON");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioException) {
            }
            return null;

        } else {
            //错误页面跳转
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                //未知异常
                model.addAttribute("message", CustomizeErrorCode.UNKNOWN_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }

}
