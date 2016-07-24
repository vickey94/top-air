package air.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vickey on 2016/7/22.
 */
public class Excel {

    private String excelPath = "E:\\air\\TOP\\0724.xlsx";


    public Excel() {


    }

    public void createExcel(String[] sheets) throws IOException {

        FileOutputStream os = new FileOutputStream(new File(this.excelPath));

        XSSFWorkbook workbook = new XSSFWorkbook();

        for (String sheet : sheets)
            workbook.createSheet(sheet);

        workbook.write(os);
        workbook.close();
        os.close();
        System.out.println("Excel表创建完成！路径为：" + this.excelPath);

    }

    public void addData(String[] sheetNames, List list) throws IOException {


        //读取已有文档
        FileInputStream is = new FileInputStream(this.excelPath);
        XSSFWorkbook workbook = new XSSFWorkbook(is);

        FileOutputStream os = new FileOutputStream(this.excelPath);

        //加载已有文档，设定参数
        SXSSFWorkbook super_wb = new SXSSFWorkbook(workbook,100);// keep 100 rows in memory, exceeding rows will be flushed to disk
        //获取指定sheet

        for(String sheetName:sheetNames) {
            System.out.println("开始导入数据！"+sheetName);
            Sheet super_Sheet = super_wb.getSheet(sheetName);
            //获取行
            int nowRow = super_Sheet.getLastRowNum() + 1;
            for (int i = 0; i < 100000; i++) {

                Row row = super_Sheet.createRow(nowRow);
                for (int num = 0; num < 30; num++) {
                    row.createCell(num).setCellValue(num);

                }
                System.out.println(i);

                nowRow++;
            }
        }

        os.flush();
        super_wb.write(os);
        super_wb.close();
        workbook.close();
        os.close();
        is.close();



        System.out.println("Excel表数据导入完成！");


    }

    public void superAddData(String[] sheetNames, List<List> lists) throws IOException {


        //读取已有文档
        FileInputStream is = new FileInputStream(this.excelPath);
        XSSFWorkbook workbook = new XSSFWorkbook(is);

        FileOutputStream os = new FileOutputStream(this.excelPath);

        //加载已有文档，设定参数
        SXSSFWorkbook super_wb = new SXSSFWorkbook(workbook,100);// keep 100 rows in memory, exceeding rows will be flushed to disk
        //获取指定sheet

        int indexOfLists = 0;
        for(String sheetName:sheetNames){
            System.out.println("开始导入数据！"+sheetName);
            Sheet super_Sheet = super_wb.getSheet(sheetName);

            //获取行
            int nowRow = super_Sheet.getLastRowNum() + 1;
            List list = lists.get(indexOfLists);
            Iterator i = list.iterator();
            while (i.hasNext()){
                String string = (String) i.next();
                String[] strings = string.split(",");
                Row row = super_Sheet.createRow(nowRow);
                int column = 0;

                for(String str: strings) {
                    row.createCell(column).setCellValue(str);
                    column++;
                }
                nowRow++;
            }
            indexOfLists++;
        }

        os.flush();
        super_wb.write(os);
        super_wb.close();
        workbook.close();
        os.close();
        is.close();



    }

    public static void main(String args[]) throws IOException {
        Excel e = new Excel();
        String[] sheets = {"a", "b", "c"};
        e.createExcel(sheets);

        List list = new ArrayList<>();
        String[] sheetNames = {"a","b"};
        e.addData(sheetNames, list);


        System.out.println("Excel表数据导入完成！");

    }

}
