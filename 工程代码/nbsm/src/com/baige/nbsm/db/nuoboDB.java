package com.baige.nbsm.db;

import com.baige.nbsm.utils.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class nuoboDB extends SQLiteOpenHelper {

	public nuoboDB(Context context) {
		super(context, Constants.DATABASE, null, Constants.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

//		// 创建订单表
//		db.execSQL("create table if not exists " + Constants.TABLE
//				+ " (_id integer primary key autoincrement,"
//				+ "username String," + "name String," + "orderTime String,"
//				+ "returnTime String," + "phone String)");

		// 创建产品表
		db.execSQL("create table if not exists " + Constants.GOODS_TABLE
				+ " (_id integer primary key autoincrement," + "number String,"
				+ "name String," + "price String," + "sort String,"
				+ "status String," + "author String," + "orderNumber String,"
				+ "orderTime String," + "returnTime String)");

		// 创建用户表
		db.execSQL("create table if not exists " + Constants.USER_TABLE
				+ " (_id integer primary key autoincrement,"
				+ "username String," + "password String," + "email String,"
				+ "phone String," + "type integer)");

		ContentValues values = new ContentValues();
		values.put("username", "admin");
		values.put("password", "123456");
		values.put("email", "4444@qq.com");
		values.put("phone", "5807637");
		values.put("type", 1);
		db.insert(Constants.USER_TABLE, "username", values);

	}

	// 数据库版本改变的时候执行该方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + Constants.GOODS_TABLE);
		db.execSQL("drop table if exists " + Constants.USER_TABLE);
		db.execSQL("drop table if exists " + Constants.TABLE);
		onCreate(db);
	}

}
