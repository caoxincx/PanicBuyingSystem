package it.caoxin.redis;

public abstract class BasePrefix implements KeyPrefix{
    //过期时间
    private int expireSeconds;

    //不同的缓存的开头
    private String prefix;

    public BasePrefix(String prefix) {
       this(0,prefix);
    }

    public BasePrefix(int expireSeconds,String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds(){
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }

}
