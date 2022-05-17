package org.hncj.smsservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hncj.commonutil.R;
import org.hncj.smsservice.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Api(description="短信发送")
@RestController
@RequestMapping("/smsservice/sms")
@Slf4j
@CrossOrigin
public class SmsApiController {
    @Autowired
    SmsService smsService;

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("sendSmsPhone/{phone}")
    public R sendSmsPhone(@PathVariable String phone){
        log.info(phone);
        Object rPhone = redisTemplate.opsForValue().get(phone);
        if ( rPhone!=null ) {
            return R.ok();
        }
        // 生成四位验证码
        String code = new DecimalFormat("0000").format(new Random().nextInt(10000));
        log.info("验证码"+code);

        boolean isSend = smsService.sendSmsPhone(phone, code);
        if ( isSend ) {
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("test")
    public R test() {
        String code = new DecimalFormat("0000").format(new Random().nextInt(10000));
        log.info("验证码"+code);
        smsService.sendSmsPhone("13503428960", code);
        return R.ok().message("test");
    }
}
