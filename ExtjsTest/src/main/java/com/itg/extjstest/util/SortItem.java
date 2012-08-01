package com.itg.extjstest.util;

import java.util.HashMap;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public class SortItem {

	private String property;
	private String direction;

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	
	private Path getParentPath(Path path, String fieldName){
		
		String[] fields = getProperty().split("\\.");
		Path path2 = path;
		
		for(int i=0;i<fields.length-1;i++){
			path2 = path2.get(fields[i]);
		}
		
		return path2;
	}
	
	public Order buildSortQuery(CriteriaBuilder cb, HashMap<String, Path> paths) {

		String[] fields = getProperty().split("\\.");
		Path path;
		String fieldName;
		
		if (fields.length == 2) {
			path = paths.get(fields[0]);
			fieldName = fields[1];

			if (path == null) {

				path = paths.get("");
				path = path.get(fields[0]);

			}

		} else {
			path = paths.get("");
			fieldName = getProperty();
		}

		
		
		Order order = null;
		if (getDirection().equals("DESC")) {
			order = cb.desc(path.get(fieldName));
		} else {
			order = cb.asc(path.get(fieldName));
		}

		return order;
	}

}
