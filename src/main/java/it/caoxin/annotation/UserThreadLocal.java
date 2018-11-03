package it.caoxin.annotation;

import it.caoxin.domain.User;

public class UserThreadLocal {
    private static ThreadLocal<User> userTheadLocal = new ThreadLocal<>();

    /**
     * ThreadLocal添加User
     * @param user
     */
    public static void setUser(User user){
        userTheadLocal.set(user);
    }

    /**
     * ThreadLocal获取User
     * @return
     */
    public static User getUser(){
        return userTheadLocal.get();
    }

    /**
     * ThreadLocal获取User
     * @return
     */
    public static void removeUser(){
       userTheadLocal.remove();
    }
}
