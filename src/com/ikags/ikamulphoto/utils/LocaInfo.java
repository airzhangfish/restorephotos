package com.ikags.ikamulphoto.utils;

public class LocaInfo {

	public String shengname="";
	public String cityname="";
	public String quname="";
	public double lat=0;
	public double lot=0;
	
	
	public void print(){
		System.out.println("省="+shengname+",市="+cityname+",区="+quname+",经="+lat+",纬="+lot);
	}
}
