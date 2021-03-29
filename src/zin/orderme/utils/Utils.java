package zin.orderme.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Utils {
	// path
	public final static String IMAGEPATH = "zin/orderme/image/none_img.png";
	public static String imageName = null;
	
	// name final
	public static String NONE = "-";

	// file copy ��ó : https://allmana.tistory.com/82
	public static String fileCopy(String imgPath, String copyPath) {
		imgPath = imgPath.replaceAll("\\\\", "/");
		copyPath = copyPath.replaceAll("\\\\", "/");
		
		// ��ΰ� ������ ����
		if (imgPath.equals(copyPath) || imageName == null)	return imgPath;
		// ���ϰ�ü����
		File oriFile = new File(imgPath);
		// �������ϰ�ü����
		File copyFile = new File(copyPath);

		try {
			FileInputStream fis = new FileInputStream(oriFile); // ��������
			FileOutputStream fos = new FileOutputStream(copyFile); // ����������

			int fileByte = 0;
			// fis.read()�� -1 �̸� ������ �� ������
			while ((fileByte = fis.read()) != -1) {
				fos.write(fileByte);
			}
			// �ڿ��������
			fis.close();
			fos.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

		return copyPath;
	}

}
