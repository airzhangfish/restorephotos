package com.ikags.ikamulphoto;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.ikags.ikamulphoto.utils.MultiPhotoUtils;

public class TestMain {

	public static String basesrc_path = ""; // ͼƬ��Դ
	public static String basetarget_path = ""; // Ŀ���ַ

	// public static String basesrc_path = "C://mydocument/��Ƭ/��Ϊmate8/"; // ͼƬ��Դ
	// public static String basetarget_path = "C://Users/airzhangfish/Desktop/test/"; // Ŀ���ַ

	public static void main(String[] args) {
		System.out.println("start dowork...");
		if (basesrc_path.equals("") || basetarget_path.equals("")) {
			System.out.println("basesrc_path or basetarget_path is null");
		} else {
			getDirectory(new File(basesrc_path));
		}
	}

	public static void test1file() {
		String path = "C://Users/airzhangfish/Desktop/1222.jpg";

		long time1 = MultiPhotoUtils.getFileAttrib(path, MultiPhotoUtils.TIME_CREATION);
		long time2 = MultiPhotoUtils.getFileAttrib(path, MultiPhotoUtils.TIME_LASTACCESS);
		long time3 = MultiPhotoUtils.getFileAttrib(path, MultiPhotoUtils.TIME_LASTMODIFIED);
		long time4 = MultiPhotoUtils.getPhotoExif(path, MultiPhotoUtils.EXIF_DATETIME_ORIGINAL);

		MultiPhotoUtils.metadataExtractor(path);

		double[] haha = MultiPhotoUtils.getPhotoExifLocation(path);
		System.out.println("GPS:" + haha[0] + "," + haha[1]);
		// д�ļ�
		// FileUtil.saveFile("", "C:\\Users\\airzhangfish\\Desktop\\shunzhi.csv", sb.toString());

	}

	public static List<Long> nums = new ArrayList<Long>();

	public static void getDirectory(File file) {
		File flist[] = file.listFiles();
		if (flist == null || flist.length == 0) {

		} else {
			for (File f : flist) {
				if (f.isDirectory()) {
					// ���ｫ�г����е��ļ���
					// System.out.println("Dir==>" + f.getAbsolutePath());
					getDirectory(f);
				} else {
					// ���ｫ�г����е��ļ�

					long time1 = MultiPhotoUtils.getFileAttrib(f.getAbsolutePath(), MultiPhotoUtils.TIME_CREATION);
					long time2 = MultiPhotoUtils.getFileAttrib(f.getAbsolutePath(), MultiPhotoUtils.TIME_LASTACCESS);
					long time3 = MultiPhotoUtils.getFileAttrib(f.getAbsolutePath(), MultiPhotoUtils.TIME_LASTMODIFIED);
					long time4 = MultiPhotoUtils.getPhotoExif(f.getAbsolutePath(), MultiPhotoUtils.EXIF_DATETIME_ORIGINAL);
					long filesize = MultiPhotoUtils.getFileAttrib(f.getAbsolutePath(), MultiPhotoUtils.TYPE_SIZE);
					double[] location = MultiPhotoUtils.getPhotoExifLocation(f.getAbsolutePath());

					nums.clear();
					nums.add(time1);
					nums.add(time2);
					nums.add(time3);
					if (time4 > 0) {
						nums.add(time4);
					}
					// ������Сֵ
					long min = Collections.min(nums);
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(min);

					String savepath = basetarget_path + (cal.get(Calendar.YEAR)) + "/" + (cal.get(Calendar.MONTH) + 1) + "��/";
					if (location[0] > 0) { // �е�������
						String city = MultiPhotoUtils.getCityNamebyLocation(location);
						if (city.length() > 0) {
							savepath = savepath + city + "/";
						}
					}
					System.out.println("filepath=" + savepath + f.getName() + "   from   " + f.getAbsolutePath());
					File pathdir = new File(savepath);
					pathdir.mkdirs();
					File copytofile = new File(savepath + f.getName());

					try {
						if (!copytofile.exists()) {
							MultiPhotoUtils.forTransfer(f,copytofile);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}
		}
	}

}