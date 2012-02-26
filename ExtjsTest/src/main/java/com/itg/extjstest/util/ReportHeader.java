package com.itg.extjstest.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ReportHeader {

	
	private String header;
	private String format;
	private short align;
	private String field;
	private int position;
	private HSSFCellStyle style;
	
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public short getAlign() {
		return align;
	}
	public void setAlign(short align) {
		this.align = align;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public HSSFCellStyle getStyle(HSSFWorkbook workbook) {

		if(style==null){
			
			style = workbook.createCellStyle();
			style.setAlignment(align);
		}
		return style;
	}
	
}
