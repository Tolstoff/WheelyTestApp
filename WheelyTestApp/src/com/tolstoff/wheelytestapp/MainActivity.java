package com.tolstoff.wheelytestapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView mainListView;
	private ArrayList<HashMap<String, String>> data;
	private Timer timer;
	private Handler uiHandler;
	private ProgressBar progressParsing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initUI();
		initTimer();

	}

	public void initUI() {

		mainListView = (ListView) findViewById(R.id.mainListView);
		mainListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// LogT.log("itemClick: position = " + position + ", id = "
				// + id);

				callDetailView((int) id);

			}

		});

		progressParsing = (ProgressBar) findViewById(R.id.progressParsing);

	}

	public void initTimer() {
		timer = new Timer();
		uiHandler = new Handler();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				uiHandler.post(new Runnable() {
					@Override
					public void run() {
						if (isNetworkAvailable()) {
							getDataFromParser();
							makeListFromData();
						} else {

							showMessNoConnection();

						}
					}
				});

			}
		}, 0L, 10L * 1000);

	}

	private boolean isNetworkAvailable() {
		boolean available = false;
		ConnectivityManager myConnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = myConnMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected())
			available = true;

		return available;
	}

	public void showMessNoConnection() {
		Toast.makeText(this, getResources().getString(R.string.no_connection),
				Toast.LENGTH_LONG).show();

	}

	public void showMessServerError() {
		Toast.makeText(this, getResources().getString(R.string.server_error),
				Toast.LENGTH_LONG).show();

	}

	public void callDetailView(int id) {
		HashMap<String, String> itemElement;
		itemElement = data.get(id);

		// LogT.log(itemElement.get("title"));
		Intent intent = new Intent(this, DetailView.class);
		intent.putExtra("title", itemElement.get("title"));
		intent.putExtra("text", itemElement.get("text"));
		startActivity(intent);

	}

	public void getDataFromParser() {
		ConnectionParser connectionParser = new ConnectionParser(
				progressParsing);
		connectionParser.execute();
		try {
			data = connectionParser.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	public void makeListFromData() {

		if (data != null) {
			String[] fromArray = { "title", "text" };
			int[] toArray = { R.id.txtID, R.id.txtTitle };

			SimpleAdapter simpleAdapter;

			simpleAdapter = new SimpleAdapter(this, data, R.layout.list_item,
					fromArray, toArray);
			mainListView.setAdapter(simpleAdapter);

		} else {
			showMessServerError();
		}
	}

	public void refreshBtnClick() {
		if (isNetworkAvailable()) {
			getDataFromParser();
			makeListFromData();
		} else {
			showMessNoConnection();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_refresh) {
			refreshBtnClick();
		}

		return super.onOptionsItemSelected(item);
	}

}
