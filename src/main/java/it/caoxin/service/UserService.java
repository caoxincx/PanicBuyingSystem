package it.caoxin.service;

import it.caoxin.domain.User;
import it.caoxin.exception.GobalException;
import it.caoxin.mapper.UserMapper;
import it.caoxin.result.CodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserById(long id){
        if (true){
            throw new GobalException(CodeMsg.PASSWORD_EMPTY);
        }
        return userMapper.getById(id);
    }

    public int insert(User user){
        return userMapper.insert(user);
    }
}
