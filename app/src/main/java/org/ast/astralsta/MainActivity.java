package org.ast.astralsta;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
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
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_FILE = 1;
    private DrawerLayout drawerLayout;
    private ListView navigationDrawer;

    private RecyclerView recyclerView;
    public static DatabaseHelper databaseHelper;
    public static Context context;
    private MyAdapter adapter;
    public static Vector<String> items = new Vector<>();
    public static Vector<Integer> itemsSSS = new Vector<>();
    public static ConcurrentHashMap<String,Item> itemMap = new ConcurrentHashMap<>();
    private Vector<Item> events = new Vector<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_main);
        context = this;
        databaseHelper = new DatabaseHelper(this);
        // 初始化侧边菜单
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.add_item);
                Button btnPickFile = findViewById(R.id.btn_pick_file);
                btnPickFile.setOnClickListener(v2 -> pickFile());
                Button btn5 = findViewById(R.id.button5);
                Button button3 = findViewById(R.id.button3);
                Button buttonSelectMonthYear2 = findViewById(R.id.button_select_month_year2);
                final Calendar calendar = Calendar.getInstance();
                final int[] mYear = {calendar.get(Calendar.YEAR)};
                final int[] mMonth = {calendar.get(Calendar.MONTH)};
                final int[] mDay = {calendar.get(Calendar.DAY_OF_MONTH)};
                buttonSelectMonthYear2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "请选择日期", Toast.LENGTH_SHORT).show();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // 当对话框中的日期被选择时，将选中的日期显示在TextView中
                                mYear[0] = year;
                                mMonth[0] = monthOfYear;
                                mDay[0] = dayOfMonth;
                                Toast.makeText(MainActivity.this, "选中的日期：" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, Toast.LENGTH_SHORT).show();
                            }
                        }, mYear[0], mMonth[0], mDay[0]);
                        datePickerDialog.show();
                    }
                });
                button3.setOnClickListener(v4 -> {
                    try {
                        EditText godd = findViewById(R.id.editView7);
                        String good = godd.getText().toString();
                        EditText su = findViewById(R.id.editView8);
                        String jiage = su.getText().toString();
                        EditText en = findViewById(R.id.editView9);
                        String enu = en.getText().toString();
                        int a = (mMonth[0] + 1);
                        String b = "";
                        if(a < 10) {
                            b = "0" + a;
                        }else {
                            b = String.valueOf(a);
                        }
                        String time = mYear[0] + "-" + b + "-" + mDay[0] + " 10:10:10";
                        Item item = new Item(0, time, "未知", good, Double.parseDouble(jiage),String.valueOf(generateRandomIntUsingMath(1000000,200000000)) , enu, "默认");
                        Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        databaseHelper.addItem(item);
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                    }catch (Exception e) {
                        Toast.makeText(MainActivity.this, "请输入正确的数据", Toast.LENGTH_SHORT).show();
                    }

                });


                btn5.setOnClickListener(v3 -> {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                });
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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
    public static int generateRandomIntUsingMath(int min, int max) {
        return (int)(Math.random() * (max - min + 1)) + min;
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
                adapter.notifyDataSetChanged();
                double sum = databaseHelper.sumInOutByMonth( selectedYear +"-" + selectedMonthStr);
                double add = 0;
                double scr = 0;
                String temp = "";
                for (Item item : events) {
                    if (item.getTime().contains("-" + selectedMonthStr + "-")) {
                        items.add(item.getTime());
                        itemsSSS.add(item.getId());
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemInserted(items.size() - 1);
                        if(item.getIn_out() < 0) {
                            add += item.getIn_out();
                        }else {
                            scr += item.getIn_out();
                        }
                    }
                }
                TextView textView = findViewById(R.id.textView6);
                textView.setText("本月收支 " + sum + "\n本月记录 " + items.size());
                TextView textView2= findViewById(R.id.textView2);
                textView2.setText("收入 " + scr + "\n支出 " + add);
            }
        }, year, month, day).show();
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
                        Item item = new Item(0, line2.split(",")[0], line2.split(",")[2], line2.split(",")[3],  a, line2.split(",")[8], line2.split(",")[6],"未知");
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
                        Item item = new Item(0, line2.split(",")[0], line2.split(",")[2], line2.split(",")[4],  a, line2.split(",")[9], line2.split(",")[7],"未知");
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
    @SuppressLint({"Range", "SetTextI18n", "NotifyDataSetChanged"})
    public void readData() {
        items.clear();
        itemsSSS.clear();
        events.clear();
        adapter.notifyDataSetChanged();
        // 获取可读的数据库实例
        Cursor cursor = databaseHelper.que();
        double a = 0;
        double add = 0;
        double sca = 0;
        if (cursor.moveToFirst()) {
            do {
                a++;
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME));
                String to = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TO));
                String good = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOOD));
                good = good.replace("\"","");
                double inOut = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_IN_OUT));
                String code = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CODE));
                String enumValue = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ENUM));
                String goodenum = "未知";
                try {
                    goodenum = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOODENUM));
                }catch (Exception e) {
                    Item item = new Item(0, time, to, good, inOut, code, enumValue,"未知");
                    databaseHelper.updateTransaction(item);
                }
                if(inOut > 0) {
                    add += inOut;
                }
                if(inOut < 0) {
                    sca += inOut;
                }
                Item item = new Item(id, time, to, good, inOut, code, enumValue, goodenum);

                items.add(item.getTime());
                itemsSSS.add(item.getId());
                adapter.notifyDataSetChanged();
                events.add(item);
                itemMap.put(item.getTime(), item);
                // 处理每一行数据
                Log.d("Database", "ID: " + id + ", Time: " + time + ", To: " + to + ", Good: " + good + ", InOut: " + inOut + ", Code: " + code + ", Enum: " + enumValue);
            } while (cursor.moveToNext());
        }
        TextView textView = findViewById(R.id.textView6);
        textView.setText("总收支 "+ String.valueOf(databaseHelper.sumColumn(DatabaseHelper.COLUMN_IN_OUT)) + "\n总共 " + a + " 条记录");
        TextView textView7 = findViewById(R.id.textView2);
        textView7.setText("支出 " + String.valueOf(sca).replace("-","") + "\n收入 " + String.valueOf(add));
        cursor.close();

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
