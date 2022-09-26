package com.example.demo.utils;

import cn.afterturn.easypoi.csv.CsvImportUtil;
import cn.afterturn.easypoi.csv.entity.CsvImportParams;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CsvUtils {
    /**
     * 导入Csv模板
     * @param file 导入的文件
     * @param clazz 类型
     * @throws Exception 导入异常
     */
    public static <T> List<T> importCsv(MultipartFile file,Class<T> clazz) throws Exception {
        return importCsv(file,0,1,clazz);
    }

    /**
     * 导入Csv模板
     * @param file 导入的文件
     * @param titleRow 标题行数
     * @param headerRow 头部行数
     * @param clazz 类型
     * @throws Exception 导入异常
     */
    public static <T> List<T> importCsv(MultipartFile file,Integer titleRow,Integer headerRow,Class<T> clazz) throws Exception {
        CsvImportParams params = new CsvImportParams();
        params.setTitleRows(titleRow);
        params.setHeadRows(headerRow);
        params.setEncoding("GBK");
        return importCsv(params,file, clazz);
    }
    /**
     *
     * @param params 导入参数设置
     * @param file 导入文件
     * @param clazz 类对象
     * @throws Exception 导入异常
     */
    public static <T> List<T> importCsv(CsvImportParams params, MultipartFile file, Class<T> clazz) throws Exception {
        return CsvImportUtil.importCsv(file.getInputStream(), clazz, params);
    }
}
