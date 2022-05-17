package org.hncj.orderservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hncj.orderservice.entity.TOrder;
import org.hncj.orderservice.entity.TPayLog;
import org.hncj.orderservice.mapper.TPayLogMapper;
import org.hncj.orderservice.service.TOrderService;
import org.hncj.orderservice.service.TPayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-21
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    TOrderService orderService;

    @Override
    public void updateOrderStatus(String orderNo) {
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        TOrder order = orderService.getOne(queryWrapper);
        if(order.getStatus().intValue() == 1) return;
        order.setStatus(1);
        orderService.updateById(order);

        TPayLog payLog = new TPayLog();
        payLog.setOrderNo(orderNo);
        payLog.setPayTime(new Date());
        payLog.setTotalFee(order.getTotalFee());
        payLog.setPayType(1);
        baseMapper.insert(payLog);
    }
}
