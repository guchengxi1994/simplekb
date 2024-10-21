package org.xiaoshuyui.simplekb.common.batchInsert;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class XlsxLoader {

    /**
     * 从XLSX输入流加载数据并映射到指定的Java类
     *
     * @param stream 输入流，包含XLSX数据
     * @param clazz  要映射的Java类，必须带有匹配Excel列名的字段
     * @param <T>    泛型参数，表示要返回的列表的类型
     * @return 成功时返回一个包含映射对象的列表，失败时返回null
     */
    public static <T> List<T> loadXlsx(InputStream stream, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        Workbook workbook = null;
        try {
            // 创建XSSFWorkbook对象来读取输入流中的XLSX数据
            workbook = new XSSFWorkbook(stream);
            Sheet sheet = workbook.getSheetAt(0);  // 假设读取第一张表

            // 获取标题行（假设第一行是标题行）
            Iterator<Row> rowIterator = sheet.rowIterator();
            Row headerRow = rowIterator.hasNext() ? rowIterator.next() : null;
            if (headerRow == null) {
                log.error("Excel文件中没有数据");
                return null;
            }

            // 将标题行映射到列索引
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            // 读取每一行数据并映射到类属性
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                T instance = clazz.getDeclaredConstructor().newInstance();  // 创建类实例
                for (Cell cell : row) {
                    int columnIndex = cell.getColumnIndex();
                    String header = headers.get(columnIndex);  // 获取列对应的标题

                    // 根据标题找到对应的类字段并设置值
                    Field field = clazz.getDeclaredField(header);
                    field.setAccessible(true);
                    Object cellValue = getCellValue(cell);  // 获取单元格的值
                    field.set(instance, cellValue);  // 将值设置到对象字段
                }
                resultList.add(instance);  // 将对象添加到结果列表
            }
        } catch (Exception e) {
            log.error("数据转化失败", e);
            return null;
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();  // 关闭工作簿
                }
            } catch (Exception e) {
                log.error("关闭工作簿时发生错误", e);
            }
        }
        return resultList;
    }


    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return null;
        }
    }
}
