package it.caoxin.redis;

public interface KeyPrefix {
    int expireSeconds();
    String getPrefix();

}
