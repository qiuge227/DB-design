package com.baige.nbsm.activty;

import java.util.HashMap;
import java.util.Map;

import com.anjoyo.LibrarySystem.R;
import com.baige.nbsm.db.nuoboDB;
import com.baige.nbsm.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class goodsDetails extends Activity {

	private TextView name;
	private TextView number;
	private TextView author;
	private TextView details;
	private TextView status;
	private int id;// 主键id

	private void init() {
		name = (TextView) findViewById(R.id.tv_rName);
		number = (TextView) findViewById(R.id.tv_rNumber);
		author = (TextView) findViewById(R.id.tv_rPrice);
		details = (TextView) findViewById(R.id.tv_rSort);
		status = (TextView) findViewById(R.id.tv_rStatus);
		Map<String, String> map = new HashMap<String, String>();
		map = getInfoById(id);
		name.setText(map.get("name"));
		number.setText(map.get("number"));
		author.setText(map.get("author"));
		details.setText(map.get("detail"));
		status.setText(map.get("status"));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_details);
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		init();
	}

	// 根据id获取信息
	public Map<String, String> getInfoById(int id) {
		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase db = new nuoboDB(goodsDetails.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE
				+ " where _id=?", new String[] { String.valueOf(id) });
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("number", cursor.getString(cursor.getColumnIndex("number")));
			map.put("author", cursor.getString(cursor.getColumnIndex("price")));
			map.put("detail", cursor.getString(cursor.getColumnIndex("sort")));
			map.put("status", cursor.getString(cursor.getColumnIndex("status")));
		}
		cursor.close();
		db.close();
		return map;
	}
}
