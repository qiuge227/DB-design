package com.baige.nbsm.activty;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.anjoyo.LibrarySystem.R;
import com.baige.nbsm.db.nuoboDB;
import com.baige.nbsm.utils.Constants;

public class userManager extends Activity {
	private ListView lv;
	private int position;// 点击的位置
	private int id; // 对应主键id

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_manager);
		init();
	}

	private void init() {
		lv = (ListView) findViewById(R.id.lv_userList);
		SQLiteDatabase db = new nuoboDB(userManager.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.USER_TABLE,
				null);
		String[] from = { "username", "password", "email", "phone", "type" };
		int[] to = { R.id.tv_iUsername, R.id.tv_iPassword, R.id.tv_iEmail,
				R.id.tv_iPhone, R.id.tv_iSt };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(userManager.this,
				R.layout.user_list_item, cursor, from, to);
		lv.setAdapter(adapter);

		registerForContextMenu(lv); // 上下文菜单要先给控件注册
		// 创建上下文菜单
		lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// 填充数据
				MenuInflater menuInflater = new MenuInflater(userManager.this);
				menuInflater.inflate(R.menu.user, menu);
			}
		});

		// 长按显示选项菜单 编辑和删除
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				position = arg2; // 获得点击的位置
				id = (int) arg3;
				return false;
			}
		});
	}

	// 选项菜单点击事件
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ID_EDIT: // 点击编辑的时候
			Toast.makeText(userManager.this, "编辑", Toast.LENGTH_LONG).show();
			Dialog(id);
			break;
		case R.id.ID_DELETE: // 点击删除的时候
			deleteDlalog(id);
			break;

		}
		return super.onContextItemSelected(item);
	}

	// 删除的确认框
	private void deleteDlalog(int id2) {
		AlertDialog.Builder builder = new AlertDialog.Builder(userManager.this);
		builder.setTitle("确认删除吗？");
		builder.setIcon(android.R.drawable.ic_delete);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (deleteById(id)) { // 表示删除成功
					Toast.makeText(userManager.this, "删除成功！", Toast.LENGTH_LONG)
							.show();
					init(); // 重新绑定数据
				} else {
					Toast.makeText(userManager.this, "删除失败！", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "添加用户").setIcon(android.R.drawable.ic_menu_add);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Dialog(-1);
		return super.onOptionsItemSelected(item);
	}

	// 创建添加用户对话框
	private void Dialog(final int id) {

		AlertDialog.Builder builder = new AlertDialog.Builder(userManager.this);
		// 往对话框中填充布局
		View view = LayoutInflater.from(userManager.this).inflate(
				R.layout.add_or_edit_item, null);
		// 创建视图给对话框
		builder.setView(view);
		// 注册布局中的控件
		final EditText et_un = (EditText) view.findViewById(R.id.et_uUN);
		final EditText et_pd = (EditText) view.findViewById(R.id.et_uPd);
		final EditText et_ma = (EditText) view.findViewById(R.id.et_uMa);
		final EditText et_ph = (EditText) view.findViewById(R.id.et_uPh);
		if (-1 != id) { // 表示编辑数据 需要根据id把数据取出来赋给文本框
			HashMap<String, String> map = getInfoById(id);
			et_pd.setText(map.get("password"));
			et_un.setText(map.get("username"));
			et_ma.setText(map.get("email"));
			et_ph.setText(map.get("phone"));
		}
		builder.setIcon(android.R.drawable.ic_menu_add);
		builder.setTitle("操作");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 获取文本框中的内容
				String un = et_un.getText().toString().trim(); // trim()//
																// 去除前后空格的意思
				String pd = et_pd.getText().toString().trim();
				String ma = et_ma.getText().toString().trim();
				String ph = et_ph.getText().toString().trim();
				for (int i = 0; i < 5; i++) {
					insert("baige", "123", "bg@qq.com", "110"); // 往数据库中添加数据
				}
				// 非空验证
				if (!"".equals(un) || !"".equals(pd) || !"".equals(ma)
						|| !"".equals(ph)) {
					if (-1 == id) {
						insert(un, pd, ma, ph); // 往数据库中添加数据
						Toast.makeText(userManager.this, "添加成功！",
								Toast.LENGTH_LONG).show();
					} else {
						update(id, un, pd, ma, ph);
						Toast.makeText(userManager.this, "更新成功！",
								Toast.LENGTH_LONG).show();
					}
					// 重新绑定数据
					init();
				} else {
					Toast.makeText(userManager.this, "用户名或密码不能为空！",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	// 往数据库中添加数据
	private void insert(String un, String pd, String ma, String ph) {

		SQLiteDatabase db = new nuoboDB(userManager.this).getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", un);
		values.put("password", pd);
		values.put("email", ma);
		values.put("phone", ph);
		values.put("type", 0);
		db.insert(Constants.USER_TABLE, "username", values);
		db.close();
	}

	// 更新数据
	private void update(int id, String un, String pd, String ma, String ph) {
		SQLiteDatabase db = new nuoboDB(userManager.this).getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", un);
		values.put("password", pd);
		values.put("email", ma);
		values.put("phone", ph);
		values.put("type", 0);
		db.update(Constants.USER_TABLE, values, "_id=?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// 通过id删除
	private boolean deleteById(int id) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(userManager.this).getReadableDatabase();
		int a = db.delete(Constants.USER_TABLE, "_id=?",
				new String[] { String.valueOf(id) });
		if (a > 0) { // 表示删除成功
			flag = true;
		} else {
			flag = false;
		}
		db.close();
		return flag;
	}

	// 通过主键id获取数据
	private HashMap<String, String> getInfoById(int id) {
		HashMap<String, String> map = new HashMap<String, String>();
		SQLiteDatabase db = new nuoboDB(userManager.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.USER_TABLE
				+ " where _id=?", new String[] { String.valueOf(id) });
		if (cursor != null && cursor.getCount() > 0) { // 表示有数据
			cursor.moveToFirst();
			map.put("username",
					cursor.getString(cursor.getColumnIndex("username")));
			map.put("password",
					cursor.getString(cursor.getColumnIndex("password")));
			map.put("email", cursor.getString(cursor.getColumnIndex("email")));
			map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
		} else {
			Toast.makeText(userManager.this, "无数据", Toast.LENGTH_LONG).show();
		}
		cursor.close();
		db.close();
		return map;
	}
}
