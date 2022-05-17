package org.hncj.eduservice.mapper;

import org.hncj.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hncj.eduservice.entity.vo.CoursePublishVo;
import org.hncj.eduservice.entity.vo.IndexCourseDetailVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-12
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CoursePublishVo getCoursePublishById(String id);

    IndexCourseDetailVo getIndexCourseVo(String courseId);
}
