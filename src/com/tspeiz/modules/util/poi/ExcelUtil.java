package com.tspeiz.modules.util.poi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tspeiz.modules.common.bean.ExcelBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;

public class ExcelUtil {
	private static Map<Integer,Integer> title_groups =null;

	private static String[] items ={"会诊时间","是否支付","费用","订单状态","医院","科室","医生姓名",
			"专家医院","专家科室","专家姓名","姓名","性别","年龄"};

	private static XSSFWorkbook workBook;
	private static XSSFSheet sheet;

	static {
		title_groups = new HashMap<Integer,Integer>();
		title_groups.put(0, 1);
		title_groups.put(1, 4);
		title_groups.put(2, 3);
		title_groups.put(3, 3);
		title_groups.put(4, 3);
		//title_groups.put(5, 1);
	}

	public static XSSFSheet buildXSLXTitle(){
		// 声明一个工作薄
		workBook = new XSSFWorkbook();
		sheet = workBook.createSheet();
		sheet.setColumnWidth(13, 3000*3);
		//sheet.autoSizeColumn(4, true);
		//sheet.autoSizeColumn(7, true);
		try {
			workBook.setSheetName(0, "导出信息");// 工作簿名称
			XSSFFont font = workBook.createFont();
			font.setColor(XSSFFont.COLOR_NORMAL);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			XSSFCellStyle cellStyle = workBook.createCellStyle();// 创建格式
			cellStyle.setFont(font);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//表头分组
			XSSFRow _titleRow = sheet.createRow((short) 0);
			_titleRow.setHeight((short)500);
			Integer begin=0;
			Integer end=0;
			for(int i=0;i<=title_groups.size();i++){
				Integer countCel = title_groups.get(i);
				if(countCel!=null&&countCel>0){
					end=begin+countCel-1;
					if(countCel>1){
						sheet.addMergedRegion(new CellRangeAddress(0,0,begin,end));
					}else {
						sheet.addMergedRegion(new CellRangeAddress(0,1,begin,end));
					}
					XSSFCellStyle _cellStyle = workBook.createCellStyle();// 创建格式
					_cellStyle.setFont(font);
					_cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					_cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					_cellStyle.setFillForegroundColor(gainColorShort(i));
					_cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
					XSSFCell _cell = _titleRow.createCell(begin, 0);
					_cell.setCellValue(gainTitle(i));
					
					_cell.setCellStyle(_cellStyle);
					begin=end+1;
				}
			}
			// 创建第一行标题
			XSSFRow titleRow = sheet.createRow((short) 1);// 第一行标题
			titleRow.setHeight((short)500);
			Integer length = items.length;
			for (int i = 0; i < length; i++) {// 创建第1行标题单元格
		        //sheet.autoSizeColumn(i);
				sheet.setColumnWidth(i+1, 3000);
				XSSFCell cell = titleRow.createCell(i+1, 1);
				cell.setCellStyle(cellStyle);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(items[i]);
			}
		}catch(Exception e){
			System.out.println("文件导出发生异常！异常原因：" + e.getMessage());
			e.printStackTrace();
		}
		return sheet;
	}

	public static void writeData(Collection<ExcelBean> dataSet,OutputStream out) throws Exception{
		//利用反射取出dataSet的值封装成List<List<String>>
		List<List> list1 = new ArrayList<List>();
		int orderNum=0;
		for (ExcelBean excel : dataSet) {
			if(excel!=null){
				List<String> list2 = new ArrayList<String>();
				// 得到类对象
				Class excelCla = (Class) excel.getClass();
			    Field[] fs = excelCla.getDeclaredFields();
			for (int j = 0; j < fs.length; j++) {
					Field f = fs[j];
					f.setAccessible(true); // 设置些属性是可以访问的
					Object val;
					if(null==f.get(excel)){
						 val ="";
					}else{
						 val = f.get(excel);// 得到此属性的值
					}
					String name = fs[j].getName();//取出属性名
					//支付状态进行转换
					if("payStatus".equals(name)){
						if(val instanceof Integer){
							if((Integer)val==1){
								val="已付款";
							}else if((Integer)val==4){
								val="待付款";
							}else if((Integer)val==0){
								val="免费";
							}
						}
					}
						//订单状态
				if("status".equals(name)){ 
					if(val instanceof Integer){
						if((Integer)val==40){
							val="已完成";
						}else if((Integer)val==10){
							val="待接诊";
						}else if((Integer)val==20){
							val="进行中";
						}else if((Integer)val==30){
							val="已退诊";
						}else if((Integer)val==50){
							val="已取消";
						}
					}
				}
				//性别
				if("sex".equals(name)){ 
					if(val instanceof Integer){
						if((Integer)val==1){
							val="男";
						}else if((Integer)val==2){
							val="女";
						}else if((Integer)val==0){
							val="未知";
						}
					}
				}
					list2.add(val.toString());
				}
					list2.add(0, String.valueOf(orderNum+1));
					list1.add(list2);
					orderNum++;
			}
		}
				XSSFCellStyle celStyle = workBook.createCellStyle();
				// 循环dataSet，每一条对应一行
				XSSFRow row = null;
				XSSFCell cell = null;
				short index = 1;
				for (List<String> list2 : list1) {
					index++;
					row = sheet.createRow(index);
					short column = 0;
					//int orderNum = 1;
					for (String s : list2) {
						    cell = row.createCell(column);
						    //cell.setCellValue(orderNum++);
							cell.setCellStyle(celStyle);
							String textValue = s;
							XSSFRichTextString richString = new XSSFRichTextString(textValue);
							cell.setCellValue(richString);
						column++;
					}
				}
				
				try {
					workBook.write(out);
					workBook.close();
				} catch (IOException e) {
					System.out.println("文件导出发生异常！异常原因：" + e.getMessage());
					e.printStackTrace();
				}
		}
	
	public static void buildExcel(Collection<ExcelBean> dataSet,HttpServletResponse response,String title){
		
		try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ExcelUtil.buildXSLXTitle();
			ExcelUtil.writeData(dataSet,os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String((title + ".xlsx").getBytes(), "iso-8859-1"));
            response.setContentLength(content.length);
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[8192];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);

            }
            bis.close();
            bos.close();
            outputStream.flush();
            outputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
	
	private static short gainColorShort(Integer i){
		switch (i) {
		case 0:
			return IndexedColors.LIGHT_TURQUOISE.getIndex();
		case 1:
			return IndexedColors.LIME.getIndex();
		case 2:
			return IndexedColors.GOLD.getIndex();
		case 3:
			return IndexedColors.SKY_BLUE.getIndex();
		case 4:
			return IndexedColors.ORANGE.getIndex();
		case 5:
			return IndexedColors.YELLOW.getIndex();
		case 6:
			return IndexedColors.GOLD.getIndex();
		case 7:
			return IndexedColors.TURQUOISE.getIndex();
		}
		return 0;
	}
	private static String gainTitle(Integer i){
		switch (i) {
		case 0:
			return "序号";
		case 1:
			return "订单信息";
		case 2:
			return "医生端";
		case 3:
			return "专家端";
		case 4:
			return "患者端";
		default:
			return "";
		}
	}
}
