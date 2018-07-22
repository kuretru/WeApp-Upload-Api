package com.jhkj.weapp.upload.controller;

import com.jhkj.weapp.common.controller.BaseController;
import com.jhkj.weapp.common.entity.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 呉真 Kuretru < kuretru@gmail.com >
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController extends BaseController {

    @PostMapping("/upload")
    public ApiResponse upload(List<MultipartFile> files) {
        List<String> result = new ArrayList();


        if (result.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ApiResponse.missingParameters("未接受到文件，表单项名称使用\"files\"。");
        }
        return ApiResponse.success(result);
    }

}
