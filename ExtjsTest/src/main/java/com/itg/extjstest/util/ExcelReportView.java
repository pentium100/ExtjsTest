package com.itg.extjstest.util;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.jws.WebParam.Mode;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelReportView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		int rowNum = 0;
		// create a wordsheet
		HSSFCell cell;
		String fileName = URLEncoder.encode((String) model.get("title"),
				"UTF-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		response.addHeader("Content-Disposition", "attachment;filename="
				+ fileName + ".xls");
		response.setHeader("Content-Type",
				"application/force-download; charset=utf-8");

		HSSFSheet sheet = workbook.createSheet((String) model.get("title"));

		HSSFRow header = sheet.createRow(rowNum++);

		List<ReportHeader> headers = (List<ReportHeader>) model.get("headers");

		int i = 0;
		for (ReportHeader colHeader : headers) {
			cell = header.createCell(i++);
			cell.setCellValue(colHeader.getHeader());
			HSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style);
			

		}
		String dataRoot = (String) model.get("dataRoot");

		List<Map<String, Object>> dataSets = (List<Map<String, Object>>) model
				.get(dataRoot);

		for (Map<String, Object> dataSet : dataSets) {
			HSSFRow row = sheet.createRow(rowNum++);
			i = 0;
			for (ReportHeader colHeader : headers) {

				cell = row.createCell(i++);
				
				if (dataSet.get(colHeader.getField().toString()) != null) {

					String value;
					

					if (java.util.Date.class.isInstance(dataSet.get(colHeader
							.getField().toString()))) {
						value = sdf.format(dataSet.get(colHeader.getField()
								.toString()));
					} else {
						value = dataSet.get(colHeader.getField().toString()).toString();
						if(colHeader.getFormat()!=null){
						    DecimalFormat formatter = new DecimalFormat(colHeader.getFormat());
						    value = formatter.format(Double.valueOf(value));
						    //value = String.format(, value);
						}
						
						
					}

					cell.setCellValue(value);

				}
				
				cell.setCellStyle(colHeader.getStyle(workbook));

			}

		}

	}

}
