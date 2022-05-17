package org.hncj.eduservice.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hncj.commonutil.R;
import org.hncj.eduservice.entity.EduCourse;
import org.hncj.eduservice.entity.EduTeacher;
import org.hncj.eduservice.service.EduCourseService;
import org.hncj.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "首页讲师显示")
@RestController
@RequestMapping("/eduservice/indexteacher")
@CrossOrigin
public class IndexTeacherController {
    @Autowired
    EduTeacherService teacherService;
    @Autowired
    EduCourseService courseService;

    @ApiOperation(value = "分页讲师列表")
    @GetMapping(value = "{current}/{limit}")
    public R getTeacherList(
            @PathVariable Long current,
            @PathVariable Long limit
    ) {
        Page<EduTeacher> page = new Page<EduTeacher>(current, limit);
        Map<String, Object> map = teacherService.getTeacherList(page);
        return R.ok().data(map);
    }

    @ApiOperation(value = "根据id获取讲师和讲师所讲的课程")
    @GetMapping(value = "{id}")
    public R getTeacherCourseById(
            @PathVariable Long id
    ){
        EduTeacher teacher = teacherService.getById(id);
        List<EduCourse> courseList = courseService.getCourseByTeacherId(teacher.getId());
        return R.ok().data("teacher", teacher).data("courseList", courseList);
    }
}
