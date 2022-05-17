package org.hncj.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hncj.commonutil.R;
import org.hncj.eduservice.entity.EduChapter;
import org.hncj.eduservice.entity.EduVideo;
import org.hncj.eduservice.entity.vo.ChapterVo;
import org.hncj.eduservice.service.EduChapterService;
import org.hncj.eduservice.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(description = "章节管理")
@RestController
@RequestMapping("/eduservice/educhapter")
@CrossOrigin
public class EduChapterController {
    @Autowired
    EduChapterService chapterService;

    @Autowired
    EduVideoService videoService;

    @ApiOperation(value = "根据课程id查询章节、小节信息")
    @GetMapping("getChapterVideoById/{courseId}")
    public R getChapterVideoById(
            @ApiParam("课程id")
            @PathVariable String courseId) {
        List<ChapterVo> list = chapterService.getChapterVideoById(courseId);
        return R.ok().data("chapterVideoList", list);
    }

    @ApiOperation(value = "添加章节")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter) {
        chapterService.save(eduChapter);
        return R.ok();
    }

    @ApiOperation(value = "根据id删除章节")
    @DeleteMapping("delChapter/{id}")
    public R delChapter(@PathVariable String id) {
        chapterService.removeById(id);
        //ALREADY 删除章节的同时删除小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", id);
        videoService.remove(wrapper);

        return R.ok();
    }

    @ApiOperation(value = "根据id查询章节")
    @GetMapping("getChapterById/{id}")
    public R getChapterById(@PathVariable String id){
        EduChapter eduChapter = chapterService.getById(id);
        return R.ok().data("eduChapter", eduChapter);
    }

    @ApiOperation(value = "修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter) {
        chapterService.updateById(eduChapter);
        return R.ok();
    }
}

