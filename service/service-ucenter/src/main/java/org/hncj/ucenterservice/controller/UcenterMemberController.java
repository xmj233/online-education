package org.hncj.ucenterservice.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hncj.commonutil.JwtUtils;
import org.hncj.commonutil.R;
import org.hncj.commonutil.vo.UcenterMemberForOrder;
import org.hncj.ucenterservice.entity.UcenterMember;
import org.hncj.ucenterservice.entity.vo.LoginVo;
import org.hncj.ucenterservice.entity.vo.RegisterVo;
import org.hncj.ucenterservice.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-17
 */
@Api(description = "用户中心")
@RestController
@RequestMapping("/ucenterservice/ucentermember")
@CrossOrigin
public class UcenterMemberController {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UcenterMemberService memberService;


    @ApiOperation(value = "用户注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){
        String token = memberService.login(loginVo);
        return R.ok().data("token",token);
    }

    @ApiOperation(value = "根据token字符串获取用户信息")
    @GetMapping("getUcenterByToken")
    public R getUcenterByToken(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember ucenterMember = memberService.getById(memberId);
        return R.ok().data("ucenterMember",ucenterMember);
    }

    @ApiOperation(value = "根据用户id获取用户信息,为service-order提供")
    @GetMapping("getUcenterById/{memberId}")
    public UcenterMemberForOrder getUcenterById(@PathVariable("memberId") String memberId) {
        UcenterMember member = memberService.getById(memberId);
        UcenterMemberForOrder ucenterMemberForOrder = new UcenterMemberForOrder();
        BeanUtils.copyProperties(member, ucenterMemberForOrder);
        return ucenterMemberForOrder;
    }

    @ApiOperation(value = "统计注册人数, 为service-statistics提供")
    @GetMapping("countRegister/{day}")
    public Integer countRegister(@PathVariable("day") String day) {
        Integer count = memberService.countRegister(day);
        return count;
    }

    @ApiOperation(value = "test")
    @GetMapping("test")
    public R test() {
        Integer co = memberService.test();
        return R.ok().data("count", co);
    }

}

