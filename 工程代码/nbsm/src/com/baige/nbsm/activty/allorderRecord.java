package com.baige.nbsm.activty;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.anjoyo.LibrarySystem.R;
import com.baige.nbsm.db.nuoboDB;
import com.baige.nbsm.user.orderRecord;
import com.baige.nbsm.utils.Constants;

public class allorderRecord extends Activity {
	private ListView lv;
	int id; 
	private TextView tv_record_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_lend_record);
		init();
	}

	private void init() {
		tv_record_result = (TextView) findViewById(R.id.tv_record_resut);
		lv = (ListView) findViewById(R.id.lv_recordList);
		SQLiteDatabase db = new nuoboDB(allorderRecord.this)
				.getReadableDatabase();
		
//		db.execSQL(insert into table1 select  * from table2);
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE,null);
//		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE
//				+ " where orderNumber=?",
//				new String[] {  });
		if (cursor != null && cursor.getCount() > 0) {
			String[] from = { "name", "orderTime", "returnTime" };
			int[] to = { R.id.tv_goodsName, R.id.tv_price, R.id.tv_sort };
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					allorderRecord.this, R.layout.goods_list_item, cursor, from,
					to);
			lv.setAdapter(adapter);
			lv.setVisibility(View.VISIBLE); // 设置可见
		} else {
			tv_record_result.setVisibility(View.VISIBLE);
		}

		// 触发长按时间 获取点击的id
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(allorderRecord.this, "确认买卖", Toast.LENGTH_SHORT)
						.show();

			}
		});

	}

}
