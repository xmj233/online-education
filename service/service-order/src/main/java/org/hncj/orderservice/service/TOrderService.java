package org.hncj.orderservice.service;

import org.hncj.orderservice.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-21
 */
public interface TOrderService extends IService<TOrder> {

    String createOrder(String courseId, String memberId);
}
