package com.baige.nbsm.activty;

import java.util.HashMap;
import java.util.Map;

import android.R.anim;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.anjoyo.LibrarySystem.R;
import com.baige.nbsm.db.nuoboDB;
import com.baige.nbsm.utils.Constants;

public class goodsManager extends Activity {
	ListView lv;//显示产品列表
	int id;// 主键id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_manager);
		init();
	}

	// 实例化控件
	private void init() {
		lv = (ListView) findViewById(R.id.lv_goodsList);
		//实例化一个数据库，权限设为可读
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		//通过游标查询数据库中的数据
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE,
				null);
		String[] from = { "number", "name","price","sort", "status" };

		int[] to = { R.id.tv_number, R.id.tv_goodsName,R.id.tv_price,R.id.tv_sort,  R.id.tv_status };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(goodsManager.this,
				R.layout.goods_list_item, cursor, from, to);
		lv.setAdapter(adapter);
		registerForContextMenu(lv);
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				id = (int) arg3;
				return false;
			}
		});

		// 点击进入详情页面
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int id = (int) arg3;
				Intent intent = new Intent(goodsManager.this, goodsDetails.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}

		});

		lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				MenuInflater inflater = new MenuInflater(goodsManager.this);
				inflater.inflate(R.menu.goods, menu);
			}
		});
	}

	// 上下文的点击事件
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ID_EDIT: // 编辑
			Dialog(id);
			break;
		case R.id.ID_DELETE: // 删除
			if (checkLend(id)) {
				deleteDlalog(id);
			} else {
				Toast.makeText(goodsManager.this, "该产品已被订购，不能删除！",
						Toast.LENGTH_LONG).show();
			}
			break;
		}
		return super.onContextItemSelected(item);
	}

	// 选项菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "添加").setIcon(android.R.drawable.ic_menu_add);
		return super.onCreateOptionsMenu(menu);
	}

	// 选项菜单点击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Dialog(-1);
		return super.onOptionsItemSelected(item);
	}

	// 添加或编辑对话框
	private void Dialog(final int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(goodsManager.this);
		View view = LayoutInflater.from(goodsManager.this).inflate(
				R.layout.goods_add_or_edit_item, null);
		builder.setView(view);
		//注册编辑框
		final EditText number = (EditText) view.findViewById(R.id.et_bNumber);
		final EditText name = (EditText) view.findViewById(R.id.et_bName);
		final EditText price = (EditText) view.findViewById(R.id.et_bPrice);
		final EditText sort = (EditText) view.findViewById(R.id.et_bSort);
		builder.setIcon(android.R.drawable.ic_menu_add);
		builder.setTitle("产品入库");
		if (-1 != id) { // 表示编辑 、要根据id获取内容
			Map<String, String> map = getInfoById(id);
			number.setText(map.get("number"));
			name.setText(map.get("name"));
			price.setText(map.get("price"));
			sort.setText(map.get("sort"));
		}
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//表示获取编辑框中的内容
				String num = number.getText().toString();
				String names = name.getText().toString();
				String prices = price.getText().toString();
				String sorts = sort.getText().toString();
				for (int i = 0; i < 5; i++) {
					insert("5", "茵陈汁", "35元", "果饮");
				}
				// 添加或者编辑操作
				if (!"".equals(num) && !"".equals(names) && !"".equals(prices)
						&& !"".equals(sorts)) {
					if (-1 == id) { // 表示添加
						if (!checkName(names,id)) { // 表示没有相同的名字
							if (insert(num, names, prices, sorts)) {
								Toast.makeText(goodsManager.this, "添加成功！",
										Toast.LENGTH_LONG).show();
								init(); // 重新绑定
							} else {
								Toast.makeText(goodsManager.this, "添加失败！",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(goodsManager.this, "该产品已存在！",
									Toast.LENGTH_LONG).show();
						}
					} else { // 表示编辑
						if (!checkName(names,id)) { // 表示没有相同的名字
							if (update(id, num, names, prices, sorts)) {
								Toast.makeText(goodsManager.this, "更新成功！",
										Toast.LENGTH_LONG).show();
								init(); // 重新绑定
							} else {
								Toast.makeText(goodsManager.this, "更新失败！",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(goodsManager.this, "该产品已存在！",
									Toast.LENGTH_LONG).show();
						}
					}

				} else {
					Toast.makeText(goodsManager.this, "输入框内容不能为空！",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		builder.setNegativeButton("取消", null);
		builder.show();
	}

	// 插入操作
	protected boolean insert(String num, String names, String prices,
			String sorts) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", num);
		values.put("name", names);
		values.put("price", prices);
		values.put("sort", sorts);
		values.put("status", "在库");
		values.put("orderNumber", "-1");//用来判断用户有没有预定
		int a = (int) db.insert(Constants.GOODS_TABLE, "number", values); // 插入操作
		if (a > 0) { // 表示插入成功
			flag = true;
		} else {
			flag = false;
		}
		db.close();
		return flag;
	}

	// 检测是否有相同的名字
	private boolean checkName(String names,int id) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE
				+ " where name=? and _id="+id, new String[] { names });
		if (cursor.getCount() > 0) { // 表示存在这个名字
			flag = true; // 表示存在这个名字
		} else {
			flag = false;
		}
		db.close();
		cursor.close();
		return flag;
	}

	// 更新操作
	protected boolean update(int id2, String num, String names, String prices,
			String sorts) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", num);
		values.put("name", names);
		values.put("price", prices);
		values.put("sort", sorts);
		int a = db.update(Constants.GOODS_TABLE, values, "_id=?",
				new String[] { String.valueOf(id2) });
		if (a > 0) { // 表示更新成功
			flag = true;
		} else {
			flag = false;
		}
		db.close();
		return flag;
	}

	// 删除的确认框
	private void deleteDlalog(int id2) {
		AlertDialog.Builder builder = new AlertDialog.Builder(goodsManager.this);
		builder.setTitle("确认删除吗？");
		builder.setIcon(android.R.drawable.ic_delete);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (delete(id)) { // 表示删除成功
					Toast.makeText(goodsManager.this, "删除成功！", Toast.LENGTH_LONG)
							.show();
					init(); // 重新绑定数据
				} else {
					Toast.makeText(goodsManager.this, "删除失败！", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	// 删除时要判断是否被预约或借出
	private boolean checkLend(int id) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE
				+ " where _id=?", new String[] { String.valueOf(id) });
		if (cursor.getCount() > 0) { // 表示存在这本书
			cursor.moveToFirst();
			String status = cursor.getString(cursor.getColumnIndex("status"));
			if ("在库".equals(status)) {
				flag = true; // 表示该书没有被预约或者借出
			} else {
				flag = false;
			}
		}
		db.close();
		cursor.close();
		return flag;
	}

	// 删除操作
	private boolean delete(int id) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		int a = db.delete(Constants.GOODS_TABLE, "_id=?",
				new String[] { String.valueOf(id) });
		if (a > 0) { // 表示删除成功
			flag = true;
			init(); // 重新绑定
		} else {
			flag = false;
		}
		db.close();
		return flag;
	}

	private Map<String, String> getInfoById(int id) {
		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE
				+ " where _id=?", new String[] { String.valueOf(id) });
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			map.put("number", cursor.getString(cursor.getColumnIndex("number")));
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("price", cursor.getString(cursor.getColumnIndex("price")));
			map.put("sort", cursor.getString(cursor.getColumnIndex("sort")));
		}
		cursor.close();
		db.close();
		return map;
	}
}
