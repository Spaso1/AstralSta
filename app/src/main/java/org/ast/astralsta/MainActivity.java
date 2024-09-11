package org.ast.astralsta;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_FILE = 1;
    private DrawerLayout drawerLayout;
    private ListView navigationDrawer;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> items;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化侧边菜单
        initNavigationDrawer();

        Button btnPickFile = findViewById(R.id.btn_pick_file);
        btnPickFile.setOnClickListener(v -> pickFile());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        items = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4", "Item 5");
        adapter = new MyAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    private void initNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationDrawer = findViewById(R.id.navigation_drawer);

        // 设置侧边菜单项
        String[] items = {"Home", "Settings", "About"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        navigationDrawer.setAdapter(adapter);

        // 处理侧边菜单项点击事件
        navigationDrawer.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    // Handle Home
                    break;
                case 1:
                    // Handle Settings
                    break;
                case 2:
                    // Handle About
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        });
    }

    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // 允许选择任何类型的文件
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                readAndDisplayFileContent(uri);
            }
        }
    }

    private void readAndDisplayFileContent(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            String fileContent = content.toString();
            fileContent = fileContent.split("交易单号,商户单号,备注")[1];
            String data[] = fileContent.split("\n");
            for (String line2 : data) {
                Log.d("File Content", line2);//line2就是初步数据
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private String readFile() {
        StringBuilder content = new StringBuilder();
        try {
            FileInputStream fis = openFileInput("data.prop");
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            content.append(new String(buffer, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "读取文件失败", Toast.LENGTH_SHORT).show();
            return "null";

        }
        return content.toString();

    }

    private void writeToFile(String data) {
        try {
            FileOutputStream fos = openFileOutput("data.prop", MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
            Toast.makeText(this, "数据已写入文件" , Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "写入文件失败", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("Range")
    public void writeData() {
        // 获取可读的数据库实例
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();

        // 查询数据
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME, // 表名
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_TIME, DatabaseHelper.COLUMN_TO, DatabaseHelper.COLUMN_GOOD, DatabaseHelper.COLUMN_IN_OUT, DatabaseHelper.COLUMN_CODE, DatabaseHelper.COLUMN_ENUM}, // 列名
                null, // 选择条件
                null, // 选择参数
                null, // 分组
                null, // 有分组时过滤
                null // 排序
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME));
                String to = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TO));
                String good = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOOD));
                int inOut = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_IN_OUT));
                String code = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CODE));
                String enumValue = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ENUM));
                Item item = new Item(id, time, to, good, inOut, code, enumValue);
                items.add(item.toGeShiHua());
                // 处理每一行数据
                Log.d("Database", "ID: " + id + ", Time: " + time + ", To: " + to + ", Good: " + good + ", InOut: " + inOut + ", Code: " + code + ", Enum: " + enumValue);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }
}
