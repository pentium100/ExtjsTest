package com.itg.extjstest.util;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;

public class ReportHeader {

	private String header;
	private String format = null;
	private short align;
	private String field;
	private int position;
	private CellStyle style;

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

	public CellStyle getStyle(Workbook workbook) {

		if (style == null) {

			style = workbook.createCellStyle();
			style.setAlignment(align);
			// style.setDataFormat(style.)
			if (this.getFormat() != null) {
				DataFormat format = workbook.createDataFormat();
				style.setDataFormat(format.getFormat(this.format));
			}
			// style.setDataFormat(org.apache.poi.hssf.usermodel.HSSFDataFormat.getBuiltinFormat("yyyy-mm-dd"));

		}
		return style;
	}

}
