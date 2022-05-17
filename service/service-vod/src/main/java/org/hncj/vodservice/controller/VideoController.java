package org.hncj.vodservice.controller;

import com.aliyun.oss.ClientException;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hncj.baseservice.handle.OeException;
import org.hncj.commonutil.R;
import org.hncj.vodservice.client.EduClient;
import org.hncj.vodservice.util.AliyunVodSDKUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hncj.vodservice.util.AliyunVodSDKUtils.initVodClient;

@Api(description="视频管理")
@RestController
@RequestMapping("/vodservice/video")
@CrossOrigin
@Slf4j
public class VideoController {

    @Autowired
    EduClient eduClient;

    @ApiOperation(value = "上传视频")
    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0,originalFilename.lastIndexOf("."));
            UploadStreamRequest request = new UploadStreamRequest(
                    "LTAI5tRaguGKndMpNy9GPdVa",
                    "GBukk1Ab17iHiXMDfAoemdmmlPdzSp",
                    title,
                    originalFilename,
                    inputStream
            );
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            String videoId = response.getVideoId();
            log.info(videoId);
            return R.ok().data("videoId",videoId);

        } catch (IOException e) {
            e.printStackTrace();
            throw  new OeException(20001,"上传视频失败");

        }
    }

    @ApiOperation(value = "删除视频")
    @DeleteMapping("delVideo/{videoId}")
    public R delVideo(@PathVariable("videoId") String videoId){

        try {
            //*初始化客户端对象
            DefaultAcsClient client = initVodClient("LTAI5tRaguGKndMpNy9GPdVa",
                    "GBukk1Ab17iHiXMDfAoemdmmlPdzSp");
            //*创建请求对象（不同操作，类不同）
            DeleteVideoRequest request = new DeleteVideoRequest();
            //*创建响应对象（不同操作，类不同）
            //DeleteVideoResponse response = new DeleteVideoResponse();
            //*向请求中设置参数
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoId);
            //*调用客户端对象方法发送请求，拿到响应
            client.getAcsResponse(request);
            return R.ok();

        } catch (Exception e) {
            e.printStackTrace();
            throw new OeException(20001,"删除视频失败");
        }
    }

    @ApiOperation(value = "批量删除视频")
    @DeleteMapping("delVideoList")
    public R delVideoList(@RequestParam("videoIdList") List<String> videoIdList){
        try {
            //*初始化客户端对象
            DefaultAcsClient client = initVodClient("LTAI5tRaguGKndMpNy9GPdVa",
                    "GBukk1Ab17iHiXMDfAoemdmmlPdzSp");
            //*创建请求对象（不同操作，类不同）
            DeleteVideoRequest request = new DeleteVideoRequest();

            //*向请求中设置参数
            //支持传入多个视频ID，多个用逗号分隔
            //11,12,13
            String videoIds = org.apache.commons.lang.StringUtils.join(videoIdList.toArray(),",");
            request.setVideoIds(videoIds);
            //*调用客户端对象方法发送请求，拿到响应
            client.getAcsResponse(request);
            return R.ok();

        } catch (Exception e) {
            e.printStackTrace();
            throw new OeException(20001,"批量删除视频失败");
        }
    }

    @ApiOperation("根据视频id获取播放凭证")
    @GetMapping("getVideoPlayAuth/{videoId}")
    public R getVideoPlayAuth(@PathVariable String videoId) {
        DefaultAcsClient client = initVodClient("LTAI5tRaguGKndMpNy9GPdVa", "GBukk1Ab17iHiXMDfAoemdmmlPdzSp");
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            response = client.getAcsResponse(request);
            //播放凭证
            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }

        // 播放数量+1
        eduClient.playCountIncrease(videoId);

        return R.ok().data("videoPlayAuth", response.getPlayAuth());
    }
}
