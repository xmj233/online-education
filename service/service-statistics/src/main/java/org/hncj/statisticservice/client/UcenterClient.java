package org.hncj.statisticservice.client;

import org.hncj.commonutil.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {

    @GetMapping("/ucenterservice/ucentermember/countRegister/{day}")
    public Integer countRegister(@PathVariable("day") String day);
}
