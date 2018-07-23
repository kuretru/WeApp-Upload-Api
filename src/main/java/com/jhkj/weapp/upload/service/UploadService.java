package com.jhkj.weapp.upload.service;

import com.jhkj.weapp.common.exception.InvalidParametersException;
import com.jhkj.weapp.common.filter.SuffixFilter;
import com.jhkj.weapp.upload.configuration.UploadConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

/**
 * @author 呉真 Kuretru < kuretru@gmail.com >
 */
@Service
public class UploadService {
    @Autowired
    private UploadConfiguration configuration;

    public String checkSuffix(String fileName) throws InvalidParametersException {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new InvalidParametersException(fileName + "：文件无后缀名！");
        }
        String suffix = fileName.substring(lastDotIndex + 1);
        boolean illegalSuffix = !getSuffixFilter().doFilter(suffix);
        if (illegalSuffix) {
            throw new InvalidParametersException(fileName + "：文件后缀名非法！");
        }
        return suffix;
    }

    public String generateFileName(String suffix) {
        StringBuilder fileName = new StringBuilder()
                .append(UUID.randomUUID().toString())
                .append(".")
                .append(suffix);
        return fileName.toString();
    }

    public String getCompletePath(String fileName) {
        StringBuilder fullName = new StringBuilder()
                .append(configuration.getParent())
                .append(configuration.getTemporary())
                .append(File.separator)
                .append(fileName);
        return fullName.toString();
    }

    @Bean
    private SuffixFilter getSuffixFilter() {
        return new SuffixFilter();
    }

}
