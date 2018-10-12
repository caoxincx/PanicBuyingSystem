package it.caoxin.service;

import it.caoxin.domain.PbsGoods;
import it.caoxin.mapper.GoodsMapper;
import it.caoxin.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    public List<GoodsVo> getAllGoods(){
        return goodsMapper.getPanicBuyingGoods();
    }

    public GoodsVo getGoodsById(long goodsId){
        return goodsMapper.getPanicBuyingGoodById(goodsId);
    }

    public void reduceStockCount(GoodsVo goodsVo){
        PbsGoods pbsGoods = new PbsGoods();
        pbsGoods.setGoodsId(goodsVo.getId());
        goodsMapper.reduceStockCount(pbsGoods);
    }
}
