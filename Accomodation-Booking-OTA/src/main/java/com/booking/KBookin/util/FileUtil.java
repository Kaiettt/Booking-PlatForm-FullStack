package com.booking.KBookin.util;

public class FileUtil {
    private FileUtil(){
    }
    public static String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
