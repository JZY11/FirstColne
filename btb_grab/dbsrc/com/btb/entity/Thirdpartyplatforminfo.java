package com.btb.entity;

import javax.persistence.Column;
import javax.persistence.Id;
/**
 * 第三方交易平台信息
 */
public class Thirdpartyplatforminfo {
	@Id
	String id;//平台id
	String name;//平台名称
	String urlicon;//平台图标地址
	String url;//平台url官网
	@Column(name="description")
	String desc;//平台描述
	String apiurl;//平台api的url地址
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrlicon() {
		return urlicon;
	}
	public void setUrlicon(String urlicon) {
		this.urlicon = urlicon;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getApiurl() {
		return apiurl;
	}
	public void setApiurl(String apiurl) {
		this.apiurl = apiurl;
	}
	
	
	
}
