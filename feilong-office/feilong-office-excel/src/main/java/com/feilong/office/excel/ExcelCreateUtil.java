/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.office.excel;

import static com.feilong.core.Validator.isNullOrEmpty;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

import com.feilong.core.UncheckedIOException;
import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.lang.StringUtil;
import com.feilong.io.FileUtil;

/**
 * 生成excel文档(POI).
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 2009-5-19下午08:08:37
 * @version 1.1 2010年7月7日 12:04:48
 * @version 1.2 2014-2-10 02:11
 */
public final class ExcelCreateUtil{

    /** Don't let anyone instantiate this class. */
    private ExcelCreateUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 将数据集合转换成excel,将excel生成到文件中.
     *
     * @param columnsTitle
     *            列标题数组
     * @param dataList
     *            list
     * @param outputFileName
     *            文件名称(有路径需要加)
     * @param excelConfigEntity
     *            the excel entity
     */
    public static void create(String[] columnsTitle,List<?> dataList,String outputFileName,ExcelConfigEntity excelConfigEntity){
        Validate.notBlank(outputFileName, "outputFileName can't be blank!");

        //---------------------------------------------------------------

        createExcel(columnsTitle, dataList, FileUtil.getFileOutputStream(outputFileName), excelConfigEntity);
    }

    //---------------------------------------------------------------

