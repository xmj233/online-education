package org.hncj.eduservice.client;

import lombok.extern.slf4j.Slf4j;
import org.hncj.commonutil.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class VodFileDegradeFeignClient implements VodClient{
    @Override
    public R delVideo(String videoId) {
        log.info("进入熔断器");
        //可以执行一些操作，比如将删除失败的信息入库，以便于后续处理
        return R.error().message("删除失败");
    }
    @Override
    public R delVideoList(List<String> videoIdList) {
        log.info("进入熔断器");
        //可以执行一些操作，比如将删除失败的信息入库，以便于后续处理
        return R.error().message("删除失败");
    }
}
