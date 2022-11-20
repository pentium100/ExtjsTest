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
import org.apache.poi.ss.usermodel.*;
import org.hibernate.jdbc.Work;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class ExcelReportView extends AbstractXlsView {

	@Override

	protected void buildExcelDocument(Map<String, Object> model,
									  Workbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		int rowNum = 0;
		// create a wordsheet
		Cell cell;
		String fileName = URLEncoder.encode((String) model.get("title"),
				"UTF-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		response.addHeader("Content-Disposition", "attachment;filename="
				+ fileName + ".xls");
		response.setHeader("Content-Type",
				"application/force-download; charset=utf-8");

		Sheet sheet = workbook.createSheet((String) model.get("title"));

		Row header = sheet.createRow(rowNum++);

		List<ReportHeader> headers = (List<ReportHeader>) model.get("headers");

		int i = 0;
		for (ReportHeader colHeader : headers) {
			cell = header.createCell(i++);
			cell.setCellValue(colHeader.getHeader());
			CellStyle style = workbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style);

		}
		String dataRoot = (String) model.get("dataRoot");

		List<Map<String, Object>> dataSets = (List<Map<String, Object>>) model
				.get(dataRoot);

		for (Map<String, Object> dataSet : dataSets) {
			Row row = sheet.createRow(rowNum++);
			i = 0;
			for (ReportHeader colHeader : headers) {

				cell = row.createCell(i++);

				if (dataSet.get(colHeader.getField().toString()) != null) {

					String value;

					if (java.util.Date.class.isInstance(dataSet.get(colHeader
							.getField().toString()))) {
						value = sdf.format(dataSet.get(colHeader.getField()
								.toString()));
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue(value);
					} else {
						value = dataSet.get(colHeader.getField().toString())
								.toString();
						// cell.setCellType(Cell.CELL_TYPE_STRING);
						if (colHeader.getFormat() != null) {
							// DecimalFormat formatter = new
							// DecimalFormat(colHeader.getFormat());
							double d = Double.valueOf(value);
							cell.setCellValue(d);
							// value = formatter.format(Double.valueOf(value));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							// value = String.format(, value);

						} else {
							cell.setCellValue(value);
							cell.setCellType(Cell.CELL_TYPE_STRING);
						}

					}

				}

				cell.setCellStyle(colHeader.getStyle(workbook));

			}

		}

	}



}
