package it.caoxin.redis.key;

import it.caoxin.redis.BasePrefix;

public class UserKey extends BasePrefix{

    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;
    private UserKey(int expireSecondes, String prefix){
        super(expireSecondes,prefix);
    }

    public static UserKey token = new UserKey(TOKEN_EXPIRE,"tk");
}
