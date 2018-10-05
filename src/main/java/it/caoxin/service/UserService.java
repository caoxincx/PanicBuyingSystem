package it.caoxin.service;

import it.caoxin.domain.User;
import it.caoxin.exception.GobalException;
import it.caoxin.mapper.UserMapper;
import it.caoxin.redis.RedisService;
import it.caoxin.redis.key.UserKey;
import it.caoxin.result.CodeMsg;
import it.caoxin.utils.MD5Util;
import it.caoxin.utils.UUIDUtil;
import it.caoxin.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {


    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    public User getUserById(long id){
        return userMapper.getById(id);
    }

    public int insert(User user){
        return userMapper.insert(user);
    }

    public User getByToken(HttpServletResponse response,String token){
        if (StringUtils.isEmpty(token)){
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);

        if (user != null){
            addCookie(response,token,user);
        }

        return user;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null){
            throw new GobalException(CodeMsg.SERVER_ERROR);
        }

        //判断手机号是否存在
        String phone = loginVo.getPhone();
        String passwordV = loginVo.getPassword();

        User user = getUserById(Long.parseLong(phone));
        if (user == null){
            throw new GobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        //验证密码是否正确
        String password = user.getPassword();
        String salt = user.getSalt();
        String dbPass = MD5Util.fromPassToDBPass(passwordV, salt);

        if (!dbPass.equals(password)){
            throw new GobalException(CodeMsg.PASSWORD_ERROR);
        }

        String token = UUIDUtil.uuid();
        addCookie(response,token,user);
        return true;

    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token,token,user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN,token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
