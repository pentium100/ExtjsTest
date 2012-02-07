package com.itg.extjstest.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
		return comparison;
	}

	public void setComparison(String comparison) {
		this.comparison = comparison;
	}

	public Predicate getPredicate(CriteriaBuilder cb,
			HashMap<String, Path> paths) {
		// TODO Auto-generated method stub
		String[] fields = getField().split("\\.");
		Path path;
		String fieldName;
		if (fields.length == 2) {
			path = paths.get(fields[0]);
			fieldName = fields[1];
		} else {
			path = paths.get("");
			fieldName = getField();
		}

		if (type.equals("list")) {
			return path.get(fieldName).in(getIntegerListValue());
		}

		if (type.equals("string")) {
			return cb.like(path.get(fieldName).as(String.class), "%"
					+ getValue() + "%");
		}
		if (type.equals("int")) {
			if (getComparison().equals("eq")) {
				
				return cb.equal(path.get(fieldName).as(Integer.class), 
						Integer.valueOf(getValue()) );

			}

		}

		return null;
	}

	private List<Integer> getIntegerListValue() {
		ArrayList<Integer> result = new ArrayList<Integer>();

		String[] values = value.split(",");
		for (String s : values) {
			result.add(Integer.valueOf(s));
		}

		return result;
	}

}
