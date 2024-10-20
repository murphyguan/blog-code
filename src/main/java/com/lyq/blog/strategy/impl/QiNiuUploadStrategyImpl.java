package com.lyq.blog.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import com.lyq.blog.util.QiNiuUtil;
import com.qiniu.cdn.CdnManager;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 七牛上传策略
 *

 */
@Service("qiniuUploadStrategyImpl")
public class QiNiuUploadStrategyImpl extends AbstractUploadStrategyImpl {

    private static final Logger log = LoggerFactory.getLogger(QiNiuUploadStrategyImpl.class);

    @Autowired
    private Auth auth;
    @Autowired
    private BucketManager bucketManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private QiNiuUtil qiNiuUtil;

    @Value("${upload.qiniu.url}")
    private String url;

    @Value("${upload.qiniu.bucket}")
    private String bucket;

    @Override
    public Boolean exists(String key) {
        boolean exists = false;
        try {
            FileInfo fileInfo = bucketManager.stat(bucket, key);
            if(fileInfo != null) {
                exists = true;
                log.info("文件key:{}", fileInfo.key);
            }
        } catch (QiniuException ex) {
            log.error("获取文件信息错误",ex);
        }
        return exists;
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        try {
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(inputStream,path + fileName,upToken,null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(),DefaultPutRet.class);
            log.info("上传成功:key:{},hash:{}",putRet.key, putRet.hash);
        } catch (QiniuException ex) {
            log.error("上传失败",ex);
        }
    }

    @Override
    public String getFileAccessUrl(String path) {
        return qiNiuUtil.addTimeStampUrl(url + path);
    }

}
