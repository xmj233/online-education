package org.hncj.statisticservice.task;

import lombok.extern.slf4j.Slf4j;
import org.hncj.statisticservice.service.StatisticsDailyService;
import org.hncj.statisticservice.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class ScheduledTask {
    @Autowired
    StatisticsDailyService dailyService;

    @Scheduled(cron = "0 0 1 * * ? ")
    public void task2(){
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(),-1));
        dailyService.createStaDaily(day);
        log.info("数据生成成功"+new Date());
    }
}
