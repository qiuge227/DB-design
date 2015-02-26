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
	private EditText username, password; // �û���������
	RadioButton rb_student, rb_manager; // ѧ��������Ա
	Button btn_login;// ��½
	Button btn_resign;//ע��

	// �ؼ�ע��
	private void init() {
		username = (EditText) findViewById(R.id.edt_username);
		password = (EditText) findViewById(R.id.edt_password);
		rb_student = (RadioButton) findViewById(R.id.rb_user);
		rb_manager = (RadioButton) findViewById(R.id.rb_manager);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_resign = (Button) findViewById(R.id.btn_register);

		// ����������ҳ��
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��ȡ�û���
				String un = username.getText().toString();
				// ��ȡ���������
				String pd = password.getText().toString();
				SQLiteDatabase db = new nuoboDB(MainActivity.this)
						.getReadableDatabase();
				Cursor cursor = null;
				if (rb_student.isChecked()) {
					cursor = db.rawQuery("select * from " + Constants.USER_TABLE
							+ " where username=? and password=? and type=?",
							new String[] { un, pd, "0" });
					if (cursor != null && cursor.getCount() > 0) { // ��ʾ��½�ɹ�
						cursor.moveToFirst();
						int id = cursor.getInt(cursor.getColumnIndex("_id"));
						Intent intent = new Intent(MainActivity.this,
								UserActivity.class);
						intent.putExtra("id", id);
						intent.putExtra("username", un);
						intent.putExtra("password", pd);
						startActivity(intent);
					} else {
						Toast.makeText(MainActivity.this, "�û������������",
								Toast.LENGTH_LONG).show();
					}
					cursor.close();
				} else if (rb_manager.isChecked()) { // ��ʾ�ǹ���Ա��½
					cursor = db.rawQuery("select * from " + Constants.USER_TABLE
							+ " where username=? and password=? and type=?",
							new String[] { un, pd, "1" });
					if (cursor != null && cursor.getCount() > 0) { // ��ʾ��½�ɹ�
						cursor.moveToFirst();
						int id = cursor.getInt(cursor.getColumnIndex("_id"));
						Intent intent = new Intent(MainActivity.this,
								ManagerActivity.class);
						intent.putExtra("id", id);
						intent.putExtra("username", un);
						intent.putExtra("password", pd);
						startActivity(intent);
					} else {
						Toast.makeText(MainActivity.this, "�û������������",
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
