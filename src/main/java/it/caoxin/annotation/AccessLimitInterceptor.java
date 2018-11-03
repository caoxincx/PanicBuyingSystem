package it.caoxin.annotation;

import com.alibaba.fastjson.JSON;
import it.caoxin.domain.User;
import it.caoxin.redis.RedisService;
import it.caoxin.redis.key.AccessKey;
import it.caoxin.result.CodeMsg;
import it.caoxin.result.Result;
import it.caoxin.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod){
            User user  = getUser(request,response);
            UserThreadLocal.setUser(user);

            HandlerMethod handlerMethod = (HandlerMethod)handler;
            // 没有流量限制的直接返回
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null){
                return true;
            }
            // 获取注解定义参数
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean isLogin = accessLimit.isLogin();
            // 获取请求uri
            String key = request.getRequestURI();
            if (isLogin){
                if (user == null){
                    render(response,CodeMsg.SESSION_ERROR);
                    return false;
                }
            }else {
                // do nothing
            }
            // redis对请求url的前缀
            AccessKey accessKey = AccessKey.withExpire(seconds);
            // 获取redis中，这个值的计数值
            Integer count = redisService.get(accessKey, key, Integer.class);
            if (count == null){
                redisService.set(accessKey,key,1);
            }else if(count < maxCount) {
                redisService.set(accessKey,key,count+1);
            }else {
                render(response,CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }



    private void render(HttpServletResponse response, CodeMsg codeMsg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out= response.getOutputStream();
        String result = JSON.toJSONString(Result.error(codeMsg));
        out.write(result.getBytes("UTF-8"));
        out.flush();
        out.close();

    }


    private User getUser(HttpServletRequest request, HttpServletResponse response) {

        String paramoken = request.getParameter(UserService.TOKEN);
        String cookieToken = getCookie(request,UserService.TOKEN);

        if(StringUtils.isEmpty(paramoken) && StringUtils.isEmpty(cookieToken)){
            return null;
        }

        String token = StringUtils.isEmpty(paramoken) ? cookieToken : paramoken;
        User user = userService.getByToken(response, token);
        return user;
    }


    /**
     * 通过浏览器中Cookie的token
     * @param request
     * @param token
     * @return
     */
    private String getCookie(HttpServletRequest request, String token) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0){
            return null;
        }
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(token)){
                return cookie.getValue();
            }
        }
        return null;
    }

}
