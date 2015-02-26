package com.baige.nbsm.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.anjoyo.LibrarySystem.R;
import com.baige.nbsm.activty.goodsManager;
import com.baige.nbsm.db.nuoboDB;
import com.baige.nbsm.utils.Constants;

public class orderRecord extends Activity {
	private ListView lv;
	int user_id; // �û�id
	private TextView tv_record_result;
	private int goods_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lend_record);
		Intent intent = getIntent();
		user_id = intent.getIntExtra("id", 0);
		tv_record_result = (TextView) findViewById(R.id.tv_record_resut);
		init();
	}

	private void init() {
		lv = (ListView) findViewById(R.id.lv_recordList);
		SQLiteDatabase db = new nuoboDB(orderRecord.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE
				+ " where orderNumber=?",
				new String[] { String.valueOf(user_id) });
		if (cursor != null && cursor.getCount() > 0) {
			String[] from = { "name", "orderTime", "returnTime" };
			int[] to = { R.id.bookname_tv, R.id.time_tv, R.id.return_tiem_tv };
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					orderRecord.this, R.layout.lend_record_item, cursor, from,
					to);
			lv.setAdapter(adapter);
			lv.setVisibility(View.VISIBLE); // ���ÿɼ�
		} else {
			tv_record_result.setVisibility(View.VISIBLE);
		}

		registerForContextMenu(lv); // ע��������
		lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, "�˶�");
			}
		});

		// ��������ʱ�� ��ȡ�����id
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				goods_id = (int) id;
				return false;
			}
		});

	}

	// ����˶������¼�
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		dialog(goods_id);

		return super.onContextItemSelected(item);
	}

	private void dialog(final int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(orderRecord.this);
		builder.setTitle("����");
		builder.setMessage("ȷ���˶���");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SQLiteDatabase db = new nuoboDB(orderRecord.this)
						.getReadableDatabase();
				ContentValues values = new ContentValues();
				values.put("orderNumber", "-1");
				values.put("orderTime", -1);
				values.put("returnTime", "�˶�");
				values.put("status", "�ڿ�");
				db.update(Constants.GOODS_TABLE, values, "_id=?",
						new String[] { String.valueOf(id) });
				lv.setVisibility(View.GONE);
				tv_record_result.setVisibility(View.GONE);
				init();
				db.close();
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}

	// �ֱ��û���Ϣ����Ԥ����Ϣ�����±���
	// �������
	public boolean insert(String usernames, String names, String orderTimes,
			String returnTimes, String phones) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(orderRecord.this).getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", usernames);
		values.put("name", names);
		values.put("orderTime", orderTimes);
		values.put("returnTime", returnTimes);
		values.put("phone", phones);
		int a = (int) db.insert(Constants.TABLE, "username", values); // �������
		if (a > 0) { // ��ʾ����ɹ�
			flag = true;
		} else {
			flag = false;
		}
		db.close();
		return flag;
	}
}
