package org.hncj.eduservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hncj.commonutil.R;
import org.hncj.eduservice.entity.EduCourse;
import org.hncj.eduservice.entity.EduTeacher;
import org.hncj.eduservice.service.EduCourseService;
import org.hncj.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description="首页显示")
@RestController
@RequestMapping("/eduservice/index")
@CrossOrigin
public class IndexController {
    @Autowired
    EduCourseService courseService;

    @Autowired
    EduTeacherService teacherService;

    @ApiOperation(value = "首页展示8条课程信息4条讲师信息")
    @GetMapping("getCourseTeacherList")
    public R getCourseTeacherList() {
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.orderByDesc("gmt_create");
        courseWrapper.last("LIMIT 8");
        List<EduCourse> courseList = courseService.list(courseWrapper);

        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByDesc("gmt_create");
        teacherWrapper.last("LIMIT 4");
        List<EduTeacher> teacherList = teacherService.list(teacherWrapper);

        return R.ok().data("teacherList", teacherList).data("courseList", courseList);
    }
}
