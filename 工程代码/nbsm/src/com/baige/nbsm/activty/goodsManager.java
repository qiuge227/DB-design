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
	ListView lv;//��ʾ��Ʒ�б�
	int id;// ����id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_manager);
		init();
	}

	// ʵ�����ؼ�
	private void init() {
		lv = (ListView) findViewById(R.id.lv_goodsList);
		//ʵ����һ�����ݿ⣬Ȩ����Ϊ�ɶ�
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		//ͨ���α��ѯ���ݿ��е�����
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

		// �����������ҳ��
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

	// �����ĵĵ���¼�
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ID_EDIT: // �༭
			Dialog(id);
			break;
		case R.id.ID_DELETE: // ɾ��
			if (checkLend(id)) {
				deleteDlalog(id);
			} else {
				Toast.makeText(goodsManager.this, "�ò�Ʒ�ѱ�����������ɾ����",
						Toast.LENGTH_LONG).show();
			}
			break;
		}
		return super.onContextItemSelected(item);
	}

	// ѡ��˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "���").setIcon(android.R.drawable.ic_menu_add);
		return super.onCreateOptionsMenu(menu);
	}

	// ѡ��˵�����¼�
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Dialog(-1);
		return super.onOptionsItemSelected(item);
	}

	// ��ӻ�༭�Ի���
	private void Dialog(final int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(goodsManager.this);
		View view = LayoutInflater.from(goodsManager.this).inflate(
				R.layout.goods_add_or_edit_item, null);
		builder.setView(view);
		//ע��༭��
		final EditText number = (EditText) view.findViewById(R.id.et_bNumber);
		final EditText name = (EditText) view.findViewById(R.id.et_bName);
		final EditText price = (EditText) view.findViewById(R.id.et_bPrice);
		final EditText sort = (EditText) view.findViewById(R.id.et_bSort);
		builder.setIcon(android.R.drawable.ic_menu_add);
		builder.setTitle("��Ʒ���");
		if (-1 != id) { // ��ʾ�༭ ��Ҫ����id��ȡ����
			Map<String, String> map = getInfoById(id);
			number.setText(map.get("number"));
			name.setText(map.get("name"));
			price.setText(map.get("price"));
			sort.setText(map.get("sort"));
		}
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//��ʾ��ȡ�༭���е�����
				String num = number.getText().toString();
				String names = name.getText().toString();
				String prices = price.getText().toString();
				String sorts = sort.getText().toString();
				for (int i = 0; i < 5; i++) {
					insert("5", "���֭", "35Ԫ", "����");
				}
				// ��ӻ��߱༭����
				if (!"".equals(num) && !"".equals(names) && !"".equals(prices)
						&& !"".equals(sorts)) {
					if (-1 == id) { // ��ʾ���
						if (!checkName(names,id)) { // ��ʾû����ͬ������
							if (insert(num, names, prices, sorts)) {
								Toast.makeText(goodsManager.this, "��ӳɹ���",
										Toast.LENGTH_LONG).show();
								init(); // ���°�
							} else {
								Toast.makeText(goodsManager.this, "���ʧ�ܣ�",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(goodsManager.this, "�ò�Ʒ�Ѵ��ڣ�",
									Toast.LENGTH_LONG).show();
						}
					} else { // ��ʾ�༭
						if (!checkName(names,id)) { // ��ʾû����ͬ������
							if (update(id, num, names, prices, sorts)) {
								Toast.makeText(goodsManager.this, "���³ɹ���",
										Toast.LENGTH_LONG).show();
								init(); // ���°�
							} else {
								Toast.makeText(goodsManager.this, "����ʧ�ܣ�",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(goodsManager.this, "�ò�Ʒ�Ѵ��ڣ�",
									Toast.LENGTH_LONG).show();
						}
					}

				} else {
					Toast.makeText(goodsManager.this, "��������ݲ���Ϊ�գ�",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}

	// �������
	protected boolean insert(String num, String names, String prices,
			String sorts) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", num);
		values.put("name", names);
		values.put("price", prices);
		values.put("sort", sorts);
		values.put("status", "�ڿ�");
		values.put("orderNumber", "-1");//�����ж��û���û��Ԥ��
		int a = (int) db.insert(Constants.GOODS_TABLE, "number", values); // �������
		if (a > 0) { // ��ʾ����ɹ�
			flag = true;
		} else {
			flag = false;
		}
		db.close();
		return flag;
	}

	// ����Ƿ�����ͬ������
	private boolean checkName(String names,int id) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE
				+ " where name=? and _id="+id, new String[] { names });
		if (cursor.getCount() > 0) { // ��ʾ�����������
			flag = true; // ��ʾ�����������
		} else {
			flag = false;
		}
		db.close();
		cursor.close();
		return flag;
	}

	// ���²���
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
		if (a > 0) { // ��ʾ���³ɹ�
			flag = true;
		} else {
			flag = false;
		}
		db.close();
		return flag;
	}

	// ɾ����ȷ�Ͽ�
	private void deleteDlalog(int id2) {
		AlertDialog.Builder builder = new AlertDialog.Builder(goodsManager.this);
		builder.setTitle("ȷ��ɾ����");
		builder.setIcon(android.R.drawable.ic_delete);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (delete(id)) { // ��ʾɾ���ɹ�
					Toast.makeText(goodsManager.this, "ɾ���ɹ���", Toast.LENGTH_LONG)
							.show();
					init(); // ���°�����
				} else {
					Toast.makeText(goodsManager.this, "ɾ��ʧ�ܣ�", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}

	// ɾ��ʱҪ�ж��Ƿ�ԤԼ����
	private boolean checkLend(int id) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.GOODS_TABLE
				+ " where _id=?", new String[] { String.valueOf(id) });
		if (cursor.getCount() > 0) { // ��ʾ�����Ȿ��
			cursor.moveToFirst();
			String status = cursor.getString(cursor.getColumnIndex("status"));
			if ("�ڿ�".equals(status)) {
				flag = true; // ��ʾ����û�б�ԤԼ���߽��
			} else {
				flag = false;
			}
		}
		db.close();
		cursor.close();
		return flag;
	}

	// ɾ������
	private boolean delete(int id) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(goodsManager.this).getReadableDatabase();
		int a = db.delete(Constants.GOODS_TABLE, "_id=?",
				new String[] { String.valueOf(id) });
		if (a > 0) { // ��ʾɾ���ɹ�
			flag = true;
			init(); // ���°�
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
