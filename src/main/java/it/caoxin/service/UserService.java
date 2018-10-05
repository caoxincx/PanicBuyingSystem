package it.caoxin.service;

import it.caoxin.domain.User;
import it.caoxin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserById(long id){
        return userMapper.getById(id);
    }

    public int insert(User user){
        return userMapper.insert(user);
    }
}
