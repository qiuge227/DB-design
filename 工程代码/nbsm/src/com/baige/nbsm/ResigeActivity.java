package com.baige.nbsm;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anjoyo.LibrarySystem.R;
import com.baige.nbsm.activty.userManager;
import com.baige.nbsm.db.nuoboDB;
import com.baige.nbsm.utils.Constants;

public class ResigeActivity extends Activity {
	private EditText et_un;
	private EditText et_pd;
	private EditText et_ma;
	private EditText et_ph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_resign);
		// ע�᲼���еĿؼ�
		et_un = (EditText) findViewById(R.id.et_uUN);
		et_pd = (EditText) findViewById(R.id.et_uPd);
		et_ma = (EditText) findViewById(R.id.et_uMa);
		et_ph = (EditText) findViewById(R.id.et_uPh);
		init();
	}

	private void init() {
		Button btn_resigned = (Button) findViewById(R.id.btn_resigned);
		btn_resigned.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// ��ȡ�ı����е�����
				String un = et_un.getText().toString().trim(); // trim()//
				String pd = et_pd.getText().toString().trim();
				String ma = et_ma.getText().toString().trim();
				String ph = et_ph.getText().toString().trim();

				// �ǿ���֤
				if (!"".equals(un) || !"".equals(pd) || !"".equals(ma)
						|| !"".equals(ph)) {
					insert(un, pd, ma, ph); // �����ݿ����������
					Toast.makeText(ResigeActivity.this, "��ӳɹ���",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ResigeActivity.this, "�û��������벻��Ϊ�գ�",
							Toast.LENGTH_LONG).show();
				}
				finish();
			}
		});
	}

	// �����ݿ����������
	private void insert(String un, String pd, String ma, String ph) {

		SQLiteDatabase db = new nuoboDB(ResigeActivity.this)
				.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", un);
		values.put("password", pd);
		values.put("email", ma);
		values.put("phone", ph);
		values.put("type", 0);
		db.insert(Constants.USER_TABLE, "username", values);
		db.close();
	}

}
