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

	// file copy 출처 : https://allmana.tistory.com/82
	public static String fileCopy(String imgPath, String copyPath) {
		imgPath = imgPath.replaceAll("\\\\", "/");
		copyPath = copyPath.replaceAll("\\\\", "/");
		
		// 경로가 같으면 종료
		if (imgPath.equals(copyPath) || imageName == null)	return imgPath;
		// 파일객체생성
		File oriFile = new File(imgPath);
		// 복사파일객체생성
		File copyFile = new File(copyPath);

		try {
			FileInputStream fis = new FileInputStream(oriFile); // 읽을파일
			FileOutputStream fos = new FileOutputStream(copyFile); // 복사할파일

			int fileByte = 0;
			// fis.read()가 -1 이면 파일을 다 읽은것
			while ((fileByte = fis.read()) != -1) {
				fos.write(fileByte);
			}
			// 자원사용종료
			fis.close();
			fos.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

		return copyPath;
	}

}
