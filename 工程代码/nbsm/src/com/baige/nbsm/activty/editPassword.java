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
	private int id; // ����id
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
				// ������
				String oldPd = et_old.getText().toString();
				// ������
				String newPd = et_new.getText().toString();
				// �ظ�����
				String rePd = et_re.getText().toString();
				if (!"".equals(oldPd) && !"".equals(newPd) && !"".equals(rePd)) { // ��Ϊ��
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
								Toast.makeText(editPassword.this, "�޸ĳɹ���",
										Toast.LENGTH_LONG).show();
								finish();
							} else {
								Toast.makeText(editPassword.this, "�޸�ʧ�ܣ�",
										Toast.LENGTH_LONG).show();
								finish();
							}
						} else {
							Toast.makeText(editPassword.this, "�������벻һ�£�",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(editPassword.this, "ԭʼ�������",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(editPassword.this, "�ı�����Ϊ�գ�",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	// �ж��������Ƿ�һ��
	public boolean check(String newPd, String rePd) {
		boolean flag = false;
		if (newPd.equals(rePd)) {
			flag = true;
		}
		return flag;
	}

	// �жϾ������Ƿ���ȷ
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
				flag = true; // ��ʾ��ͬ
			} else {
				flag = false;
			}
		}
		cursor.close();
		db.close();
		return flag;
	}
}
