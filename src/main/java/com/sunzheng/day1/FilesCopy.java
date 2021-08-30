package com.sunzheng.day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesCopy {
    public static void main(String[] args) {
        String source="D:\\node";
        String target="D:\\nodexxa";

        try {
            Files.walk(Paths.get(source)).forEach(path -> {
                try{

                    String targetName=path.toString().replace(source,target);
                    //判断是文件夹
                    if(Files.isDirectory(path)){
                        //创建文件夹
                        Files.createDirectories(Paths.get(targetName));
                    }else if(Files.isRegularFile(path))
                    {
                        //拷贝文件
                        Files.copy(path,Paths.get(targetName));
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
