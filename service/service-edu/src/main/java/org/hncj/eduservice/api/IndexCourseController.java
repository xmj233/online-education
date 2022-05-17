package org.hncj.eduservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hncj.commonutil.JwtUtils;
import org.hncj.commonutil.R;
import org.hncj.commonutil.vo.CourseVoForOrder;
import org.hncj.eduservice.client.OrderClient;
import org.hncj.eduservice.entity.EduCourse;
import org.hncj.eduservice.entity.EduVideo;
import org.hncj.eduservice.entity.vo.ChapterVo;
import org.hncj.eduservice.entity.vo.IndexCourseDetailVo;
import org.hncj.eduservice.entity.vo.IndexCourseQueryVo;
import org.hncj.eduservice.service.EduChapterService;
import org.hncj.eduservice.service.EduCourseService;
import org.hncj.eduservice.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description = "前台课程展示")
@RestController
@RequestMapping("/eduservice/courseapi")
@CrossOrigin
@Slf4j
public class IndexCourseController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private EduVideoService videoService;
    @Autowired
    OrderClient orderClient;

    @ApiOperation(value = "带条件分页查询课程列表")
    @PostMapping("getCourseApiPageVo/{current}/{limit}")
    public R getCourseApiPageVo(@PathVariable Long current,
                                @PathVariable Long limit,
                                @RequestBody IndexCourseQueryVo courseQueryVo) {
        Page<EduCourse> page = new Page<>(current, limit);
        log.info(courseQueryVo.getSubjectId());
        log.info(courseQueryVo.getSubjectParentId());
        Map<String, Object> map = courseService.getCourseApiPageVo(page, courseQueryVo);
        return R.ok().data(map);
    }

    @ApiOperation(value = "根据课程id查询课程相关信息")
    @GetMapping("getIndexCourseInfo/{courseId}")
    public R getIndexCouseInfo(@PathVariable String courseId, HttpServletRequest httpServletRequest) {
        // 查询课程信息
        IndexCourseDetailVo courseDetailVo = courseService.getIndexCourseInfo(courseId);
        // 查询章节小节信息
        List<ChapterVo> chapterVoList = chapterService.getChapterVideoById(courseId);
        // 查询课程是否已购买
        String memberId = JwtUtils.getMemberIdByJwtToken(httpServletRequest);
        boolean isBuyCourse = orderClient.isBuyCourse(courseId, memberId);

        // 课程浏览数+1
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("id", courseId);
        EduCourse eduCourse = courseService.getOne(wrapper);
        eduCourse.setViewCount(eduCourse.getViewCount()+1);
        courseService.update(eduCourse, wrapper);

        return R.ok().data("courseDetailVo", courseDetailVo).data("chapterVoList", chapterVoList).data("isBuyCourse", isBuyCourse);
    }

    @ApiOperation(value = "根据课程id查询课程相关信息, 为service-order提供")
    @GetMapping("getIndexCourseInfoForOrder/{courseId}")
    public CourseVoForOrder getCourseInfo(@PathVariable("courseId") String courseId) {
        IndexCourseDetailVo indexCourseInfo = courseService.getIndexCourseInfo(courseId);
        CourseVoForOrder courseVoForOrder = new CourseVoForOrder();
        BeanUtils.copyProperties(indexCourseInfo, courseVoForOrder);
        return courseVoForOrder;
    }

    @ApiOperation("成功付款后销量加1, 为service-order提供")
    @GetMapping("buyCountIncrease/{courseId}")
    public void buyCountIncrease(@PathVariable("courseId") String courseId) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("id", courseId);
        EduCourse eduCourse = courseService.getOne(wrapper);
        eduCourse.setBuyCount(eduCourse.getBuyCount()+1);
        courseService.update(eduCourse, wrapper);
    }

    @ApiOperation("播放数量+1, 为service-vod提供")
    @GetMapping("playCountIncrease/{videoId}")
    public void playCountIncrease(@PathVariable("videoId") String videoId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("video_source_id", videoId);
        EduVideo eduVideo = videoService.getOne(wrapper);
        eduVideo.setPlayCount(eduVideo.getPlayCount()+1);
        videoService.update(eduVideo, wrapper);
    }

    @ApiOperation("统计课程总浏览数, 为service-statistics提供")
    @GetMapping("getCourseViewNum")
    public Integer getCourseViewNum() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.select("sum(view_count) as viewCount");
        Map<String, Object> map = courseService.getMap(wrapper);
        Integer viewCount =  Integer.valueOf(map.get("viewCount").toString());
        return viewCount;
    }

    @ApiOperation("统计课程总购买数, 为service-statistics提供")
    @GetMapping("getCourseBuywNum")
    public Integer getCourseBuyNum() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.select("sum(buy_count) as buyCount");
        Map<String, Object> map = courseService.getMap(wrapper);
        Integer buyCount = Integer.valueOf(map.get("buyCount").toString());
        return buyCount;
    }

    @ApiOperation("统计视频总播放数, 为service-statistics提供")
    @GetMapping("getVideoPlayNum")
    public Integer getVideoPlayNum() {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.select("sum(play_count) as playCount");
        Map<String, Object> map = videoService.getMap(wrapper);
        Integer playCount = Integer.valueOf(map.get("playCount").toString());
        return playCount;
    }

}
