package com.baige.nbsm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.anjoyo.LibrarySystem.R;
import com.baige.nbsm.activty.ManagerActivity;
import com.baige.nbsm.db.nuoboDB;
import com.baige.nbsm.user.UserActivity;
import com.baige.nbsm.utils.Constants;

public class MainActivity extends Activity {
	private EditText username, password; // 用户名，密码
	RadioButton rb_student, rb_manager; // 学生，管理员
	Button btn_login;// 登陆
	Button btn_resign;//注册

	// 控件注册
	private void init() {
		username = (EditText) findViewById(R.id.edt_username);
		password = (EditText) findViewById(R.id.edt_password);
		rb_student = (RadioButton) findViewById(R.id.rb_user);
		rb_manager = (RadioButton) findViewById(R.id.rb_manager);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_resign = (Button) findViewById(R.id.btn_register);

		// 点击进入管理页面
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取用户名
				String un = username.getText().toString();
				// 获取输入的密码
				String pd = password.getText().toString();
				SQLiteDatabase db = new nuoboDB(MainActivity.this)
						.getReadableDatabase();
				Cursor cursor = null;
				if (rb_student.isChecked()) {
					cursor = db.rawQuery("select * from " + Constants.USER_TABLE
							+ " where username=? and password=? and type=?",
							new String[] { un, pd, "0" });
					if (cursor != null && cursor.getCount() > 0) { // 表示登陆成功
						cursor.moveToFirst();
						int id = cursor.getInt(cursor.getColumnIndex("_id"));
						Intent intent = new Intent(MainActivity.this,
								UserActivity.class);
						intent.putExtra("id", id);
						intent.putExtra("username", un);
						intent.putExtra("password", pd);
						startActivity(intent);
					} else {
						Toast.makeText(MainActivity.this, "用户名或密码错误！",
								Toast.LENGTH_LONG).show();
					}
					cursor.close();
				} else if (rb_manager.isChecked()) { // 表示是管理员登陆
					cursor = db.rawQuery("select * from " + Constants.USER_TABLE
							+ " where username=? and password=? and type=?",
							new String[] { un, pd, "1" });
					if (cursor != null && cursor.getCount() > 0) { // 表示登陆成功
						cursor.moveToFirst();
						int id = cursor.getInt(cursor.getColumnIndex("_id"));
						Intent intent = new Intent(MainActivity.this,
								ManagerActivity.class);
						intent.putExtra("id", id);
						intent.putExtra("username", un);
						intent.putExtra("password", pd);
						startActivity(intent);
					} else {
						Toast.makeText(MainActivity.this, "用户名或密码错误！",
								Toast.LENGTH_LONG).show();
					}
					cursor.close();
				}

			}
		});
		
		btn_resign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ResigeActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		init();
	}

}
