package com.tek.test.util;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class Csv {



    /**
     * 写入csv（追加）
     * @param head
     * @param dataList
     * @param csvFilePath
     * @param append
     */
    public static void writeDataListToCsv(String head, List<String> dataList, String csvFilePath, boolean append) {
        File csvFile = new File(csvFilePath);
        if(!csvFile.exists()){
            try {
                csvFile.createNewFile();
            }catch (IOException e){

            }
        }
        // 将流写在try里面，当try执行完之后，流会自动关闭
        try (FileOutputStream fileOutputStream = new FileOutputStream(csvFilePath, append);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "GBK");
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
            if (StringUtils.isNotBlank(head)) {
                bufferedWriter.write(head);
                bufferedWriter.newLine();
            }
            // 将数据写入到文件中
            for (String rowStr : dataList) {
                if (StringUtils.isNotBlank(rowStr)) {
                    bufferedWriter.write(rowStr);
                    bufferedWriter.newLine();
                }
            }
            //把缓存中的数据输出到CSV文件
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *  读取数据
     * @param csvFilePath
     * @return
     */
    public static List<String> readFromCsv(String csvFilePath) {
        List<String> dataList = new ArrayList<>();
        File csvFile = new File(csvFilePath);
        // 如果文件不存在，直接返回
        if (!csvFile.exists()) {
            return dataList;
        }
        // 将流写在try里面，当try执行完之后，流会自动关闭
        try (InputStream inputStream = Files.newInputStream(csvFile.toPath());
             InputStreamReader fileReader = new InputStreamReader(inputStream, "GBK");
             BufferedReader buffReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = buffReader.readLine()) != null) {
                if (StringUtils.isNotBlank(line)) {
                    dataList.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

}

