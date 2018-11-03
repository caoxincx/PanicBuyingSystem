package it.caoxin.redis.key;

import it.caoxin.redis.BasePrefix;

public class PbsKey extends BasePrefix{
    public PbsKey(String prefix) {
        super(prefix);
    }
    public PbsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static PbsKey notMoreGoods = new PbsKey("notMoreGoods");
    public static PbsKey getPbsPath = new PbsKey(60,"pbsPath");
    public static PbsKey getPbsVerifyCode= new PbsKey(300,"pbsVerifyCode");
}
