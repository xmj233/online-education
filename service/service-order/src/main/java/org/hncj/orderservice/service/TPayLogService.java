package org.hncj.orderservice.service;

import org.hncj.orderservice.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-21
 */
public interface TPayLogService extends IService<TPayLog> {

    void updateOrderStatus(String orderNo);
}
