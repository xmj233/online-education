package org.hncj.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hncj.commonutil.R;
import org.hncj.eduservice.entity.EduCourse;
import org.hncj.eduservice.entity.vo.CourseInfoForm;
import org.hncj.eduservice.entity.vo.CoursePublishVo;
import org.hncj.eduservice.entity.vo.CourseQuery;
import org.hncj.eduservice.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-12
 */
@Api(description = "课程管理")
@RestController
@RequestMapping("/eduservice/educourse")
@CrossOrigin
public class EduCourseController {
    @Autowired
    EduCourseService courseService;

    @ApiOperation("添加课程信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(
            @ApiParam("课程信息")
            @RequestBody CourseInfoForm courseInfoForm) {
        String courseId = courseService.addCourseInfo(courseInfoForm);
        return R.ok().data("courseId", courseId);
    }

    @ApiOperation(value = "根据id查询课程信息")
    @GetMapping("getCourseInfoById/{id}")
    public R getCourseInfoById(@PathVariable String id) {
        CourseInfoForm courseInfoForm = courseService.getCourseInfoById(id);
        return R.ok().data("courseInfo", courseInfoForm);
    }

    @ApiOperation(value = "修改课程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoForm courseInfoForm) {
        courseService.updateCourseInfo(courseInfoForm);
        return R.ok();
    }

    @ApiOperation(value = "根据课程id查询课程发布信息")
    @GetMapping("getCoursePublishById/{id}")
    public R getCoursePublishById(@PathVariable String id) {
        CoursePublishVo coursePublishVo = courseService.getCoursePublishById(id);
        return R.ok().data("coursePublishVo", coursePublishVo);
    }

    @ApiOperation(value = "根据id发布课程")
    @PutMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        EduCourse eduCourse = courseService.getById(id);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }

    @ApiOperation(value = "查询所有课程信息")
    @GetMapping("getCourseInfo")
    public R getCourseInfo() {
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list", list);
    }

    @ApiOperation(value = "带条件、带分页查询所有课程信息")
    @PostMapping("getCourseInfo/{current}/{limit}")
    public R getCourseInfoVo(
            @PathVariable Long current,
            @PathVariable Long limit,
            @RequestBody CourseQuery courseQuery
            ) {
        String title = courseQuery.getTitle();
        String begin = courseQuery.getBegin();
        String end = courseQuery.getEnd();

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if ( !StringUtils.isEmpty(title) ) {
            wrapper.like("title", title);
        }
        if ( !StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if ( !StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        Page<EduCourse> page = new Page<>(current, limit);
        courseService.page(page, wrapper);
        List<EduCourse> courseList = page.getRecords();
        long total = page.getTotal();
        return R.ok().data("courseList", courseList).data("total", total);
    }

    @ApiOperation(value = "根据id删除课程相关信息")
    @DeleteMapping("delCourseInfo/{id}")
    public R delCourseInfo(@PathVariable String id){
        courseService.delCourseInfo(id);
        return R.ok();
    }
}

