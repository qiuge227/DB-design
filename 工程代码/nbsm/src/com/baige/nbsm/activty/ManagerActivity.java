package com.baige.nbsm.activty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.anjoyo.LibrarySystem.R;

public class ManagerActivity extends Activity {
	private String username, password;
	private TextView tv_welcome; // 前面的欢迎语
	private Button btn_editPd, btn_exit; // 用户管理，图书管理,修改密码，注销
	private int id;// 主键id

	private void init() {
		tv_welcome = (TextView) findViewById(R.id.tv_iUsername);
		tv_welcome.setText(" " + username);
		btn_editPd = (Button) findViewById(R.id.btn_editPd);
		btn_exit = (Button) findViewById(R.id.btn_exit);

		// 注销按钮
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 修改密码
		btn_editPd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManagerActivity.this, editPassword.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});

	}

	// 跳转到用户管理界面
	public void go_user(View v) {
		startActivity(new Intent(ManagerActivity.this, userManager.class));
	}

	// 跳转到产品管理界面
	public void go_goods(View v) {
		startActivity(new Intent(ManagerActivity.this, goodsManager.class));
	}
	
	//跳转到订购管理
	public void go_ordered(View v){
		startActivity(new Intent(ManagerActivity.this,allorderRecord.class));
	}

	// 跳转到修改密码界面
	public void go_change(View v) {
		startActivity(new Intent(ManagerActivity.this, editPassword.class));
	}

	// 注销界面
	public void go_exit(View v) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_manager);
		Intent intent = getIntent();
		// 获取到传过来的用户名和密码
		username = intent.getStringExtra("username");
		password = intent.getStringExtra("password");
		id = intent.getIntExtra("id", 0);
		init();
	}
}
