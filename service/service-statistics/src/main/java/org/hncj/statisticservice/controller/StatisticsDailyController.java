package org.hncj.statisticservice.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hncj.commonutil.R;
import org.hncj.statisticservice.client.EduClient;
import org.hncj.statisticservice.client.UcenterClient;
import org.hncj.statisticservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-22
 */
@Api(description = "统计信息")
@RestController
@RequestMapping("/statisticservice/statisticsdaily")
@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    StatisticsDailyService dailyService;

    @ApiOperation(value = "生成统计数据")
    @PostMapping("createStaDaily/{day}")
    public R createStaDaily(@PathVariable String day){
        dailyService.createStaDaily(day);
        return R.ok();
    }

    @ApiOperation(value = "查询统计数据")
    @GetMapping("getStaDaily/{type}/{begin}/{end}")
    public R getStaDaily(@PathVariable String type,
                         @PathVariable String  begin,
                         @PathVariable String  end){
        Map<String,Object> map = dailyService.getStaDaily(type,begin,end);
        return R.ok().data(map);
    }

}

