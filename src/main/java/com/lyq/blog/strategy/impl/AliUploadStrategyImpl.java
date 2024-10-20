package com.lyq.blog.strategy.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.lyq.blog.config.AliConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * 阿里云上传策略
 *

 */
@Service("aliUploadStrategyImpl")
public class AliUploadStrategyImpl extends AbstractUploadStrategyImpl {
    @Autowired
    private AliConfigProperties aliConfigProperties;

    @Override
    public Boolean exists(String filePath) {
        return getOssClient().doesObjectExist(aliConfigProperties.getBucketName(), filePath);
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        getOssClient().putObject(aliConfigProperties.getBucketName(), path + fileName, inputStream);
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return aliConfigProperties.getUrl() + filePath;
    }

    /**
     * 获取ossClient
     *
     * @return {@link OSS} ossClient
     */
    private OSS getOssClient() {
        return new OSSClientBuilder().build(aliConfigProperties.getEndpoint(), aliConfigProperties.getAccessKeyId(), aliConfigProperties.getAccessKeySecret());
    }

}
