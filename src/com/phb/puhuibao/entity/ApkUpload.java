package com.phb.puhuibao.entity;

public class ApkUpload {
private int id;
private String apk;
private String apk_describe;
private int type;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getApk() {
	return apk;
}
public void setApk(String apk) {
	this.apk = apk;
}
public String getapk_describe() {
	return apk_describe;
}
public void setapk_describe(String apk_describe) {
	this.apk_describe = apk_describe;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public ApkUpload(int id, String apk, String apk_describe, int type) {
	super();
	this.id = id;
	this.apk = apk;
	this.apk_describe = apk_describe;
	this.type = type;
}
public ApkUpload() {
	// TODO Auto-generated constructor stub
}

}
