package com.baige.nbsm.user;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.anjoyo.LibrarySystem.R;
import com.baige.nbsm.db.nuoboDB;
import com.baige.nbsm.utils.Constants;

public class goodsView extends Activity {
	private ListView lv;
	private int user_id; // 用户主键id
	private int good_id; // 书籍id

	private void init(){
		lv = (ListView) findViewById(R.id.lv_goodsList);
		SQLiteDatabase db = new nuoboDB(goodsView.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE,
				null);
		String[] from = { "number", "name", "price", "sort", "status" };
		int[] to = { R.id.tv_number, R.id.tv_goodsName, R.id.tv_price,
				R.id.tv_sort, R.id.tv_status };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(goodsView.this,
				R.layout.goods_list_item, cursor, from, to);
		lv.setAdapter(adapter);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.good_view);
		Intent intent = getIntent();
		// 获取用户的主键id
		user_id = intent.getIntExtra("id", 0);
		init();
		
		// 点击进入查看界面
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				good_id = (int) id;
				Intent intent = new Intent(goodsView.this, user_booking.class);
				intent.putExtra("id", good_id); // 书籍id
				intent.putExtra("user_id", user_id); // 用户id
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onResume() {
		init();
		super.onResume();
	}

}
