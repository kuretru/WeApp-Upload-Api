package com.jhkj.weapp.upload.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 呉真 Kuretru < kuretru@gmail.com >
 */
@Component
@Data
@ConfigurationProperties(prefix = "com.jhkj.weapp.upload.api")
public class UploadConfiguration {

    private String parent;

    private String temporary;

    private Map<String, String> secrets;

}
