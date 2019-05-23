package com.example.feng.version1.Util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.feng.version1.bean.Equipment;
import com.example.feng.version1.bean.ErrorEquipment;
import com.example.feng.version1.bean.SiteTaskEquipment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtil {
    private static WritableFont arial14font = null;

    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";

    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            arial12format = new WritableCellFormat(arial12font);
            arial12format.setAlignment(Alignment.CENTRE);
            arial12format.setVerticalAlignment(VerticalAlignment.CENTRE);

            arial10format.setAlignment(Alignment.CENTRE);
            arial10format.setVerticalAlignment(VerticalAlignment.CENTRE);

            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public static void initExcel(String fileName,String name, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(fileName,name);
            if (!file.exists()) {
                file.createNewFile();
            }
            workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("设备数据总表", 0);
            sheet.addCell( new Label(0, 0, fileName, arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            sheet.setRowView(0, 340);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void writeObjListToExcel(List<T> objList, String fileName,String name, Context c,int a) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                in = new FileInputStream(new File(fileName,name));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName,name), workbook);
                WritableSheet sheet = writebook.getSheet(0);

                if (a == 0){
                    for (int j = 0; j < objList.size(); j++) {
                        Equipment meter = (Equipment) objList.get(j);
                        List<String> list = new ArrayList<>();

                        list.add(meter.getName());
                        list.add(meter.getMeterName());
                        list.add(meter.getTabNum());
                        list.add(meter.getTime());
                        list.add(meter.getUserName());

                        for (int i = 0; i < list.size(); i++) {
                            sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                            if (list.get(i).length() <= 4) {
                                sheet.setColumnView(i, list.get(i).length() + 8);
                            } else {
                                sheet.setColumnView(i, list.get(i).length() + 5);
                            }
                        }
                        sheet.setRowView(j + 1, 350);
                    }
                    writebook.write();
                    Toast.makeText(c, "所有数据导出Excel成功", Toast.LENGTH_SHORT).show();
                }
                if (a == 1){
                    for (int j = 0; j < objList.size(); j++) {
                        ErrorEquipment meter = (ErrorEquipment) objList.get(j);
                        List<String> list = new ArrayList<>();

                        list.add(meter.getSite());
                        list.add(meter.getDevice());
                        list.add(meter.getTabId());
                        list.add(meter.getTabNum());
                        list.add(meter.getTime());
                        list.add(meter.getUserName());

                        for (int i = 0; i < list.size(); i++) {
                            sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                            if (list.get(i).length() <= 4) {
                                sheet.setColumnView(i, list.get(i).length() + 8);
                            } else {
                                sheet.setColumnView(i, list.get(i).length() + 5);
                            }
                        }
                        sheet.setRowView(j + 1, 350);
                    }
                    writebook.write();
                    Toast.makeText(c, "异常数据导出Excel成功", Toast.LENGTH_SHORT).show();
                }
                if (a == 2){
                    for (int j = 0; j < objList.size(); j++) {
                        SiteTaskEquipment meter = (SiteTaskEquipment) objList.get(j);
                        List<String> list = new ArrayList<>();

                        list.add(meter.getDeviceName());
                        list.add(meter.getMeterName());
                        list.add(meter.getData());
                        list.add(meter.getTime());
                        list.add(meter.getInPerson());

                        for (int i = 0; i < list.size(); i++) {
                            sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                            if (list.get(i).length() <= 4) {
                                sheet.setColumnView(i, list.get(i).length() + 8);
                            } else {
                                sheet.setColumnView(i, list.get(i).length() + 5);
                            }
                        }
                        sheet.setRowView(j + 1, 350);
                    }
                    writebook.write();
                    Toast.makeText(c, "按站点任务导出Excel成功", Toast.LENGTH_SHORT).show();
                }
                if (a == 3){
                    for (int j = 0; j < objList.size(); j++) {
                        Equipment meter = (Equipment) objList.get(j);
                        List<String> list = new ArrayList<>();

                        list.add(meter.getSite());
                        list.add(meter.gettabId());
                        list.add(meter.getTabNum());
                        list.add(meter.getTime());
                        list.add(meter.getUserName());

                        for (int i = 0; i < list.size(); i++) {
                            sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                            if (list.get(i).length() <= 4) {
                                sheet.setColumnView(i, list.get(i).length() + 8);
                            } else {
                                sheet.setColumnView(i, list.get(i).length() + 5);
                            }
                        }
                        sheet.setRowView(j + 1, 350);
                    }
                    writebook.write();
                    Toast.makeText(c, "按设备导出Excel成功", Toast.LENGTH_SHORT).show();
                }



            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}

