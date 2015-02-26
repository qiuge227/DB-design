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
	private int position;// �����λ��
	private int id; // ��Ӧ����id

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

		registerForContextMenu(lv); // �����Ĳ˵�Ҫ�ȸ��ؼ�ע��
		// ���������Ĳ˵�
		lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// �������
				MenuInflater menuInflater = new MenuInflater(userManager.this);
				menuInflater.inflate(R.menu.user, menu);
			}
		});

		// ������ʾѡ��˵� �༭��ɾ��
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				position = arg2; // ��õ����λ��
				id = (int) arg3;
				return false;
			}
		});
	}

	// ѡ��˵�����¼�
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ID_EDIT: // ����༭��ʱ��
			Toast.makeText(userManager.this, "�༭", Toast.LENGTH_LONG).show();
			Dialog(id);
			break;
		case R.id.ID_DELETE: // ���ɾ����ʱ��
			deleteDlalog(id);
			break;

		}
		return super.onContextItemSelected(item);
	}

	// ɾ����ȷ�Ͽ�
	private void deleteDlalog(int id2) {
		AlertDialog.Builder builder = new AlertDialog.Builder(userManager.this);
		builder.setTitle("ȷ��ɾ����");
		builder.setIcon(android.R.drawable.ic_delete);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (deleteById(id)) { // ��ʾɾ���ɹ�
					Toast.makeText(userManager.this, "ɾ���ɹ���", Toast.LENGTH_LONG)
							.show();
					init(); // ���°�����
				} else {
					Toast.makeText(userManager.this, "ɾ��ʧ�ܣ�", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "����û�").setIcon(android.R.drawable.ic_menu_add);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Dialog(-1);
		return super.onOptionsItemSelected(item);
	}

	// ��������û��Ի���
	private void Dialog(final int id) {

		AlertDialog.Builder builder = new AlertDialog.Builder(userManager.this);
		// ���Ի�������䲼��
		View view = LayoutInflater.from(userManager.this).inflate(
				R.layout.add_or_edit_item, null);
		// ������ͼ���Ի���
		builder.setView(view);
		// ע�᲼���еĿؼ�
		final EditText et_un = (EditText) view.findViewById(R.id.et_uUN);
		final EditText et_pd = (EditText) view.findViewById(R.id.et_uPd);
		final EditText et_ma = (EditText) view.findViewById(R.id.et_uMa);
		final EditText et_ph = (EditText) view.findViewById(R.id.et_uPh);
		if (-1 != id) { // ��ʾ�༭���� ��Ҫ����id������ȡ���������ı���
			HashMap<String, String> map = getInfoById(id);
			et_pd.setText(map.get("password"));
			et_un.setText(map.get("username"));
			et_ma.setText(map.get("email"));
			et_ph.setText(map.get("phone"));
		}
		builder.setIcon(android.R.drawable.ic_menu_add);
		builder.setTitle("����");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ��ȡ�ı����е�����
				String un = et_un.getText().toString().trim(); // trim()//
																// ȥ��ǰ��ո����˼
				String pd = et_pd.getText().toString().trim();
				String ma = et_ma.getText().toString().trim();
				String ph = et_ph.getText().toString().trim();
				for (int i = 0; i < 5; i++) {
					insert("baige", "123", "bg@qq.com", "110"); // �����ݿ����������
				}
				// �ǿ���֤
				if (!"".equals(un) || !"".equals(pd) || !"".equals(ma)
						|| !"".equals(ph)) {
					if (-1 == id) {
						insert(un, pd, ma, ph); // �����ݿ����������
						Toast.makeText(userManager.this, "��ӳɹ���",
								Toast.LENGTH_LONG).show();
					} else {
						update(id, un, pd, ma, ph);
						Toast.makeText(userManager.this, "���³ɹ���",
								Toast.LENGTH_LONG).show();
					}
					// ���°�����
					init();
				} else {
					Toast.makeText(userManager.this, "�û��������벻��Ϊ�գ�",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}

	// �����ݿ����������
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

	// ��������
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

	// ͨ��idɾ��
	private boolean deleteById(int id) {
		boolean flag = false;
		SQLiteDatabase db = new nuoboDB(userManager.this).getReadableDatabase();
		int a = db.delete(Constants.USER_TABLE, "_id=?",
				new String[] { String.valueOf(id) });
		if (a > 0) { // ��ʾɾ���ɹ�
			flag = true;
		} else {
			flag = false;
		}
		db.close();
		return flag;
	}

	// ͨ������id��ȡ����
	private HashMap<String, String> getInfoById(int id) {
		HashMap<String, String> map = new HashMap<String, String>();
		SQLiteDatabase db = new nuoboDB(userManager.this).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Constants.USER_TABLE
				+ " where _id=?", new String[] { String.valueOf(id) });
		if (cursor != null && cursor.getCount() > 0) { // ��ʾ������
			cursor.moveToFirst();
			map.put("username",
					cursor.getString(cursor.getColumnIndex("username")));
			map.put("password",
					cursor.getString(cursor.getColumnIndex("password")));
			map.put("email", cursor.getString(cursor.getColumnIndex("email")));
			map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
		} else {
			Toast.makeText(userManager.this, "������", Toast.LENGTH_LONG).show();
		}
		cursor.close();
		db.close();
		return map;
	}
}
