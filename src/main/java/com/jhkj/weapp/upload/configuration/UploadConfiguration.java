package com.jhkj.weapp.upload.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 呉真 Kuretru < kuretru@gmail.com >
 */
@Component
@Data
@ConfigurationProperties(prefix = "com.jhkj.weapp.upload")
@PropertySource("classpath:application.yml")
public class UploadConfiguration {

    private String parent;

    private String temporary;

}
