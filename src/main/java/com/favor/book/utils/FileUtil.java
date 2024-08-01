package com.favor.book.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Component
public class FileUtil {
    // 设置上传文件存储路径,路径设置为绝对路径https://blog.csdn.net/daniel7443/article/details/51620308
    final String filePath = "F:/book";
    // 设置时间格式
    final SimpleDateFormat sdf=new SimpleDateFormat("/yyyy/MM/dd/");

    /**
     * 单个文件上传
     * @param file 文件实体
     * @return 返回文件名等一系列信息用于存入数据库
     */
    public Map<String, String> addOneFile(MultipartFile file, String newName) {
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
        String size = String.valueOf(file.getSize());
        // 新名字为空时，如果文件本身名字不存在，而且也没有重命名的新名字，设置文件存储名为当前时间,否则设置为oldName
        if (newName.isEmpty()) {
            if (oldName == null) {
                newName = String.valueOf(new Date());
            } else {
                newName = oldName;
            }
        }
        try {

            // 查找文件是否存在,如果存在则不能上传
            File oldFile = new File(path + newName);
            if (oldFile.exists()) {
                return null;
            }
            // 保存文件,实际存储位置永远对应数据库表中存入的newName
            file.transferTo(new File(folder, newName));
            Map<String,String> result=new HashMap<>();
            result.put("filePath", path);
            result.put("oldName", oldName);
            result.put("newName", newName);
            result.put("size", size);
            return result;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public Boolean downloadOneFile(String filePath, String fileName, String savePath) {
        filePath = filePath + fileName;
        // 文件存储路径，需要包含文件夹+文件名
        savePath = savePath + '\\' + fileName;
        // 读取此位置上的文件
        File file = new File(filePath);
        if (!file.exists()) {
            // 文件不存在，返回false
            return false;
        }
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath)); OutputStream outputStream = Files.newOutputStream(Paths.get(savePath))) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            // 文件下载成功
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
