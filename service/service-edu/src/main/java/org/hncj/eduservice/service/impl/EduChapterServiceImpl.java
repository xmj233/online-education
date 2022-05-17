package org.hncj.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hncj.eduservice.entity.EduChapter;
import org.hncj.eduservice.entity.EduVideo;
import org.hncj.eduservice.entity.vo.ChapterVo;
import org.hncj.eduservice.entity.vo.VideoVo;
import org.hncj.eduservice.mapper.EduChapterMapper;
import org.hncj.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hncj.eduservice.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-12
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    EduVideoService videoService;

    @Override
    public List<ChapterVo> getChapterVideoById(String courseId) {
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(chapterWrapper);

        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideos = videoService.list(videoWrapper);

        ArrayList<ChapterVo> chapterVos = new ArrayList<>();
        for (int i = 0; i < eduChapters.size(); i++) {
            EduChapter eduChapter = eduChapters.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);

            ArrayList<VideoVo> videoVos = new ArrayList<>();
            for (int j = 0; j < eduVideos.size(); j++) {
                EduVideo eduVideo = eduVideos.get(j);
                if (eduChapter.getId().equals(eduVideo.getChapterId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    videoVos.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVos);
            chapterVos.add(chapterVo);
        }
        return chapterVos;
    }
}
