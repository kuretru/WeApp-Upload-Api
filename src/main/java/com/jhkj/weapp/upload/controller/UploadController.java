package com.jhkj.weapp.upload.controller;

import com.jhkj.weapp.common.controller.BaseController;
import com.jhkj.weapp.common.entity.ApiResponse;
import com.jhkj.weapp.common.exception.InvalidParametersException;
import com.jhkj.weapp.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 呉真 Kuretru < kuretru@gmail.com >
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController extends BaseController {
    @Autowired
    private UploadService service;

    @PostMapping("/upload")
    public ApiResponse upload(List<MultipartFile> files) throws InvalidParametersException {
        List<String> result = new ArrayList();
        for (MultipartFile file : files) {
            //region 1.判断文件是否存在
            if (file.isEmpty()) {
                continue;
            }
            //endregion
            //region 2.检查文件后缀名
            String suffix = service.checkSuffix(file.getOriginalFilename());
            //endregion
            //region 3.生成文件名
            String fileName = service.generateFileName(suffix);
            String fullPath = service.getCompletePath(fileName);
            //endregion
            //region 4.写入文件
            try {
                FileOutputStream outputStream = new FileOutputStream(new File(fullPath));
                BufferedOutputStream stream = new BufferedOutputStream(outputStream);
                stream.write(file.getBytes());
                stream.close();
                result.add(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //endregion
        }

        if (result.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ApiResponse.missingParameters("未接收到文件，表单项名称使用\"files\"。");
        }
        return ApiResponse.success(result);
    }

}
