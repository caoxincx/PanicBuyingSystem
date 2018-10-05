package it.caoxin.config;

import it.caoxin.domain.User;
import it.caoxin.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    UserService userService;

    /**
     * 判断Controller中是否有参数为User的参数
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazzType = methodParameter.getParameterType();
        return clazzType == User.class;
    }

    /**
     * 从redis中通过token获取对应的用户值
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

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
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(token)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
