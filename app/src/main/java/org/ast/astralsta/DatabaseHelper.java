package org.ast.astralsta;
import android.content.ContentValues;
import android.content.Context;
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
    public long addItem(String time, String to, String good, int in_out, String code, String enumValue) {
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
}
