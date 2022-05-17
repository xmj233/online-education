package org.hncj.eduservice.service;

import org.hncj.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hncj.eduservice.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-12
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoById(String courseId);
}
