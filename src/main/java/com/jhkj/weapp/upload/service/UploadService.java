package com.jhkj.weapp.upload.service;

import com.jhkj.weapp.common.entity.UploadVO;
import com.jhkj.weapp.common.exception.InvalidParametersException;
import com.jhkj.weapp.common.filter.SuffixFilter;
import com.jhkj.weapp.common.util.StringUtils;
import com.jhkj.weapp.upload.configuration.UploadConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 呉真 Kuretru < kuretru@gmail.com >
 */
@Service
public class UploadService {
    @Autowired
    private UploadConfiguration configuration;

    private String getSuffix(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return null;
        }
        return fileName.substring(lastDotIndex + 1);
    }

    public String checkSuffix(String fileName) throws InvalidParametersException {
        String suffix = getSuffix(fileName);
        if (StringUtils.isNullOrEmpty(suffix)) {
            throw new InvalidParametersException(fileName + "：文件无后缀名！");
        }
        boolean illegalSuffix = !getSuffixFilter().doFilter(suffix);
        if (illegalSuffix) {
            throw new InvalidParametersException(fileName + "：文件后缀名非法！");
        }
        return suffix;
    }

    public String getTemporaryFileName(String suffix) {
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

    public List<String> confirm(UploadVO uploadVO) throws InvalidParametersException {
        String temporaryPath = configuration.getParent() + configuration.getTemporary();
        String formalPath = configuration.getParent() + uploadVO.getCaller();
        if (!new File(formalPath).exists()) {
            throw new InvalidParametersException("文件夹不存在。");
        }
        String monthlyPath = getMonthlyPath();
        formalPath = formalPath + monthlyPath;
        monthlyPath.replace(File.separator, "/");
        if (!new File(formalPath).exists()) {
            new File(formalPath).mkdirs();
        }
        List<String> result = new ArrayList();
        for (String file : uploadVO.getOriginalFiles()) {
            String temporaryFile = temporaryPath + File.separator + file;
            String formalFile = getFormalFileName(file);
            new File(temporaryFile).renameTo(new File(formalPath + formalFile));
            result.add(monthlyPath + formalFile);
        }
        return result;
    }

    private String getMonthlyPath() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("-yyyy-MM-");
        String result = LocalDate.now().format(formatter);
        return result.replace("-", File.separator);
    }

    private String getFormalFileName(String fileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_");
        StringBuilder result = new StringBuilder()
                .append(LocalDateTime.now().format(formatter))
                .append(fileName.substring(0, 8))
                .append(".")
                .append(getSuffix(fileName));
        return result.toString();
    }

    @Bean
    private SuffixFilter getSuffixFilter() {
        return new SuffixFilter();
    }

}
