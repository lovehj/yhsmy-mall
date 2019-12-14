package com.yhsmy.util;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.yhsmy.entity.FileLib;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * fastdfs工具类
 *
 * @auth 李正义
 * @date 2019/12/1 17:00
 **/
@Component
@Slf4j
public class FastdfsClientUtil {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private FdfsWebServer fdfsWebServer;


    public FileLib upload (MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty ()) {
            return null;
        }
        FileLib fileLib = new FileLib ();
        // 获取原始文件名
        String origianlFileName = multipartFile.getOriginalFilename ();
        fileLib.setFileName (origianlFileName);
        origianlFileName = origianlFileName.substring (origianlFileName.lastIndexOf (".") + 1);

        // 文件扩展名
        String ext = origianlFileName.substring (origianlFileName.lastIndexOf (".") + 1, origianlFileName.length ());
        fileLib.setTableType (fileLib.getFileCtype (ext));
        fileLib.setContentType (multipartFile.getContentType ());

        StorePath storePath = fastFileStorageClient.
                uploadImageAndCrtThumbImage (multipartFile.getInputStream (), multipartFile.getSize (), origianlFileName, null);

        fileLib.setFilePath (storePath.getFullPath ());
        fileLib.setFileSize (multipartFile.getSize ());
        fileLib.setUrlPrefix (fdfsWebServer.getWebServerUrl ());
        return fileLib;
    }

    /**
     * 下载文件
     *
     * @param group
     * @param path
     * @return
     */
    public byte[] downLoad (String group, String path) {
        return fastFileStorageClient.downloadFile (group, path, new DownloadByteArray ());
    }

    /**
     * 上传文件
     *
     * @param path
     */
    public void delete (String path) throws Exception {
        fastFileStorageClient.deleteFile (path);
    }

    /**
     * 获取fastdfs访问的图片地址前缀
     *
     * @return 附件地址前缀
     */
    public String getFdfsWebUrlPrefix () {
        return "http://"+fdfsWebServer.getWebServerUrl ();
    }

}
