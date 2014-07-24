package cn.zhanglian2010.mp3.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {
	
	public static final String SD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;;

	/**
	 * 在SD卡指定目录中新建文件
	 * 
	 * @throws IOException
	 */
	public static File createFileInSDCard(String fileName, String dir)
			throws IOException {
		File file = new File(SD_ROOT + dir + File.separator + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡中创建文件夹
	 * 
	 * @param dirName
	 */
	public static File createSDDir(String dir) {
		File dirFile = new File(SD_ROOT + dir + File.separator);
		dirFile.mkdirs();
		return dirFile;
	}

	/**
	 * 文件在SD卡指定目录中是否存在
	 */
	public static boolean isFileExist(String fileName, String path) {
		File file = new File(SD_ROOT + path + File.separator + fileName);
		return file.exists();
	}

	/**
	 * 将指定输入流写到指定路径的文件中
	 */
	public static File write2SDFromInput(String path, String fileName,
			InputStream input) {

		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createFileInSDCard(fileName, path);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int temp;
			while ((temp = input.read(buffer)) != -1) {
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

}