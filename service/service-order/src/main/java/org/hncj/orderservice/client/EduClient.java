package org.hncj.orderservice.client;

import org.hncj.commonutil.vo.CourseVoForOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-edu")
public interface EduClient {

    //根据课程id查询课程相关信息
    @GetMapping("/eduservice/courseapi/getIndexCourseInfoForOrder/{courseId}")
    public CourseVoForOrder getCourseInfo(@PathVariable("courseId") String courseId);

    //成功付款后销量加1
    @GetMapping("/eduservice/courseapi/buyCountIncrease/{courseId}")
    public void buyCountIncrease(@PathVariable("courseId") String courseId);
}
