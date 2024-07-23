package com.example.moon.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomizeExceptionHandler {

    //自定义-错误处理函数
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model){
//        HttpStatus status = getStatus(request);
        if(e instanceof CustomizeException){
            model.addAttribute("message",e.getMessage());
        }else {
            //未知异常
            model.addAttribute("message",CustomizeErrorCode.UNKNOWN_ERROR.getMessage());
        }
        return new ModelAndView("error");
    }

    //错误码，暂时用不到。
//    private HttpStatus getStatus(HttpServletRequest request){
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        if(statusCode==null){
//            return HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return HttpStatus.valueOf(statusCode);
//    }
}
