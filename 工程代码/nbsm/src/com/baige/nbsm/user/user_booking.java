package com.baige.nbsm.user;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.anjoyo.LibrarySystem.R;
import com.baige.nbsm.db.nuoboDB;
import com.baige.nbsm.utils.Constants;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class user_booking extends Activity {

	TextView name, number, price, sort, status;
	Button btn_book; // ���أ�Ԥ��
	int book_id;// ����id
	int user_id;// �û�id
	SimpleDateFormat format = new SimpleDateFormat("MM��dd��");

	private void init() {
		name = (TextView) findViewById(R.id.tv_qName);
		number = (TextView) findViewById(R.id.tv_qNumber);
		price = (TextView) findViewById(R.id.tv_qPrice);
		sort = (TextView) findViewById(R.id.tv_qSort);
		status = (TextView) findViewById(R.id.tv_qStatus);
		btn_book = (Button) findViewById(R.id.btn_book);

		// ���Ԥ��
		btn_book.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bookUpdate(book_id);
				btn_book.setVisibility(View.GONE); // Ԥ���ɹ���ť���ɼ�
				initTextView();
			}
		});

		initTextView();
	}

	// Ԥ�������¸�������
	private void bookUpdate(int id) {
		SQLiteDatabase db = new nuoboDB(user_booking.this)
				.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("status", "��Ԥ��");
		values.put("orderNumber", String.valueOf(user_id));
		long time = System.currentTimeMillis(); // ��ȡԤ����ʱ��
		String orderTime = format.format(time); // ��ʽ��ʱ��
		values.put("orderTime", orderTime);
		long returnTime = (time + Long.valueOf("172800000"));
		String returnTimes = format.format(returnTime);
		values.put("returnTime", returnTimes);
		Toast.makeText(
				user_booking.this,
				"�𾴵��û���������" + orderTime + "��������Ʒ��������" + returnTimes
						+ "ǰ��������֧����", Toast.LENGTH_LONG).show();
		db.update(Constants.GOODS_TABLE, values, "_id=?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	private void initTextView() {
		Map<String, String> map = new HashMap<String, String>();
		map = getInfoById(book_id);
		name.setText(map.get("name"));
		number.setText(map.get("number"));
		price.setText(map.get("price"));
		sort.setText(map.get("sort"));
		status.setText(map.get("status"));
		if ("�ڿ�".equals(status.getText().toString())) {
			btn_book.setVisibility(View.VISIBLE);
		} else {
			btn_book.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_book_detail);
		Intent intent = getIntent();
		book_id = intent.getIntExtra("id", 0);
		user_id = intent.getIntExtra("user_id", 0);
		init();
	}

	// ����id��ȡ��Ϣ
	public Map<String, String> getInfoById(int id) {
		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase db = new nuoboDB(user_booking.this)
				.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE
				+ " where _id=?", new String[] { String.valueOf(id) });
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("number", cursor.getString(cursor.getColumnIndex("number")));
			map.put("price", cursor.getString(cursor.getColumnIndex("price")));
			map.put("sort", cursor.getString(cursor.getColumnIndex("sort")));
			map.put("status", cursor.getString(cursor.getColumnIndex("status")));
		}
		cursor.close();
		db.close();
		return map;
	}
}