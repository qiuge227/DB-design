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
	private TextView tv_welcome; // ǰ��Ļ�ӭ��
	private Button btn_editPd, btn_exit; // �û�����ͼ�����,�޸����룬ע��
	private int id;// ����id

	private void init() {
		tv_welcome = (TextView) findViewById(R.id.tv_iUsername);
		tv_welcome.setText(" " + username);
		btn_editPd = (Button) findViewById(R.id.btn_editPd);
		btn_exit = (Button) findViewById(R.id.btn_exit);

		// ע����ť
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// �޸�����
		btn_editPd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManagerActivity.this, editPassword.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});

	}

	// ��ת���û��������
	public void go_user(View v) {
		startActivity(new Intent(ManagerActivity.this, userManager.class));
	}

	// ��ת����Ʒ�������
	public void go_goods(View v) {
		startActivity(new Intent(ManagerActivity.this, goodsManager.class));
	}
	
	//��ת����������
	public void go_ordered(View v){
		startActivity(new Intent(ManagerActivity.this,allorderRecord.class));
	}

	// ��ת���޸��������
	public void go_change(View v) {
		startActivity(new Intent(ManagerActivity.this, editPassword.class));
	}

	// ע������
	public void go_exit(View v) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_manager);
		Intent intent = getIntent();
		// ��ȡ�����������û���������
		username = intent.getStringExtra("username");
		password = intent.getStringExtra("password");
		id = intent.getIntExtra("id", 0);
		init();
	}
}
