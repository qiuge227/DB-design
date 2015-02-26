package com.baige.nbsm.activty;

import com.anjoyo.LibrarySystem.R;
import com.baige.nbsm.db.nuoboDB;
import com.baige.nbsm.utils.Constants;

import android.R.bool;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class editPassword extends Activity {
	private int id; // 主键id
	private EditText et_old, et_new, et_re;
	private Button btn_submit, btn_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editpassword);
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		et_old = (EditText) findViewById(R.id.et_oldPd);
		et_new = (EditText) findViewById(R.id.et_newPd);
		et_re = (EditText) findViewById(R.id.et_rePd);

		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 旧密码
				String oldPd = et_old.getText().toString();
				// 新密码
				String newPd = et_new.getText().toString();
				// 重复密码
				String rePd = et_re.getText().toString();
				if (!"".equals(oldPd) && !"".equals(newPd) && !"".equals(rePd)) { // 不为空
					if (checkOld(oldPd, id)) {
						if (check(newPd, rePd)) {
							SQLiteDatabase db = new nuoboDB(editPassword.this)
									.getReadableDatabase();
							ContentValues values = new ContentValues();
							values.put("password", newPd);
							int a = db.update(Constants.USER_TABLE, values,
									"_id=?",
									new String[] { String.valueOf(id) });
							if (a > 0) {
								Toast.makeText(editPassword.this, "修改成功！",
										Toast.LENGTH_LONG).show();
								finish();
							} else {
								Toast.makeText(editPassword.this, "修改失败！",
										Toast.LENGTH_LONG).show();
								finish();
							}
						} else {
							Toast.makeText(editPassword.this, "两次密码不一致！",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(editPassword.this, "原始密码错误！",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(editPassword.this, "文本框不能为空！",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	// 判断新密码是否一样
	public boolean check(String newPd, String rePd) {
		boolean flag = false;
		if (newPd.equals(rePd)) {
			flag = true;
		}
		return flag;
	}

	// 判断旧密码是否正确
	public boolean checkOld(String oldPd, int id) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(editPassword.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.USER_TABLE
				+ " where _id=?", new String[] { String.valueOf(id) });
		if (cursor != null || cursor.getCount() > 0) {
			cursor.moveToFirst();
			String password = cursor.getString(cursor
					.getColumnIndex("password"));
			if (oldPd.equals(password)) {
				flag = true; // 表示相同
			} else {
				flag = false;
			}
		}
		cursor.close();
		db.close();
		return flag;
	}
}
