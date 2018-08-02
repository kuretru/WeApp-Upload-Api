package com.jhkj.weapp.upload.service;

import com.jhkj.weapp.common.entity.transfer.UploadDTO;
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
import java.util.Map;
import java.util.UUID;

/**
 * @author 呉真 Kuretru < kuretru@gmail.com >
 */
@Service
public class UploadService {
    @Autowired
    private UploadConfiguration configuration;

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

    public boolean verifyDTO(UploadDTO uploadDTO) {
        Map<String, String> secrets = configuration.getSecrets();
        if (secrets.containsKey(uploadDTO.getCaller())) {
            return secrets.get(uploadDTO.getCaller()).equals(uploadDTO.getSecret());
        }
        return false;
    }

    public List<String> confirm(UploadDTO uploadDTO) throws InvalidParametersException {
        String temporaryPath = configuration.getParent() + configuration.getTemporary();
        String formalPath = configuration.getParent() + uploadDTO.getCaller();
        if (!new File(formalPath).exists()) {
            throw new InvalidParametersException("文件夹不存在。");
        }
        String monthlyPath = getMonthlyPath();
        formalPath = formalPath + monthlyPath;
        monthlyPath = monthlyPath.replace(File.separator, "/");
        if (!new File(formalPath).exists()) {
            new File(formalPath).mkdirs();
        }
        List<String> result = new ArrayList<>();
        for (String file : uploadDTO.getOriginalFiles()) {
            String temporaryFile = temporaryPath + File.separator + file;
            File oldFile = new File(temporaryFile);
            if (!oldFile.exists()) {
                throw new InvalidParametersException(file + "文件不存在");
            }
        }
        for (String file : uploadDTO.getOriginalFiles()) {
            String temporaryFile = temporaryPath + File.separator + file;
            File oldFile = new File(temporaryFile);
            String formalFile = getFormalFileName(file);
            oldFile.renameTo(new File(formalPath + formalFile));
            result.add(monthlyPath + formalFile);
        }
        return result;
    }

    private String getSuffix(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return null;
        }
        return fileName.substring(lastDotIndex + 1);
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
