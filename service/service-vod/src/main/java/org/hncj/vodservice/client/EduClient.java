package org.hncj.vodservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-edu")
public interface EduClient {
    @GetMapping("/eduservice/courseapi/playCountIncrease/{videoId}")
    public void playCountIncrease(@PathVariable("videoId") String videoId);
}
