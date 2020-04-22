package com.feilong.office.csv.commons;

import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.feilong.office.csv.User;

/**
 * @author ashraf
 * 
 */
public class CsvFileWriter{

    //CSV文件头
    private static final Object[] FILE_HEADER = { "用户名", "名称", "年龄" };

    //---------------------------------------------------------------

    /**
     * 写CSV文件
     * 
     * @param fileName
     * @throws IOException
     */
    public static void writeCsvFile(String fileName) throws IOException{
        //创建 CSVFormat
        CSVFormat csvFileFormat = CSVFormat.EXCEL;//.withRecordSeparator(NEW_lineSeparator())

        //初始化FileWriter
        FileWriter fileWriter = new FileWriter(fileName);

        //初始化 CSVPrinter
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFileFormat);
        //创建CSV文件头
        csvPrinter.printRecord(FILE_HEADER);

        // 用户对象放入List
        List<User> userList = newArrayList();
        userList.add(new User("zhangsan", "张三", 25));
        userList.add(new User("lisi", "李四", 23));
        userList.add(new User("wangwu", "王五", 24));
        userList.add(new User("zhaoliu", "赵六", 20));

        // 遍历List写入CSV
        for (User user : userList){
            List<String> userDataRecord = newArrayList();
            userDataRecord.add(user.getUsername());
            userDataRecord.add(user.getName());
            userDataRecord.add(String.valueOf(user.getAge()));
            csvPrinter.printRecord(userDataRecord);
        }
        fileWriter.flush();
        fileWriter.close();
        csvPrinter.close();
    }
}