    /**
     * 将数据集合转换成excel.
     *
     * @param columnsTitle
     *            列标题数组
     * @param dataList
     *            list
     * @param outputStream
     *            outputStream 输出流
     * @param excelConfigEntity
     *            excel实体配置
     */
    public static void createExcel(String[] columnsTitle,List<?> dataList,OutputStream outputStream,ExcelConfigEntity excelConfigEntity){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // 总列数
        int columnsLength = columnsTitle.length;
        setSheetStyle(sheet, columnsLength, excelConfigEntity);

        //---------------------------------------------------------------

        //生成3种样式
        excelConfigEntity.setHightLightCellStyle(CellStyleBuilder.buildHightLightHSSFCellStyle(workbook));
        excelConfigEntity.setChangeColorRowCellStyle(CellStyleBuilder.buildChangeRowColorHSSFCellStyle(workbook));
        excelConfigEntity.setChangeColorRowAndHightLightCellStyle(CellStyleBuilder.buildChangeRowColorAndHightLightHSSFCellStyle(workbook));

        // 创建excel标题行
        createExcelTitleRow(workbook, sheet, columnsTitle);
        // excel填充数据
        createExcelDataRow(sheet, dataList, columnsLength, excelConfigEntity);

        //---------------------------------------------------------------
        try{
            workbook.write(outputStream);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置 sheet 样式.
     * 
     * @param sheet
     *            the sheet
     * @param columnsLength
     *            列长度
     * @param excelConfig
     *            the excel config
     */
    private static void setSheetStyle(Sheet sheet,int columnsLength,ExcelConfigEntity excelConfig){
        // 打印网格线
        // sheet.setGridsPrinted(true);
        // 显示自动分页符 自动换行
        sheet.setAutobreaks(true);

        // 合并单元格
        // sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 5));

        //---------------------------------------------------------------

        // 是否冻结窗口
        if (excelConfig.getIsFreezePane()){
            if (columnsLength > 19){// 冻拆窗口
                sheet.createFreezePane(2, 1);
            }else{
                sheet.createFreezePane(0, 1, 0, 1);
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * 创建数据行.
     * 
     * @param sheet
     *            表
     * @param dataList
     *            the data list
     * @param columnCount
     *            列数
     * @param excelEntity
     *            the excel entity
     */
    private static void createExcelDataRow(HSSFSheet sheet,List<?> dataList,int columnCount,ExcelConfigEntity excelEntity){
        if (isNullOrEmpty(dataList)){
            return;
        }

        //---------------------------------------------------------------

        // 数据行数
        int z = dataList.size();
        // 填充excel数据
        for (int i = 0; i < z; i++){
            HSSFRow row = sheet.createRow(i + 1);
            // 设置行高
            row.setHeightInPoints(18);

            for (int j = 0; j < columnCount; j++){
                HSSFCell cell = row.createCell(j);

                // 取到集合中的每一行数据数组
                String[] objects = (String[]) dataList.get(i);

                setCellValueAndStyle(cell, i, objects[j], excelEntity);
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * 创建标题行.
     * 
     * @param workbook
     *            the workbook
     * @param sheet
     *            the sheet
     * @param columnsTitle
     *            the columns title
     */
    private static void createExcelTitleRow(HSSFWorkbook workbook,HSSFSheet sheet,String[] columnsTitle){
        HSSFRow row = sheet.createRow(0);

        // 设置行高
        row.setHeightInPoints(22);

        // 列数
        int columnCount = columnsTitle.length;

        HSSFCellStyle titleRowCellStyle = CellStyleBuilder.buildHSSFCellStyleForTitleRowCell(workbook);

        for (int j = 0; j < columnCount; j++){
            HSSFCell cell = row.createCell(j);
            // 设置样式
            cell.setCellStyle(titleRowCellStyle);

            String value = columnsTitle[j];

            setCellValue(cell, value);
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置单元格值和样式.
     * 
     * @param cell
     *            单元格
     * @param rowIndex
     *            行号
     * @param value
     *            值
     * @param excelConfigEntity
     *            实体
     */
    private static void setCellValueAndStyle(HSSFCell cell,int rowIndex,String value,ExcelConfigEntity excelConfigEntity){
        // 单元格是否含有特殊字符串
        String specialString = excelConfigEntity.getSpecialString();
        boolean isHasSpecialString = StringUtils.contains(value.toString(), specialString);

        // 是否是需要变背景颜色的行
        boolean isChangeColorRow = rowIndex % 2 != 0;

        // 两个有其一就创建cellStyle
        if (isHasSpecialString || isChangeColorRow){

            boolean isHasSpecialStringToAddStyle = excelConfigEntity.getIsHasSpecialStringToAddStyle();
            boolean isRowChangeColor = excelConfigEntity.getIsRowChangeColor();

            // 有特殊字符 且隔行
            if (isHasSpecialString && isChangeColorRow){

                // 高亮 且隔行变色
                if (isHasSpecialStringToAddStyle && isRowChangeColor){
                    cell.setCellStyle(excelConfigEntity.getChangeColorRowAndHightLightCellStyle());
                    value = StringUtil.replaceAll(value, specialString, "");
                }

                // 高亮不隔行变色
                else if (isHasSpecialStringToAddStyle && !isRowChangeColor){
                    cell.setCellStyle(excelConfigEntity.getHightLightCellStyle());
                    value = StringUtil.replaceAll(value, specialString, "");
                }

                // 不高亮 隔行变色
                else if (!isHasSpecialStringToAddStyle && isRowChangeColor){
                    cell.setCellStyle(excelConfigEntity.getChangeColorRowCellStyle());
                }
            }

            // 有特殊字符 但不是隔行
            else if (isHasSpecialString && !isChangeColorRow){
                if (isHasSpecialStringToAddStyle){
                    cell.setCellStyle(excelConfigEntity.getHightLightCellStyle());
                    value = StringUtil.replaceAll(value, specialString, "");
                }
            }

            // 隔行 没有特殊字符
            else if (isChangeColorRow && !isHasSpecialString){
                if (isRowChangeColor){
                    cell.setCellStyle(excelConfigEntity.getChangeColorRowCellStyle());
                }
            }
        }

        setCellValue(cell, value);
    }

    //---------------------------------------------------------------

    /**
     * 表格设置值.
     * 
     * @param cell
     *            表格
     * @param value
     *            值
     */
    private static void setCellValue(HSSFCell cell,Object value){
        cell.setCellValue(new HSSFRichTextString(ConvertUtil.toString(value)));
    }
}