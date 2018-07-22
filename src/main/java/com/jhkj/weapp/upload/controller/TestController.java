package com.jhkj.weapp.upload.controller;

import com.jhkj.weapp.common.entity.ApiResponse;
import com.jhkj.weapp.common.util.InstantUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * @author 呉真 Kuretru < kuretru@gmail.com >
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @GetMapping("/ping")
    public ApiResponse ping() {
        String now = InstantUtils.instantToString(Instant.now());
        return ApiResponse.success(now);
    }

}
