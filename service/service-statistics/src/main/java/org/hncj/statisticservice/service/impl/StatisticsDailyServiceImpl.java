package org.hncj.statisticservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hncj.statisticservice.client.EduClient;
import org.hncj.statisticservice.client.UcenterClient;
import org.hncj.statisticservice.entity.StatisticsDaily;
import org.hncj.statisticservice.mapper.StatisticsDailyMapper;
import org.hncj.statisticservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-22
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    EduClient eduClient;
    @Autowired
    UcenterClient ucenterClient;

    @Override
    public void createStaDaily(String day) {
        //删除数据
        QueryWrapper<StatisticsDaily> queryWrapperdel = new QueryWrapper<>();
        queryWrapperdel.eq("date_calculated", day);
        baseMapper.delete(queryWrapperdel);

        //统计数据
        Integer registerNum = ucenterClient.countRegister(day);
        Integer courseViewNum = eduClient.getCourseViewNum();
        Integer courseBuyNum = eduClient.getCourseBuyNum();
        Integer videoPlayNum = eduClient.getVideoPlayNum();

        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setDateCalculated(day);
        statisticsDaily.setCourseViewNum(courseViewNum);
        statisticsDaily.setCourseBuyNum(courseBuyNum);
        statisticsDaily.setVideoViewNum(videoPlayNum);
        statisticsDaily.setRegisterNum(registerNum);

        baseMapper.insert(statisticsDaily);
    }

    @Override
    public Map<String, Object> getStaDaily(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        wrapper.select("date_calculated", type);
        wrapper.orderByAsc("date_calculated");
        List<StatisticsDaily> dailyList = baseMapper.selectList(wrapper);

        HashMap<String, Object> map = new HashMap<>();
        List<String> dateCalculatedList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();

        for (int i = 0; i < dailyList.size(); i++) {
            StatisticsDaily daily = dailyList.get(i);
            dateCalculatedList.add(daily.getDateCalculated());

            switch (type) {
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "course_view_num":
                    dataList.add(daily.getCourseViewNum());
                    break;
                case "course_buy_num":
                    dataList.add(daily.getCourseBuyNum());
                    break;
                case "video_view_num":
                    dataList.add(daily.getVideoViewNum());
                    break;
                default:
                    break;
            }
        }
        map.put("dateCalculatedList", dateCalculatedList);
        map.put("dataList", dataList);
        return map;
    }
}
