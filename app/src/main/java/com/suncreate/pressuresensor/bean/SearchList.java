package com.suncreate.pressuresensor.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 搜索实体类
 *
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class SearchList extends Entity implements ListEntity<SearchResult> {

	public final static String CATALOG_ALL = "all";
	public final static String CATALOG_TECH = "tech";
	public final static String CATALOG_GOODS = "goods";
	public final static String CATALOG_GARAGE = "garage";
	public final static String CATALOG_BUSINESS = "business";

	@XStreamAlias("pagesize")
	private int pageSize;

	@XStreamAlias("results")
	private List<SearchResult> list = new ArrayList<SearchResult>();

	public int getPageSize() {
		return pageSize;
	}

	@Override
	public List<SearchResult> getList() {
		return list;
	}

	public void setList(List<SearchResult> list) {
		this.list = list;
	}
}
