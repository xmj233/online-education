package org.hncj.statisticservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient("service-edu")
public interface EduClient {

    // 课程总浏览数
    @GetMapping("/eduservice/courseapi/getCourseViewNum")
    public Integer getCourseViewNum();

    // 课程总购买数
    @GetMapping("/eduservice/courseapi/getCourseBuywNum")
    public Integer getCourseBuyNum();

    // 视频总播放数
    @GetMapping("/eduservice/courseapi/getVideoPlayNum")
    public Integer getVideoPlayNum();
}
