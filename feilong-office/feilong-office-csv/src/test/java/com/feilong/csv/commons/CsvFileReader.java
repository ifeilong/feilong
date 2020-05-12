package com.feilong.csv.commons;

import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.io.FileReader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.csv.User;

/**
 * @author ashraf_sarhan
 *
 */
public class CsvFileReader{

    /** The Constant log. */
    private static final Logger   LOGGER      = LoggerFactory.getLogger(CsvFileReader.class);

    //---------------------------------------------------------------
    //CSV文件头
    private static final String[] FILE_HEADER = { "用户名", "密码", "名称", "年龄" };

    //---------------------------------------------------------------

    /**
     * @param fileName
     */
    public static void readCsvFile(String fileName){
        FileReader fileReader = null;
        CSVParser csvFileParser = null;
        //创建CSVFormat(header mapping)
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER);
        try{
            //初始化FileReader object
            fileReader = new FileReader(fileName);
            //初始化 CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            //CSV文件records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();
            // CSV
            List<User> userList = newArrayList();
            // 
            for (int i = 1; i < csvRecords.size(); i++){
                CSVRecord record = csvRecords.get(i);
                //创建用户对象填入数据
                User user = new User(record.get("用户名"), record.get("名称"), Integer.parseInt(record.get("年龄")));
                userList.add(user);
            }
            // 遍历打印
            for (User user : userList){
                LOGGER.debug(user.toString());
            }
        }catch (Exception e){

        }finally{
            IOUtils.closeQuietly(fileReader, csvFileParser);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args){
        readCsvFile("c://users.csv");
    }

}