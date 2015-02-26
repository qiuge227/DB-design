package com.baige.nbsm.user;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.anjoyo.LibrarySystem.R;
import com.baige.nbsm.db.nuoboDB;
import com.baige.nbsm.utils.Constants;

public class goodsSearch extends Activity {
	private ListView lv; // 绑定的数据
	private View result_layout; // 查找结果的listview
	private EditText content; // 查找的内容
	private Button btn_search; // 搜索按钮
	private TextView tv_result;
	private int user_id; // 用户主键id
	private int good_id; // 书籍id

	private void init() {
		tv_result = (TextView) findViewById(R.id.tv_result);
		lv = (ListView) findViewById(R.id.list_books);
		result_layout = findViewById(R.id.resultLayout);
		content = (EditText) findViewById(R.id.earchContent_et);
		btn_search = (Button) findViewById(R.id.search_btn);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 先设置为不可见
				result_layout.setVisibility(View.GONE);
				tv_result.setVisibility(View.GONE);
				String result = content.getText().toString().trim();
				if (!"".equals(result)) {
					initListview(result);
				} else {
					Toast.makeText(goodsSearch.this, "查找条件不能为空！",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		// 点击进入查看界面
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				good_id = (int) id;
				Intent intent = new Intent(goodsSearch.this,
						user_booking.class);
				intent.putExtra("id", good_id); // 书籍id
				intent.putExtra("user_id", user_id); // 用户id
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		String result = content.getText().toString().trim();
		initListview(result);
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_search);
		Intent intent = getIntent();
		// 获取用户的主键id
		user_id = intent.getIntExtra("id", 0);
		init();
	}

	// 通过条件查找书籍,绑定到listview上
	private void initListview(String condition) {

		SQLiteDatabase db = new nuoboDB(goodsSearch.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE
				+ " where name=? or number=? or price=? or sort=?",
				new String[] { ""+condition, condition, condition, ""+condition });
		if (cursor != null && cursor.getCount() > 0) { // 表示查到数据
			String[] from = { "number", "name", "price", "sort", "status" };
			int[] to = { R.id.tv_number, R.id.tv_goodsName, R.id.tv_price,
					R.id.tv_sort, R.id.tv_status };
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					goodsSearch.this, R.layout.goods_list_item, cursor, from, to);
			lv.setAdapter(adapter);
			result_layout.setVisibility(View.VISIBLE); // 布局可见
		} else {
			tv_result.setVisibility(View.VISIBLE);
		}
	}
}
