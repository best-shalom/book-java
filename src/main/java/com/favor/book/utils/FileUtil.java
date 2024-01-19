package com.favor.book.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileUtil {
    // 设置上传文件存储路径,路径设置为绝对路径https://blog.csdn.net/daniel7443/article/details/51620308
    String filePath="F:\\book";
    // 设置时间格式
    SimpleDateFormat sdf=new SimpleDateFormat("/yyyy/MM/dd/");

    /**
     * 单个文件上传
     * @param file 文件实体
     * @return 返回文件名等一系列信息用于存入数据库
     */
    public Map<String,String> addOneFile(MultipartFile file){
        // 设置真实文件路径为基础+时间文件夹
        String path=filePath+sdf.format(new Date());
        // 查找文件夹是否存在
        File folder =new File(path);
        // 如果文件夹不存在则创建文件夹
        if(!folder.exists()){
            boolean create=folder.mkdirs();
            if(!create){
                return null;
            }
        }
        // 获取文件后缀+文件名
        String oldName=file.getOriginalFilename();
        // String newName=XXX;
        try {
            // 保存文件
            file.transferTo(new File(folder,oldName));
            Map<String,String> result=new HashMap<>();
            result.put("path",path);
            result.put("name",oldName);
            return result;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
