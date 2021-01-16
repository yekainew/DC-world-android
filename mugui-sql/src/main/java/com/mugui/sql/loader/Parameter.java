package com.mugui.sql.loader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mugui.base.Mugui;

public  class Parameter implements Mugui, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1319033781783457512L;
	List<Object> parameter = new ArrayList<Object>();

	@SuppressWarnings("unchecked")
	void addParameter(Object object) {
		if (object instanceof List)
			parameter.addAll((List<Object>) object);
		else {
			parameter.add(object); 
		}
	}

	public List<Object> getParameter() {
		return parameter;
	}

	public Object[] getParameterArray() {
		return parameter.toArray();
	}

	protected StringBuilder sql = null;

	@Override
	public String toString() {
		return sql.toString();
	}
}
