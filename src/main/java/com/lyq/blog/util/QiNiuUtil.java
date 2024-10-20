package com.lyq.blog.util;

import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class QiNiuUtil {

    private static final Logger log = LoggerFactory.getLogger(QiNiuUtil.class);

    @Autowired
    private Auth auth;

    @Value("${upload.qiniu.url}")
    private String qiniuUrl;

    /**
     * 生成资源基于CDN时间戳防盗链的访问外链
     *
     * @param url 资源原始外链
     */
    public String addTimeStampUrl(String url){
        if(!url.contains(qiniuUrl)){
            if(url.startsWith("http://") || url.startsWith("https://")){
                return url;
            }
            url = qiniuUrl + url;
        }
        // 设置过期时间为600秒
        return auth.privateDownloadUrl(url,600);
    }

    /**
     * 去除防盗链
     * @param url 资源原始外链
     */
    public String removeTimeStampUrl(String url){
        if(url.contains(qiniuUrl)){
            url = url.replaceAll("\\?.*$", "");
            url = url.replaceAll(qiniuUrl, "");
            return url;
        }
        return url;
    }

    /**
     * 增加防盗链
     * @param text 富文本内容
     */
    public String addTimeStampUrlForText(String text){
        Pattern pattern = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String newSrc = addTimeStampUrl(matcher.group(1));
            text = text.replace(matcher.group(1), newSrc);
        }
        return text;
    }

    /**
     * 去除防盗链
     * @param text 富文本内容
     */
    public String removeTimeStampUrlForText(String text){
        Pattern pattern = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String newSrc = removeTimeStampUrl(matcher.group(1));
            text = text.replace(matcher.group(1), newSrc);
        }
        return text;
    }

}
