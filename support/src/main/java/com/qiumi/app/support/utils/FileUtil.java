package com.qiumi.app.support.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.appframe.utils.logger.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DecimalFormat;

/**
 * 文件工具类
 * 
 * 
 */
public class FileUtil {
	private static String TAG = "FileUtil";

	/**
	 * 获取目录名称
	 * 
	 * @param url
	 * @return FileName
	 */
	public static String getFileName(String url) {
		int lastIndexStart = url.lastIndexOf("/");
		if (lastIndexStart != -1) {
			return url.substring(lastIndexStart + 1, url.length());
		} else {
			return new Timestamp(System.currentTimeMillis()).toString();
		}
	}

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return boolean
	 */
	public static boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getDownloadCachePath(Context context) {
		String path = getPhoneMemoryPath(context) + "/tfari/download";
		createNewDir(path);
		return path;
	}

	public static String getDbPath(Context context) {
		String path = getPhoneMemoryPath(context) + "/tfari/database" + File.separator;
		createNewDir(path);
		return path;
	}

	public static String getMediaCachePath(Context context) {
		String path = getPhoneMemoryPath(context) + "/tfari/cache";
		createNewDir(path);
		return path;
	}
	
	public static String getLogCachePath(Context context) {
		String path = getPhoneMemoryPath(context) + "/tfari/logcache";
		createNewDir(path);
		return path;
	}
	
	public static String getImageCachePath(Context context) {
		String path = getPhoneMemoryPath(context) + "/tfari/imagecache";
		createNewDir(path);
		return path;
	}
	
	public static void createNewDir(String dir) {
		if (!checkSDCard()) {
			return;
		}
		if (null == dir) {
			return;
		}
		File f = new File(dir);
		if (!f.exists()) {
			String[] pathSeg = dir.split(File.separator);
			String path = "";
			for (String temp : pathSeg) {
				if (TextUtils.isEmpty(temp)) {
					path += File.separator;
					continue;
				} else {
					path += temp + File.separator;
				}
				File tempPath = new File(path);
				if (tempPath.exists() && !tempPath.isDirectory()) {
					tempPath.delete();
				}
				tempPath.mkdirs();
			}
		} else {
			if (!f.isDirectory()) {
				f.delete();
				f.mkdirs();
			}
		}
	}

	/**
	 * 获取手机可用的存储路径， SD可用且大于5M
	 * 
	 */
	public static String getPhoneMemoryPath(Context c) {
		String sdStatus = Environment.getExternalStorageState();
		boolean sdCardExist = sdStatus.equals(Environment.MEDIA_MOUNTED);

		if (TextUtils.isEmpty(sdStatus)) {
			return c.getFilesDir().getAbsolutePath();
		}

		if (!sdCardExist) {
			return c.getFilesDir().getAbsolutePath();
		}

		try {
			long sdcardSpace = 0;
			try {
				sdcardSpace = getSDcardAvailableSpace();
			} catch (Exception e) {
				Log.d(TAG, "error1:" + e.getMessage());
			}
			if (sdcardSpace >= 5) {
				return getSDCardPath(c);
			}

			long phoneSpace = getDataStorageAvailableSpace();
			if (phoneSpace >= 5) {
				return c.getFilesDir().getAbsolutePath();
			}
			Log.d(TAG, String.format("get storage space, phone: %d, sdcard: %d", (int) (phoneSpace / 1024 / 1024), (int) (sdcardSpace / 1024 / 1024)));
		} catch (Exception e) {
			Log.d(TAG, "error3:" + e.getMessage());
		}

		return c.getFilesDir().getAbsolutePath();
	}

