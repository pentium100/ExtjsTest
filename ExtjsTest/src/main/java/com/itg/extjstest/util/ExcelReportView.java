package com.itg.extjstest.util;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.jws.WebParam.Mode;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelReportView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		int rowNum = 0;
		// create a wordsheet
		
		String fileName = URLEncoder.encode((String) model.get("title"), "UTF-8");
				
		response.addHeader("Content-Disposition", "attachment;filename="+fileName+".xls"); 
		response.setHeader("Content-Type","application/force-download; charset=utf-8");
		
		
		HSSFSheet sheet = workbook.createSheet((String) model.get("title"));

		HSSFRow header = sheet.createRow(rowNum++);

		List<ReportHeader> headers = (List<ReportHeader>) model.get("headers");

		int i = 0;
		for (ReportHeader colHeader : headers) {
			header.createCell(i++).setCellValue(colHeader.getHeader());
			
		}
		String dataRoot = (String) model.get("dataRoot");

		List<Map<String, Object>> dataSets = (List<Map<String, Object>>) model
				.get(dataRoot);

		for (Map<String, Object> dataSet : dataSets) {
			HSSFRow row = sheet.createRow(rowNum++);
			i = 0;
			for (ReportHeader colHeader : headers) {
				
				HSSFCell cell = row.createCell(i++);
				if (dataSet.get(colHeader.getField().toString()) != null) {
					cell.setCellValue(dataSet.get(colHeader.getField().toString()).toString());
				}
				cell.setCellStyle(colHeader.getStyle(workbook));
				
				

			}

		}

	}

}
