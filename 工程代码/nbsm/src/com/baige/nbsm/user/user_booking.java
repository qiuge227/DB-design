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
	Button btn_book; // 返回，预定
	int book_id;// 主键id
	int user_id;// 用户id
	SimpleDateFormat format = new SimpleDateFormat("MM月dd日");

	private void init() {
		name = (TextView) findViewById(R.id.tv_qName);
		number = (TextView) findViewById(R.id.tv_qNumber);
		price = (TextView) findViewById(R.id.tv_qPrice);
		sort = (TextView) findViewById(R.id.tv_qSort);
		status = (TextView) findViewById(R.id.tv_qStatus);
		btn_book = (Button) findViewById(R.id.btn_book);

		// 点击预定
		btn_book.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bookUpdate(book_id);
				btn_book.setVisibility(View.GONE); // 预定成功后按钮不可见
				initTextView();
			}
		});

		initTextView();
	}

	// 预定，重新更新数据
	private void bookUpdate(int id) {
		SQLiteDatabase db = new nuoboDB(user_booking.this)
				.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("status", "已预定");
		values.put("orderNumber", String.valueOf(user_id));
		long time = System.currentTimeMillis(); // 获取预定的时间
		String orderTime = format.format(time); // 格式化时间
		values.put("orderTime", orderTime);
		long returnTime = (time + Long.valueOf("172800000"));
		String returnTimes = format.format(returnTime);
		values.put("returnTime", returnTimes);
		Toast.makeText(
				user_booking.this,
				"尊敬的用户，您已于" + orderTime + "订购本产品，请您在" + returnTimes
						+ "前进行在线支付！", Toast.LENGTH_LONG).show();
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
		if ("在库".equals(status.getText().toString())) {
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

	// 根据id获取信息
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