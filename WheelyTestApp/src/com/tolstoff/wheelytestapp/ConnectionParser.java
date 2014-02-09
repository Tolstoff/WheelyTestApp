package com.tolstoff.wheelytestapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

public class ConnectionParser extends
		AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {

	private final String URL_SEVER = "http://crazy-dev.wheely.com";
	private ArrayList<HashMap<String, String>> data;

	private InputStream inputStream;
	private String inputStr = "";
	private Context context;
	private ProgressBar progressParsing;

	public ConnectionParser(ProgressBar progressParsing) {
		this.progressParsing = progressParsing;

	}

	@Override
	protected void onPreExecute() {
		progressParsing.setVisibility(View.VISIBLE);

		super.onPreExecute();
	}

	@Override
	protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
		try {
			connectServer();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	private void connectServer() throws IOException {

		try {
			URL urlServer = new URL(URL_SEVER);
			HttpURLConnection connection = (HttpURLConnection) urlServer
					.openConnection();
			connection.setReadTimeout(1000000);
			connection.setConnectTimeout(100000);
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setDoInput(true);

			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
				jsonParser();

			}

			LogT.log(HttpURLConnection.HTTP_OK);

			connection.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

	}

	private void jsonParser() {
		try {
			data = new ArrayList<HashMap<String, String>>();

			HashMap<String, String> dataItem;

			getStringFromInputStream();

			JSONArray jsonArray = new JSONArray(inputStr);

			for (int i = 0; i < jsonArray.length(); i++) {
				// LogT.log(i);
				JSONObject jItem = jsonArray.getJSONObject(i);

				dataItem = new HashMap<String, String>();
				dataItem.put("id", jItem.getString("id"));
				// LogT.log(jItem.getString("id"));
				dataItem.put("title", jItem.getString("title"));
				// LogT.log(jItem.getString("title"));
				dataItem.put("text", jItem.getString("text"));
				// LogT.log(jItem.getString("text"));
				data.add(dataItem);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
		progressParsing.setVisibility(View.INVISIBLE);
		super.onPostExecute(result);
	}

	private void getStringFromInputStream() {

		try {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			int read = 0;
			while ((read = inputStream.read()) != -1) {
				bos.write(read);
			}
			byte[] result = bos.toByteArray();
			bos.close();

			inputStr = new String(result);

			// LogT.log(inputStr);

		}

		catch (IOException e) {
			e.printStackTrace();
		}

	}

}
