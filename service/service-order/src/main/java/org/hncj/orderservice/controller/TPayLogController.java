package org.hncj.orderservice.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hncj.commonutil.R;
import org.hncj.orderservice.service.TPayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-21
 */
@Api(description = "订单日志管理")
@RestController
@RequestMapping("/orderservice/paylog")
@CrossOrigin
public class TPayLogController {

    @Autowired
    TPayLogService payLogService;

    @ApiOperation(value = "支付成功")
    @GetMapping("pay/{orderNo}")
    public R pay(@PathVariable String orderNo) {
        payLogService.updateOrderStatus(orderNo);
        return R.ok().message("支付成功");
    }

}

