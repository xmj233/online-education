package org.hncj.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hncj.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hncj.eduservice.entity.vo.CourseInfoForm;
import org.hncj.eduservice.entity.vo.CoursePublishVo;
import org.hncj.eduservice.entity.vo.IndexCourseDetailVo;
import org.hncj.eduservice.entity.vo.IndexCourseQueryVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-12
 */
public interface EduCourseService extends IService<EduCourse> {

    String addCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoById(String id);

    void updateCourseInfo(CourseInfoForm courseInfoForm);

    CoursePublishVo getCoursePublishById(String id);

    void delCourseInfo(String id);

    List<EduCourse> getCourseByTeacherId(String id);

    Map<String, Object> getCourseApiPageVo(Page<EduCourse> page, IndexCourseQueryVo courseQueryVo);

    IndexCourseDetailVo getIndexCourseInfo(String courseId);
}
