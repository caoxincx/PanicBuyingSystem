package it.caoxin.redis.key;

import it.caoxin.redis.BasePrefix;

public class PbsKey extends BasePrefix{
    public PbsKey(String prefix) {
        super(prefix);
    }

    public static PbsKey notMoreGoods = new PbsKey("notMoreGoods");
}
