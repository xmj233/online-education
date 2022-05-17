package org.hncj.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hncj.baseservice.handle.OeException;
import org.hncj.eduservice.client.VodClient;
import org.hncj.eduservice.entity.EduChapter;
import org.hncj.eduservice.entity.EduCourse;
import org.hncj.eduservice.entity.EduCourseDescription;
import org.hncj.eduservice.entity.EduVideo;
import org.hncj.eduservice.entity.vo.CourseInfoForm;
import org.hncj.eduservice.entity.vo.CoursePublishVo;
import org.hncj.eduservice.entity.vo.IndexCourseDetailVo;
import org.hncj.eduservice.entity.vo.IndexCourseQueryVo;
import org.hncj.eduservice.mapper.EduCourseMapper;
import org.hncj.eduservice.service.EduChapterService;
import org.hncj.eduservice.service.EduCourseDescriptionService;
import org.hncj.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hncj.eduservice.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-12
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    EduCourseDescriptionService courseDescriptionService;
    @Autowired
    EduVideoService videoService;
    @Autowired
    EduChapterService chapterService;
    @Autowired
    VodClient vodClient;

    @Override
    public String addCourseInfo(CourseInfoForm courseInfoForm) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm, eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if ( insert==0 ) {
            throw new OeException(20001, "创建课程失败");
        }
        String courseId = eduCourse.getId();
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(courseId);
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescriptionService.save(courseDescription);
        return courseId;
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {
        EduCourse eduCourse = baseMapper.selectById(id);
        //2封装课程信息
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(eduCourse,courseInfoForm);
        //3根据id查询课程描述信息
        EduCourseDescription courseDescription = courseDescriptionService.getById(id);
        //4封装课程描述
        courseInfoForm.setDescription(courseDescription.getDescription());
        return courseInfoForm;
    }

    @Override
    public void updateCourseInfo(CourseInfoForm courseInfoForm) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,eduCourse);
        //2更新课程数据
        int update = baseMapper.updateById(eduCourse);
        //3判断是否成功
        if(update==0){
            throw  new OeException(20001,"修改课程失败");
        }
        //4更新课程描述
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(courseInfoForm.getId());
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescriptionService.updateById(courseDescription);
    }

    @Override
    public CoursePublishVo getCoursePublishById(String id) {
        CoursePublishVo coursePublishVo = baseMapper.getCoursePublishById(id);
        return coursePublishVo;
    }

    @Override
    public void delCourseInfo(String id) {
        // 删除视频
        QueryWrapper<EduVideo> videoIdWrapper = new QueryWrapper<>();
        videoIdWrapper.eq("course_id",id);
        List<EduVideo> list = videoService.list(videoIdWrapper);
        //遍历获取视频id
        List<String> videoIdList = new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            EduVideo eduVideo = list.get(i);
            videoIdList.add(eduVideo.getVideoSourceId());
        }
        //判断，调接口
        if(videoIdList.size()>0){
            vodClient.delVideoList(videoIdList);
        }

        //删除小节
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id",id);
        videoService.remove(videoWrapper);

        //删除章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id",id);
        chapterService.remove(chapterWrapper);
        //删除课程描述
        courseDescriptionService.removeById(id);
        //删除课程
        int delete = baseMapper.deleteById(id);
        if(delete==0){
            throw  new OeException(20001,"删除课程失败");
        }
    }

    @Override
    public List<EduCourse> getCourseByTeacherId(String id) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", id);
        wrapper.orderByDesc("gmt_modified");
        List<EduCourse> courseList = baseMapper.selectList(wrapper);
        return courseList;
    }

    @Override
    public Map<String, Object> getCourseApiPageVo(Page<EduCourse> page, IndexCourseQueryVo courseQueryVo) {
        //1 取出查询条件
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String subjectId = courseQueryVo.getSubjectId();
        String buyCountSort = courseQueryVo.getBuyCountSort();
        String gmtCreateSort = courseQueryVo.getGmtCreateSort();
        String priceSort = courseQueryVo.getPriceSort();
        //2 验空，不为空拼写到查询条件
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();

        if(!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(buyCountSort)){
            queryWrapper.orderByDesc("buy_count");
        }
        if(!StringUtils.isEmpty(gmtCreateSort)){
            queryWrapper.orderByDesc("gmt_create");
        }
        if(!StringUtils.isEmpty(priceSort)){
            queryWrapper.orderByDesc("price");
        }
        queryWrapper.eq("status","Normal");

        //3 分页查询
        baseMapper.selectPage(page,queryWrapper);
        //4 封装数据
        List<EduCourse> records = page.getRecords();
        long current = page.getCurrent();
        long pages = page.getPages();
        long size = page.getSize();
        long total = page.getTotal();
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public IndexCourseDetailVo getIndexCourseInfo(String courseId) {
        IndexCourseDetailVo courseDetailVo = baseMapper.getIndexCourseVo(courseId);
        return courseDetailVo;
    }
}
