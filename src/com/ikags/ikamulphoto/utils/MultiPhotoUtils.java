package com.ikags.ikamulphoto.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifImageDirectory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

public class MultiPhotoUtils {

	public static final int TIME_CREATION = 0;
	public static final int TIME_LASTACCESS = 1;
	public static final int TIME_LASTMODIFIED = 2;
	public static final int TYPE_SIZE = 3;
	public static final int EXIF_DATETIME_ORIGINAL = 0;
	public static Vector<LocaInfo> citylist=new Vector<LocaInfo>();
	public static String citycsvpath="locationpos.csv";  //"C://programme/eclipse_projects/adt_projects/zreadme/locationpos.csv";
	/**
	 * 获得文件的相关时间属性
	 * 
	 * @param filepath
	 * @param timetype
	 * @return
	 */
	public static long getFileAttrib(String filepath, int timetype) {
		long millitime = 0;
		try {
			Path p = Paths.get(filepath);
			BasicFileAttributes att = Files.readAttributes(p, BasicFileAttributes.class);// 获取文件的属性
			switch (timetype) {
				case TIME_CREATION :
					millitime = att.creationTime().toMillis();
				break;
				case TIME_LASTACCESS :
					millitime = att.lastAccessTime().toMillis();
				break;
				case TIME_LASTMODIFIED :
					millitime = att.lastModifiedTime().toMillis();
				break;
				case TYPE_SIZE :
					millitime = att.size();
				break;
				default :
					System.out.println("getPhotoAttrib no this timetype=" + timetype);
					millitime = 0;
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return millitime;
	}

	public static String time2date(long logtime) {
		Date date2 = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");// 24小时制
		date2.setTime(logtime);
		return simpleDateFormat.format(date2);
	}

	/**
	 * 获得照片exif信息的各种属性
	 * 
	 * @param filepath
	 * @param type
    * @return
	 */
	public static long getPhotoExif(String filepath, int type) {
		long millitime = 0;
		int rowtype = ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL;
		if (type == EXIF_DATETIME_ORIGINAL) {
			rowtype = ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL;
		}
		try {
			File file = new File(filepath);
			Metadata metadata = ImageMetadataReader.readMetadata(file);
			ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			Date date = directory.getDate(rowtype);
			millitime = date.getTime();
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		return millitime;
	}
	
	/**
	 * 查看图片所有的信息
	 * @param path
	 */
	public static void metadataExtractor(String path) {
		File jpegFile = new File(path);
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
			for (Directory directory : metadata.getDirectories()) {
				for (Tag tag : directory.getTags()) {
					System.out.println(tag);
				}
			}
		} catch (ImageProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取图片的经纬度
	 * @param filepath
	 * @return
	 */
	public static double[] getPhotoExifLocation(String filepath) {
		double[] millitime = new double[2];
		try {
			File file = new File(filepath);
			Metadata metadata = ImageMetadataReader.readMetadata(file);
			GpsDirectory directory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
			String lat = directory.getString(GpsDirectory.TAG_LATITUDE);
			String lon = directory.getString(GpsDirectory.TAG_LONGITUDE);
			//System.out.println("lat="+lat+",lon="+lon);
			millitime[0]=pointToLatlong(lat);
			millitime[1]=pointToLatlong(lon);
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		return millitime;
	}
	
	/**
	 * 手机经纬度转换
	 * @param point
	 * @return
	 */
	 private static double pointToLatlong(String point) {
		String[] location=point.split(" ");
		String[] loc0=location[0].split("/");
		String[] loc1=location[1].split("/");
		String[] loc2=location[2].split("/");
		double dou1=Double.parseDouble(loc0[0])/Double.parseDouble(loc0[1]);
		double dou2=Double.parseDouble(loc1[0])/Double.parseDouble(loc1[1]);
		double dou3=Double.parseDouble(loc2[0])/Double.parseDouble(loc2[1]);
		Double duStr = dou1 + dou2 / 60 + dou3 / 60 / 60;
		return duStr;
	}
	 
	 
	 
	 /**
	  * 获取城市地址
	  * @param point
	  * @return
	  */
	 public static String getCityNamebyLocation(double[] point) {
		 
		 if(citylist.size()==0){
			 try {
					Vector  citylocation=StringUtil.getCSVData(new FileInputStream(new File(citycsvpath)), "UTF-8");
					for(int i=0;i<citylocation.size();i++){
						Vector vec=(Vector)citylocation.get(i);
						LocaInfo info=new LocaInfo();
						info.shengname=(String)vec.get(0);
						info.cityname=(String)vec.get(1);
						info.quname=(String)vec.get(2);
						info.lot=Double.parseDouble((String)vec.get(3));
						info.lat=Double.parseDouble((String)vec.get(4));
						//info.print();
						citylist.add(info);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
		 }
		 
		 
		 String name="";
		 double min=100;
		 for(int i=0;i<citylist.size();i++){
			 LocaInfo info=citylist.get(i);
			 double res=Math.sqrt((info.lat-point[0])*(info.lat-point[0])+(info.lot-point[1])*(info.lot-point[1]));
			 if(res<min){
				 min=res;
				 name=info.cityname;
			 }
		 }
		 return name;
	 }
	 
	 
	

	/**
	 * 复制文件命令（win专用）
	 * 
	 * @param origpath
	 * @param destpath
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static boolean copyFile(String origpath, String destpath, String filename) throws Exception {
		boolean flag = false;
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		File f = new File(destpath);
		if (!f.exists()) {
			f.mkdirs();
		}
		int exitVal;
		String cmd = "cmd exe /c copy " + origpath + filename + " " + destpath;
		System.out.println("cmd=" + cmd);
		p = rt.exec(cmd);
		exitVal = p.waitFor();
		if (exitVal == 0) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}
	
	
	
	public static long forTransfer(File f1,File f2) throws Exception{
        long time=new Date().getTime();
        int length=2097152;
        FileInputStream in=new FileInputStream(f1);
        FileOutputStream out=new FileOutputStream(f2);
        FileChannel inC=in.getChannel();
        FileChannel outC=out.getChannel();
        int i=0;
        while(true){
            if(inC.position()==inC.size()){
                inC.close();
                outC.close();
                return new Date().getTime()-time;
            }
            if((inC.size()-inC.position())<20971520)
                length=(int)(inC.size()-inC.position());
            else
                length=20971520;
            inC.transferTo(inC.position(),length,outC);
            inC.position(inC.position()+length);
            i++;
        }
    }
	


}
