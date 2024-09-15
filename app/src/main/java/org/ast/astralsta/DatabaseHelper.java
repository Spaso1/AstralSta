package org.ast.astralsta;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 定义数据库名和版本号
    public static final String DATABASE_NAME = "stats.db";
    public static final int DATABASE_VERSION = 1;

    // 数据表名称
    public static final String TABLE_NAME = "StaTable";

    // 字段定义
    public static final String COLUMN_ID = "id"; // 自增主键
    public static final String COLUMN_TIME = "time"; // 时间
    public static final String COLUMN_TO = "tto";// 收款方
    public static final String COLUMN_GOOD = "good";// 商品
    public static final String COLUMN_IN_OUT = "in_out";//收入or支出
    public static final String COLUMN_CODE = "code";// 编码
    public static final String COLUMN_ENUM = "enum";// 类型

    // 创建数据表的SQL语句
    private static final String CREATE_TABLE_STA_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TIME + " TEXT NOT NULL," +
                    COLUMN_TO + " TEXT," +
                    COLUMN_GOOD + " TEXT," +
                    COLUMN_IN_OUT + " INTEGER," +
                    COLUMN_CODE + " TEXT," +
                    COLUMN_ENUM + " TEXT" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STA_TABLE);
    }

    // 升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果需要的话，可以在这里添加删除旧表、创建新表等操作
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public long addItem(String time, String to, String good, double in_out, String code, String enumValue) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_TO, to);
        values.put(COLUMN_GOOD, good);
        values.put(COLUMN_IN_OUT, in_out);
        values.put(COLUMN_CODE, code);
        values.put(COLUMN_ENUM, enumValue);

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }
    public long addItem(Item item) {
        String time = item.getTime();
        String to = item.getTo();
        String good = item.getGood();
        good = good.replace("\"","");

        double in_out = item.getIn_out();
        String code = item.getCode();
        String enumValue = item.getEnumValue();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_TO, to);
        values.put(COLUMN_GOOD, good);
        values.put(COLUMN_IN_OUT, in_out);
        values.put(COLUMN_CODE, code);
        values.put(COLUMN_ENUM, enumValue);

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }
    public Cursor queryByCODE(String cODE) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_TIME, COLUMN_TO, COLUMN_GOOD, COLUMN_IN_OUT, COLUMN_CODE, COLUMN_ENUM};
        String selection = COLUMN_CODE + " = ?";
        String[] selectionArgs = {cODE};
        return db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }
    public Cursor queryById(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_TIME, COLUMN_TO, COLUMN_GOOD, COLUMN_IN_OUT, COLUMN_CODE, COLUMN_ENUM};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {id};
        return db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }
    public void clearAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public void deleteDataById(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }
    @SuppressLint("Range")
    public long sumColumn(String columnName) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT SUM(" + columnName + ") AS total FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        long sum = 0;
        if (cursor.moveToFirst()) {
            sum = cursor.getLong(cursor.getColumnIndex("total"));
        }

        cursor.close();
        db.close();
        return sum;
    }
    // 更新记录的方法
    public void updateTransaction(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        long id = item.getId();
        String time = item.getTime();
        String to = item.getTo();
        String good = item.getGood();
        double inOut = item.getIn_out();
        String code = item.getCode();
        String enumType = item.getEnumValue();

        String updateQuery = "UPDATE " + TABLE_NAME + " SET " +
                COLUMN_TIME + " = ?, " +
                COLUMN_TO + " = ?, " +
                COLUMN_GOOD + " = ?, " +
                COLUMN_IN_OUT + " = ?, " +
                COLUMN_CODE + " = ?, " +
                COLUMN_ENUM + " = ? " +
                "WHERE " + COLUMN_ID + " = ?";

        db.execSQL(updateQuery, new Object[]{time, to, good, inOut, code, enumType, id});
    }
}
