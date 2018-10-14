package it.caoxin.mapper;

import it.caoxin.domain.OrderInfo;
import it.caoxin.domain.PbsOrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper {
    /**
     * 通过用户id和商品id查询抢购订单
     * @param userId
     * @param goodsId
     * @return
     */
    @Select("SELECT *  FROM pbs_order_info WHERE user_id = #{userId} and goods_id = #{goodsId}")
    PbsOrderInfo getPbsOrderByUserAndGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    /**
     * 插入订单信息
     * @param orderInfo
     * @return
     */
    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    /**
     * 插入抢购订单信息
     * @param pbsOrderInfo
     * @return
     */
    @Insert("insert into pbs_order_info (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    public int insertMiaoshaOrder(PbsOrderInfo pbsOrderInfo);

    /**
     * 通过orderId查询订单信息
     * @param orderId
     * @return
     */
    @Select("select * from order_info where id = #{orderId}")
    public OrderInfo getOrderById(@Param("orderId")long orderId);
}
