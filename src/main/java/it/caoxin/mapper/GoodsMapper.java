package it.caoxin.mapper;

import it.caoxin.domain.PbsGoods;
import it.caoxin.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsMapper {

    /**
     * 查询全部
     * @return
     */
    @Select("SELECT g.*,pbsg.stock_count,pbsg.pb_price,pbsg.start_date,pbsg.end_date FROM goods g LEFT JOIN pbs_goods pbsg ON g.id = pbsg.goods_id")
    List<GoodsVo> getPanicBuyingGoods();

    /**
     * 通过goodsId查询goods
     * @param goodsId
     * @return
     */
    @Select("SELECT g.*,pbsg.stock_count,pbsg.pb_price,pbsg.start_date,pbsg.end_date FROM goods g LEFT JOIN pbs_goods pbsg ON g.id = pbsg.goods_id where g.id = #{goodsId}")
    GoodsVo getPanicBuyingGoodById(@Param("goodsId") long goodsId);

    /**
     * 减少数据库中库存数量
     * @param pbsGoods
     * @return
     */
    @Update("update pbs_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStockCount(PbsGoods pbsGoods);

}
