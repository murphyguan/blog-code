package com.lyq.blog.config;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云配置属性

 */
@Configuration
public class QiNiuConfig {

    @Value("${upload.qiniu.ak}")
    private String ak;

    @Value("${upload.qiniu.sk}")
    private String sk;

    private Auth auth;

    private BucketManager bucketManager;

    private UploadManager uploadManager;

    @Bean
    public Auth getAuth(){
        if(auth == null){
            auth = Auth.create(ak, sk);
        }
        return auth;
    }

    @Bean
    public BucketManager getBucketManager(){
        if(bucketManager == null){
            //构造一个带指定 Region 对象的配置类
            com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Region.autoRegion());
            bucketManager = new BucketManager(auth, cfg);
        }
        return bucketManager;
    }

    @Bean
    public UploadManager getUploadManager(){
        if(uploadManager == null){
            com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Region.autoRegion());
            cfg.resumableUploadAPIVersion = com.qiniu.storage.Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
            uploadManager = new UploadManager(cfg);
        }
        return uploadManager;
    }
}
