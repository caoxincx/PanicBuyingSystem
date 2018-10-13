package it.caoxin.redis.key;

import it.caoxin.redis.BasePrefix;

public class GoodKey extends BasePrefix {
    public static final int TOKEN_EXPIRE = 60;
    public GoodKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static GoodKey goodListToken = new GoodKey(TOKEN_EXPIRE,"goodList");
    public static GoodKey goodDetailToken = new GoodKey(TOKEN_EXPIRE,"goodDetail");
}
