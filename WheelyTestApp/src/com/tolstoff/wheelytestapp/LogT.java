package com.tolstoff.wheelytestapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class LogT {

	public static final String LOG = "LogT";

	public static boolean isDebug = false;

	public static void log(Object statement)

	{

		if (isDebug)
			Log.d(LOG, statement.toString());

	}

//  Не забыть вставить в Манифест	
//	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />	
	
	static public void backupDB(Context context, String dbName) {

		File data = Environment.getDataDirectory();
		File sdcard = Environment.getExternalStorageDirectory();
		String outputFilePath = "", inputFilePath = "";

		inputFilePath = "/data/" + context.getPackageName() + "/databases/"
				+ dbName;
		
		outputFilePath = "/temp/" + dbName + ".db";
		
		try {

			File inputFile = new File(data, inputFilePath);
			File outputFile = new File(sdcard, outputFilePath);
			if (!outputFile.exists())
				outputFile.createNewFile();

			InputStream input = new FileInputStream(inputFile);
			OutputStream output = new FileOutputStream(outputFile);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			output.flush();
			output.close();
			input.close();
			LogT.log("Расположение копии базы данных: " + data + outputFilePath);
		} catch (IOException e) {
			LogT.log(e);
		}
	}

}
