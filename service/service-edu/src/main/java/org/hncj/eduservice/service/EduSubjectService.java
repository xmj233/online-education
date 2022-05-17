package org.hncj.eduservice.service;

import org.hncj.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hncj.eduservice.entity.vo.OneSubjectVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-11
 */
public interface EduSubjectService extends IService<EduSubject> {

    void addSubject(MultipartFile file, EduSubjectService subjectService);

    List<OneSubjectVo> getAllSubject();
}
