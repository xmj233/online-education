package org.hncj.eduservice.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hncj.commonutil.R;
import org.hncj.eduservice.client.VodClient;
import org.hncj.eduservice.entity.EduVideo;
import org.hncj.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-12
 */
@Api(description="小节管理")
@RestController
@RequestMapping("/eduservice/eduvideo")
@CrossOrigin
public class EduVideoController {
    @Autowired
    EduVideoService videoService;
    @Autowired
    VodClient vodClient;

    @ApiOperation(value = "添加小节")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "根据id删除小节")
    @DeleteMapping("delVideo/{id}")
    public R delVideo(@PathVariable String id) {
        // 删除视频
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        if ( videoSourceId!=null ) {
            vodClient.delVideo(videoSourceId);
        }
        // 删除小节
        videoService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据id查询小节")
    @GetMapping("getVideoById/{id}")
    public R getVideoById(@PathVariable String id){
        EduVideo eduVideo = videoService.getById(id);
        return R.ok().data("eduVideo", eduVideo);
    }

    @ApiOperation(value = "修改小节")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        videoService.updateById(eduVideo);
        return R.ok();
    }
}

