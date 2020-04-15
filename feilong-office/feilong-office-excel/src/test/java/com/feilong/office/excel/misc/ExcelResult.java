package com.feilong.office.excel.misc;

//package com.feilong.tools.office.excel;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.lang.reflect.InvocationTargetException;
//import java.util.List;
//
//import javax.servlet.jsp.jstl.sql.Result;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.feilong.servlet.jsp.ResultUtil;
//
///**
// * 处理Excel文档(Excel).
// *
// * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
// * @version 1.0 2009-5-19下午08:08:37
// * @version 1.1 2010年7月7日 12:04:48
// */
//public class ExcelResult{
//
//    /** The Constant LOGGER. */
//    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelResult.class);
//
//    /**
//     * 将生成的excel数据保存到物理路径中.
//     *
//     * @param result
//     *            数据集
//     * @param fileName
//     *            生成的路径
//     * @param excelConfigEntity
//     *            feiLongExcelEntity
//     */
//    public void convertResultToExcel(Result result,String fileName,ExcelConfigEntity excelConfigEntity){
//        try{
//            FileOutputStream fileOutputStream = FileUtil.getFileOutputStream(fileName);
//            convertResultToExcel(result, excelConfigEntity, fileOutputStream);
//        }catch (Exception e){
//            LOGGER.error(e.getClass().getName(), e);
//        }
//    }
//
//    /**
//     * 将生成的excel数据保存到流当中.
//     *
//     * @param result
//     *            数据集
//     * @param excelConfigEntity
//     *            feiLongExcelEntity
//     * @param outputStream
//     *            流
//     */
//    public void convertResultToExcel(Result result,ExcelConfigEntity excelConfigEntity,OutputStream outputStream)
//                    throws IOException,SecurityException,IllegalArgumentException,ClassNotFoundException,NoSuchMethodException,
//                    InstantiationException,IllegalAccessException,InvocationTargetException{
//        /**
//         * 标题数组
//         */
//        String[] columnsTitle = result.getColumnNames();
//        List<?> list = ResultUtil.convertResultToList(result, ExcelConfigEntity.class);
//
//        ExcelCreateUtil excel = new ExcelCreateUtil();
//        excel.createExcel(columnsTitle, list, excelConfigEntity, outputStream);
//    }
//}
