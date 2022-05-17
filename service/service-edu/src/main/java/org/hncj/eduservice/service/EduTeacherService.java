package org.hncj.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hncj.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-11
 */
public interface EduTeacherService extends IService<EduTeacher> {

    Map<String, Object> getTeacherList(Page<EduTeacher> page);
}
