package com.itg.extjstest.util;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;


public class FilterItem {

	private String type;
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private String field;
	private String comparison;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getComparison() {

		if (getType().equals("list")) {
			return "in";
		}
		if (getType().equals("boolean")) {
			return "=";
		}

		if (getType().equals("string")
				&& (getValue() == null || (getValue().equals("null")))) {
			return "is";
		}

		if (getType().equals("string") && comparison==null || comparison.equals("")) {
			return "like";
		}
		

		if (getType().equals("string") && comparison!=null && comparison.equals("not")) {
			return "not like";
		}

		if (comparison.equals("gt") || comparison.equals("greaterThan")) {
			return ">";
		}
		if (comparison.equals("lt") || comparison.equals("lessThan")) {
			return "<";
		}

		if (comparison.equals("ge") || comparison.equals("greaterOrEqual")) {
			return ">=";
		}
		if (comparison.equals("le") || comparison.equals("lessOrEqual")) {
			return "<=";
		}

		if (comparison.equals("eq") || comparison.equals("equal")) {
			return "=";
		}
		if (comparison.equals("notEqual")) {
			return "!=";
		}

		return comparison;

	}

	public void setComparison(String comparison) {
		this.comparison = comparison;
	}

	public String getSqlValues() {

		StringBuffer result = new StringBuffer();
		if (getType().equals("date")) {

			try {

				if (!getValue().equals("") && !getValue().equals("null")
						&& !getValue().equals("not null")) {
					SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
					Date today = s.parse(getValue());

					result.append("'"
							+ new SimpleDateFormat("yyyy-MM-dd").format(today)
							+ "'");
				} else {
					result.append(getValue());
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (getType().equals("datetime")) {

			result.append("'" + getValue() + "'");

		}

		if (getType().equals("string")) {

			if (getValue() == null || getValue().equals("null")) {

				result.append(" null ");
			} else {

				result.append("'%" + getValue() + "%'");
			}
		}

		if (getType().equals("list")) {
			result.append("(");
			result.append(getValue());
			result.append(")");

		}

		if (getType().equals("numeric") || getType().equals("int")) {
			result.append(getValue());

		}

		if (getType().equals("boolean")) {
			result.append(getValue().equals("true") ? 1 : 0);

		}

		return result.toString();
	}

	private Path getParentPath(Path path, String fieldName) {

		String[] fields = fieldName.split("\\.");
		Path path2 = path;

		for (int i = 0; i < fields.length - 1; i++) {
			path2 = path2.get(fields[i]);
		}

		return path2;
	}

	private Path getPathByFieldName(HashMap<String, Path> paths) {

		String[] fields = getField().split("\\.");
		Path path = null;
		String fieldName;
		if (fields.length >= 2) {
			path = paths.get("");
			path = getParentPath(path, getField());

			fieldName = fields[fields.length - 1];

		} else {

			path = paths.get("");
			fieldName = getField();

		}

		if (fields.length >= 2) {
			try {
				path.get(fieldName);
			} catch (IllegalArgumentException e) {

				path = paths.get(fields[0]);
				fieldName = fields[1];

			}
		}
		return path;
	}

	public Predicate getPredicate(CriteriaBuilder cb,
			HashMap<String, Path> paths) throws ParseException {

		Path path = getPathByFieldName(paths);
		String[] fields = getField().split("\\.");

		String fieldName = fields[fields.length - 1];
		if (type.equals("list")) {
			return path.get(fieldName).in(getStringListValue());
			// return path.get(fieldName).in(getIntegerListValue());
		}

		if (type.equals("date")) {

			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			if (getComparison().equals("<")) {
				return cb.lessThan(path.get(fieldName).as(Date.class),
						sdf.parse(getValue()));
			}

			if (getComparison().equals(">")) {
				return cb.greaterThan(path.get(fieldName).as(Date.class),
						sdf.parse(getValue()));
			}

			if (getComparison().equals("=")) {
				return cb.equal(path.get(fieldName).as(Date.class),
						sdf.parse(getValue()));
			}

		}

		if (type.equals("sList")) {

			return path.get(fieldName).in(getIntegerListValue());
		}

		if (type.equals("boolean")) {
			return cb.equal(path.get(fieldName).as(Boolean.class), getValue()
					.equals("true"));
		}

		if (type.equals("string")) {

			return cb.like(path.get(fieldName).as(String.class), "%"
					+ getValue() + "%");
		}
		if (type.equals("int") || type.equals("numeric")) {
			if (getComparison().equals("=")) {

				return cb.equal(path.get(fieldName).as(Integer.class),
						Integer.valueOf(getValue()));

			}

		}

		return null;
	}

	private List<String> getStringListValue() {
		// TODO Auto-generated method stub
		ArrayList<String> result = new ArrayList<String>();

		String[] values = value.split(",");
		for (String s : values) {
			result.add(String.valueOf(s));
		}

		return result;

	}

	private List<Integer> getIntegerListValue() {
		ArrayList<Integer> result = new ArrayList<Integer>();

		String[] values = value.split(",");
		for (String s : values) {
			result.add(Integer.valueOf(s));
		}

		return result;
	}

	public String getSqlWhere() {

		StringBuffer result = new StringBuffer();

		result.append(getField());
		result.append(" ");
		result.append(getComparison());
		result.append(" ");
		result.append(getSqlValues());
		result.append(" ");
		return result.toString();

	}

}
