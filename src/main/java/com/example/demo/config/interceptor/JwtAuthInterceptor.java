package com.example.demo.config.interceptor;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.demo.config.BaseResponseStatus.EMPTY_JWT;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("[preHandle]");

        String accessToken = request.getHeader("X-ACCESS-TOKEN");
        if(accessToken == null) {
            System.out.println("access 토큰 없음");
            return false;
        }

        // 만료시간 확인
        if(!jwtService.validateToken(accessToken)){
            System.out.println("access 토큰 만료됨");
            return false;
        }

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("[postHandle]");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("[afterCompletion]");
    }
}
