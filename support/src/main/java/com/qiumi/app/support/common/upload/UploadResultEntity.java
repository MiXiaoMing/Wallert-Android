package com.qiumi.app.support.common.upload;

import java.io.Serializable;

/**
 * 上传文件返回值基类
 */

public class UploadResultEntity implements Serializable{
    public String status, message, timestamp;
    public FileResult data;

    public class FileResult {
        public String fileCode;
    }
}
