package com.wonders.wsjy.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class CollectionPropertyUtils {

	public static List propertyFilter(List c, CollectionPropertyFilterString filter) {
		if (c == null)
			return null;
		List result = new ArrayList();
		for (int i = 0; i < c.size(); i++) {
			String r = filter.filter(c.get(i));
			if (StringUtils.isNotEmpty(r))
				result.add(r);
		}
		return result;
	}
	
	public static List propertyFilter(List c, CollectionPropertyFilterList filter) {
		if (c == null)
			return null;
		List result = new ArrayList();
		for (int i = 0; i < c.size(); i++) {
			List r = filter.filter(c.get(i));
			if (r != null)
				result.addAll(r);
		}
		return result;
	}
	
	public static List propertyFilter(List c, CollectionPropertyFilterObject filter) {
		if (c == null)
			return null;
		List result = new ArrayList();
		for (int i = 0; i < c.size(); i++) {
			Object r = filter.filter(c.get(i));
			if (r != null)
				result.add(r);
		}
		return result;
	}
}
