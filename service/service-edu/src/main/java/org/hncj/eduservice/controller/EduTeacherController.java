package org.hncj.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.hncj.baseservice.handle.OeException;
import org.hncj.commonutil.R;
import org.hncj.eduservice.entity.EduTeacher;
import org.hncj.eduservice.entity.vo.TeacherQuery;
import org.hncj.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-11
 */

@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/eduteacher")
@CrossOrigin
public class EduTeacherController {
    @Autowired
    EduTeacherService teacherService;

    @ApiOperation("获取所有讲师信息")
    @GetMapping
    public R getAllTeacher() {
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("list", list);
    }

    @ApiOperation("根据id删除讲师")
    @DeleteMapping("{id}")
    public R delTeacher(
            @ApiParam("讲师id")
            @PathVariable String id) {
        boolean remove = teacherService.removeById(id);
        if (remove) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "分页查询讲师列表")
    @GetMapping("getTeacherPage/{current}/{limit}")
    public R getTeacherPage(
            @ApiParam("当前页")
            @PathVariable Long current,
            @ApiParam("每页数据条数")
            @PathVariable Long limit) {
        Page<EduTeacher> page = new Page<>(current, limit);
        teacherService.page(page, null);
        List<EduTeacher> records = page.getRecords();
        long total = page.getTotal();
        return R.ok().data("list", records).data("total", total);
    }

    @ApiOperation(value = "带条件分页查询讲师列表")
    @PostMapping("getTeacherPageVo/{current}/{limit}")
    public R getTeacherVo(
            @ApiParam("当前页")
            @PathVariable Long current,
            @ApiParam("每页数据条数")
            @PathVariable Long limit,
            @ApiParam("vo数据")
            @RequestBody TeacherQuery teacherQuery
            ) {
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if ( !StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if ( !StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if ( !StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        Page<EduTeacher> page = new Page<>(current, limit);
        teacherService.page(page, wrapper);
        List<EduTeacher> list = page.getRecords();
        Long total = page.getTotal();
        return R.ok().data("list", list).data("total", total);
    }

    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(
            @ApiParam("讲师信息")
            @RequestBody EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else{
            return R.error();
        }
    }
    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("getTeacherById/{id}")
    public R getTeacherById(
            @ApiParam("讲师id")
            @PathVariable String id){
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("eduTeacher",eduTeacher);
    }
    @ApiOperation(value = "修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(
            @ApiParam("讲师信息")
            @RequestBody EduTeacher eduTeacher){
        boolean update = teacherService.updateById(eduTeacher);
        if(update){
            return R.ok();
        }else{
            return R.error();
        }
    }
}

