package com.baige.nbsm.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.anjoyo.LibrarySystem.R;

public class UserActivity extends Activity {

	Button btn_search, btn_record, btn_cancel;
	TextView tv_welcome;
	int id; // �û�id
	String name, password; // �û���������

	private void init() {
		tv_welcome = (TextView) findViewById(R.id.tv_sWelcome);
		tv_welcome.setText(" " + name);
	}
	
	// ��������Ʒչʾ
	public void go_view(View v){
		Intent intent = new Intent(UserActivity.this, goodsView.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	// ���������������
	public void go_search(View v) {
		Intent intent = new Intent(UserActivity.this, goodsSearch.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	// ������붩������
	public void go_record(View v) {
		Intent intent = new Intent(UserActivity.this, orderRecord.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	// ����˳���¼
	public void go_exit(View v) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_main);
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		name = intent.getStringExtra("username");
		password = intent.getStringExtra("password");
		init();
	}
}
