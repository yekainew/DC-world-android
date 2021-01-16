package com.mugui.sql.loader;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.JsonBeanAttr;

public class Delete extends Parameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4859970659232453933L;

	public static Delete q(JsonBean bean) {
		return new Delete(bean);
	}

	public Delete(JsonBean bean) {
		sql=new StringBuilder();
		query(bean);
	}

	private Delete query(JsonBean bean) {
		JsonBeanAttr attr = JsonBeanAttr.getAttr(bean);
		sql.append("DELETE FROM `").append(attr.getTABLE()).append("` WHERE `").append(attr.getKEY())
				.append("`=? ");
		addParameter(bean.get(attr.getKEY()));
		return this;
	}
}
