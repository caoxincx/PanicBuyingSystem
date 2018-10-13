package it.caoxin.redis.key;

import it.caoxin.redis.BasePrefix;

public class OrderKey extends BasePrefix{

    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey orderKey = new OrderKey("orderKey");
}