	/**
	 * 获取手机内部可用空间大小
	 * 
	 * @return
	 */
	public static long getDataStorageAvailableSpace() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获取手机内置SD卡可用空间大小
	 * 
	 */
	public static long getSDcardAvailableSpace() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File path = Environment.getExternalStorageDirectory();
			if (path == null) {
				return 0;
			}
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize; // "Byte"
		} else {
			return 0;
		}
	}

	/**
	 * 获取手机内置SD卡路径
	 * 
	 */
	public static String getSDCardPath(Context c) {
		File sdDir = null;
		String sdStatus = Environment.getExternalStorageState();
		boolean sdCardExist = sdStatus.equals(Environment.MEDIA_MOUNTED);

		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
			return sdDir.toString();
		}
		return "";
	}

	/**
	 * 获取文件的大小
	 * 
	 * @param fileSize
	 *            文件的大小
	 * @return
	 */
	public static String FormetFileSize(int fileSize) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static boolean isValidAttach(String path, boolean inspectSize) {
		if (!isExistsFile(path) || getFileSize(path) == 0) {
			Log.d(TAG, "isValidAttach: file is not exist, or size is 0");
			return false;
		}
		if (inspectSize && getFileSize(path) > 2 * 1024 * 1024) {
			Log.d(TAG, "file size is too large");
			return false;
		}
		return true;
	}

	public static boolean isExistsFile(String filepath) {
		try {
			if (TextUtils.isEmpty(filepath)) {
				return false;
			}
			File file = new File(filepath);
			return file.exists();
		} catch (Exception e) {
			// e.printStackTrace();
			Log.d(TAG, "the file is not exists file path is: " + filepath);
			return false;
		}
	}
	
    public static byte[] readFile(String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        int length = fis.available();
        byte[] buffer = new byte[length];
        fis.read(buffer);
        fis.close();
        fis = null;
        file = null;
        return buffer;
    }
    
    public static byte[] readFile(String path, long offset) {
        try {
            RandomAccessFile raf = new RandomAccessFile(new File(path), "r");
            if (offset < 0) {
                offset = 0;
            }
            raf.seek(offset);
            byte[] buf = new byte[(int) (raf.length() - offset)];
            raf.read(buf);
            raf.close();
            return buf;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static void saveBitmap(String path, Bitmap bitmap){
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			Log.i(TAG, "已经保存");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public static void write(String path, byte[] bytes) {
        try {
            File file;
            FileOutputStream out;
            // file = new File(path);
            // if (!file.exists()) {
            // file.mkdirs();
            // }
            file = null;
            String dir = path.substring(0, path.lastIndexOf("/"));
            file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String name = path.substring(path.lastIndexOf("/") + 1);
            file = new File(dir, name);
            file.createNewFile();
            out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
            out.close();
            out = null;
            file = null;
        } catch (IOException e) {
            Logger.getLogger().e(e.toString());
        }
    }
    
	public static int getFileSize(String filepath) {
		try {
			if (TextUtils.isEmpty(filepath)) {
				return -1;
			}
			File file = new File(filepath);
			return (int) file.length();
		} catch (Exception e) {
			return -1;
		}
	}

	public static void copy(String src, String dest) {
		if (TextUtils.isEmpty(src) || TextUtils.isEmpty(dest)) {
			return;
		}
		InputStream is = null;
		OutputStream os = null;
		File out = new File(dest);
		if (!out.getParentFile().exists()) {
			out.getParentFile().mkdirs();
		}
		try {
			is = new BufferedInputStream(new FileInputStream(src));
			os = new BufferedOutputStream(new FileOutputStream(dest));
			byte[] b = new byte[256];
			int len = 0;
			try {
				while ((len = is.read(b)) != -1) {
					os.write(b, 0, len);
				}
				os.flush();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
					}
				}
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}
	}
	public static void copy(InputStream src, String dest) {
		if(src == null){
			Logger.getLogger().e("filecopy inputstream null!"+dest);
			return;
		}
		InputStream is = null;
		OutputStream os = null;
		File out = new File(dest);
		if (!out.getParentFile().exists()) {
			out.getParentFile().mkdirs();
		}
		try {
			is = src;
			os = new BufferedOutputStream(new FileOutputStream(dest));
			byte[] b = new byte[256];
			int len = 0;
			try {
				while ((len = is.read(b)) != -1) {
					os.write(b, 0, len);
				}
				os.flush();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
					}
				}
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}
	}
    /**   
     * 删除指定目录下文件及目录    
     * @param deleteThisPath   
     * @return
     */     
    public static void deleteFolderAndFiles(String filePath, boolean deleteThisPath) {     
        if (!TextUtils.isEmpty(filePath)) {     
            try {  
                File file = new File(filePath);     
                if (file.isDirectory()) {// 处理目录     
                    File files[] = file.listFiles();     
                    for (int i = 0; i < files.length; i++) {     
                    	deleteFolderAndFiles(files[i].getAbsolutePath(), true);     
                    }      
                }     
                if (deleteThisPath) {     
                    if (!file.isDirectory()) {// 如果是文件，删除     
                        file.delete();     
                    } else {// 目录     
                   if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除     
                            file.delete();     
                        }     
                    }     
                }  
            } catch (Exception e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }     
        }     
    }  
    
	/**
	 * 获取文件md5值
	 * 
	 * @param in
	 * @return
	 */
	public static String getFileMD5(InputStream in) {
		// 也可以传入path
		// try {
		// new FileInputStream("filepath");
		// } catch (FileNotFoundException e1) {
		// e1.printStackTrace();
		// }
		MessageDigest digest = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}


	/**
	 * 得到资源文件中图片的Uri
	 * @param context 上下文对象
	 * @param id 资源id
	 * @return Uri
	 */
	public static String getUriFromDrawableRes(Context context, int id) {
		Resources resources = context.getResources();
		return  ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
				+ resources.getResourcePackageName(id) + "/"
				+ resources.getResourceTypeName(id) + "/"
				+ resources.getResourceEntryName(id);

	}

}
