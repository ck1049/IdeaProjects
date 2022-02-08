package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    private static final List<String> CONTENT_TYPES
            = Arrays.asList("image/jpeg", "image/tiff", "image/gif",
            "image/jpeg", "image/png", "image/tiff", "image/x-icon",
            "image/jpeg", "image/vnd.wap.wbmp", "image/fax", "image/pnetvue",
            "image/jpeg", "image/vnd.rn-realpix");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    public String uploadImg(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        // 校验文件类型
        //String suffix = StringUtils.substringAfterLast(originalFilename, ".");
        String contentType = file.getContentType();
        if (!CONTENT_TYPES.contains(contentType)){
            LOGGER.info("文件类型不合法:{}", originalFilename);
            return null;
        }
        try {
            // 校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null){
                LOGGER.info("文件内容不合法:{}", originalFilename);
                return null;
            }
            // 保存到服务器
            //file.transferTo(new File("D:\\IdeaProjects\\hm49\\image\\" + originalFilename));
            String ext = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                  file.getInputStream(), file.getSize(),  ext, null
            );
            // 返回url，进行回显
            //return "http://image.leyou.com/" + originalFilename;
            return "http://image.leyou.com/" + storePath.getFullPath();
        } catch (IOException e) {
            LOGGER.info("服务器内部错误:{}", originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
