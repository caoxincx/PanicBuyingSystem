package it.caoxin.config;

import it.caoxin.annotation.UserThreadLocal;
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

        return UserThreadLocal.getUser();
    }


}
