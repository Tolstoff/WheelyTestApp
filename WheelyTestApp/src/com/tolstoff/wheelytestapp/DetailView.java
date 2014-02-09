package com.tolstoff.wheelytestapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailView extends Activity {

	TextView txtTitle, txtText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		initView();
		fillView();

	}

	public void initView() {
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtText = (TextView) findViewById(R.id.txtText);
		ActionBar actionBar = getActionBar();
//		actionBar.setHomeButtonEnabled(true);
	
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public void fillView() {
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		String text = intent.getStringExtra("text");
		txtTitle.setText(title);
		txtText.setText(text);
		setTitle(title);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
	    onBackPressed();
	    return true;
	}

}
