package org.ast.astralsta;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_FILE = 1;
    private DrawerLayout drawerLayout;
    private ListView navigationDrawer;

    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    public static Context context;
    private MyAdapter adapter;
    public static List<String> items = new ArrayList<>();
    public static List<Integer> itemsSSS = new ArrayList<>();
    List<Item> events = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        databaseHelper = new DatabaseHelper(this);
        // 初始化侧边菜单
        initNavigationDrawer();
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.add_item);
                Button btnPickFile = findViewById(R.id.btn_pick_file);
                btnPickFile.setOnClickListener(v2 -> pickFile());
                Button btn5 = findViewById(R.id.button5);
                btn5.setOnClickListener(v3 -> {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                });
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 初始化 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 设置返回箭头
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Button buttonSelectMonthYear = findViewById(R.id.button_select_month_year);

        buttonSelectMonthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthPicker();
            }
        });
        adapter = new MyAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        int spaceInPixels = 16; // 例如，16dp
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spaceInPixels));

        readData();

    }
    private void showMonthPicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 创建DatePickerDialog实例
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                // 因为我们只关心月份，这里可以忽略year和day
                String selectedMonthStr = String.format("%02d", (selectedMonth + 1));
                Toast.makeText(getApplicationContext(), "您选择了月份：" + selectedMonthStr, Toast.LENGTH_SHORT).show();
                Button btn = findViewById(R.id.button_select_month_year);
                btn.setText(selectedMonthStr);
                items.clear();
                itemsSSS.clear();
                int sum = 0;
                for (Item item : events) {
                    if (item.getDateTime().getMonthValue() == selectedMonth + 1) {
                        items.add(item.toGeShiHua());
                        itemsSSS.add(item.getId());
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemInserted(items.size() - 1);
                        sum += item.getIn_out();
                    }
                }
                TextView textView = findViewById(R.id.textView6);
                textView.setText("本月流水:" + sum);
            }
        }, year, month, day).show();
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
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
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
            if(fileContent.contains("微信支付账单明细列表")) {
                fileContent = fileContent.split("交易单号,商户单号,备注")[1];
                String data[] = fileContent.split("\n");
                int i = 0;
                int i2 = 0;
                for (String line2 : data) {
                    Log.d("File Content", line2);
                    try {
                        double a = Double.parseDouble(line2.split(",")[5].replace("¥",""));
                        if(line2.split(",")[4].equals("支出")) {
                            a = a * -1;
                        }
                        Item item = new Item(0, line2.split(",")[0], line2.split(",")[2], line2.split(",")[3],  a, line2.split(",")[8], line2.split(",")[6]);
                        Log.d("Item", item.toGeShiHua());
                        if(databaseHelper.queryByCODE(item.getCode()).getCount() == 0) {
                            i2 ++;
                            databaseHelper.addItem(item);
                            items.add(item.toGeShiHua());
                            itemsSSS.add(item.getId());
                            adapter.notifyItemInserted(items.size() - 1);
                        }else {
                            i ++;
                            Toast.makeText(this, "数据已存在", Toast.LENGTH_SHORT);
                        }
                    }catch (Exception e) {

                    }
                }
                Toast.makeText(this, "微信 已导入" + i2 + "条数据，跳过" + i + "条数据", Toast.LENGTH_SHORT).show();
            }else {
                InputStream inputStream2 = getContentResolver().openInputStream(uri);
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(inputStream2,"GBK"));
                content = new StringBuilder();
                line= "";
                while ((line = reader2.readLine()) != null) {
                    content.append(line).append("\n");
                }
                fileContent = content.toString();
                Log.d("File Content", fileContent);
                fileContent = fileContent.split(",交易订单号,商家订单号,备注,")[1];
                String data[] = fileContent.split("\n");
                int i = 0;
                int i2 = 0;
                for (String line2 : data) {
                    Log.d("File Content", line2);
                    try {
                        double a = Double.parseDouble(line2.split(",")[6].replace("¥",""));
                        if(line2.split(",")[5].equals("支出")) {
                            a = a * -1;
                        }
                        Item item = new Item(0, line2.split(",")[0], line2.split(",")[2], line2.split(",")[4],  a, line2.split(",")[9], line2.split(",")[7]);
                        Log.d("Item", item.toGeShiHua());
                        if(databaseHelper.queryByCODE(item.getCode()).getCount() == 0) {
                            i2 ++;
                            databaseHelper.addItem(item);
                            items.add(item.toGeShiHua());
                            itemsSSS.add(item.getId());
                            adapter.notifyItemInserted(items.size() - 1);
                        }else {
                            i ++;
                            Toast.makeText(this, "数据已存在", Toast.LENGTH_SHORT);
                        }
                    }catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                }
                Toast.makeText(this, "支付宝 已导入" + i2 + "条数据，跳过" + i + "条数据", Toast.LENGTH_SHORT).show();
                readData();
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
    public void readData() {
        items.clear();
        itemsSSS.clear();
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
                double inOut = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_IN_OUT));
                String code = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CODE));
                String enumValue = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ENUM));
                Item item = new Item(id, time, to, good, inOut, code, enumValue);
                events.add(item);
                // 使用 List.sort(Comparator) 排序





                // 处理每一行数据
                Log.d("Database", "ID: " + id + ", Time: " + time + ", To: " + to + ", Good: " + good + ", InOut: " + inOut + ", Code: " + code + ", Enum: " + enumValue);
            } while (cursor.moveToNext());
        }
        TextView textView = findViewById(R.id.textView6);
        textView.setText(String.valueOf(databaseHelper.sumColumn(DatabaseHelper.COLUMN_IN_OUT)));
        cursor.close();
        db.close();

        events.sort(Comparator.comparing(Item::getDateTime));

        // 输出排序后的结果
        for (Item item : events) {
            items.add(item.toGeShiHua());
            itemsSSS.add(item.getId());
            adapter.notifyItemInserted(items.size() - 1);
        }
    }
    private static String convertToUtf8(String encodedString) {
        try {
            Charset inputCharset = Charset.forName("GBK");
            byte[] bytes = encodedString.getBytes(inputCharset);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Error converting string: " + e.getMessage());
            return null;
        }
    }

}
