package org.hncj.orderservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.hncj.commonutil.JwtUtils;
import org.hncj.commonutil.R;
import org.hncj.orderservice.entity.TOrder;
import org.hncj.orderservice.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-21
 */
@Api(description = "订单管理")
@RestController
@RequestMapping("/orderservice/order")
@CrossOrigin
public class TOrderController {
    @Autowired
    TOrderService orderService;

    @ApiOperation(value = "根据课程id、用户id创建订单")
    @PostMapping("createOrder/{courseId}")
    public R createOrder(
            @PathVariable String courseId,
            HttpServletRequest httpServletRequest) {
        String memberId = JwtUtils.getMemberIdByJwtToken(httpServletRequest);
        String orderNo = orderService.createOrder(courseId, memberId);
        return R.ok().data("orderNo", orderNo);
    }

    @ApiOperation(value = "根据订单编号查询订单信息")
    @GetMapping("getOrderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo){
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        TOrder order = orderService.getOne(queryWrapper);
        return R.ok().data("order",order);
    }

    @ApiOperation(value = "根据用户id,课程id查询课程是否已购买，为service-edu提供")
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(
            @PathVariable("courseId") String courseId,
            @PathVariable("memberId") String memberId
    ) {
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        int count = orderService.count(queryWrapper);
        if ( count>0 ) {
            return true;
        } else {
            return false;
        }
    }

}

