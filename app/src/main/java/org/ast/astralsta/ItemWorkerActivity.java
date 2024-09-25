package org.ast.astralsta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemWorkerActivity extends AppCompatActivity {
    @Override
    @SuppressLint({"MissingInflatedId", "Range"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_worker);

        TextView textView = findViewById(R.id.yusuan);
        textView.setOnClickListener(v ->{
            showDatePickerDialog();
        });
        TextView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ItemWorkerActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }



    private void showDatePickerDialog() {
        // 创建自定义布局
        LinearLayout datePickerLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.date_picker_layout, null);

        // 获取 DatePicker 和 EditText
        final DatePicker datePicker = (DatePicker) datePickerLayout.findViewById(R.id.datePicker);
        final EditText etMonthlyBudget = (EditText) datePickerLayout.findViewById(R.id.etMonthlyBudget);

        Date date = new Date();

        // 使用SimpleDateFormat定义日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        // 将Date对象转换为指定格式的字符串
        String formattedDate = sdf.format(date);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String monthlyBudget = sharedPreferences.getString(formattedDate,"");
        etMonthlyBudget.setText(monthlyBudget);

        // 监听 EditText 的变化
        // 设置日期改变监听器
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                year = datePicker.getYear();
                month = datePicker.getMonth();
                String date;
                if(month < 10) {
                    date = year + "-0" + (month + 1);
                }else {
                    date = year + "-" + (month + 1);
                }
                String monthlyBudget = sharedPreferences.getString(date,"");
                if (monthlyBudget.isEmpty()) {
                    etMonthlyBudget.setText("");
                } else {
                    etMonthlyBudget.setText(monthlyBudget);
                }
            }
        });
        // 创建 AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择月份并填写预算")
                .setView(datePickerLayout)
                .setPositiveButton("也是关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                            }
                })
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        etMonthlyBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文本变化前
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文本变化时
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 文本变化后
                try {
                    double monthlyBudget = Double.parseDouble(s.toString());
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int day = datePicker.getDayOfMonth();
                    String date;
                    if(month < 10) {
                        date = year + "-0" + (month + 1);
                    }else {
                        date = year + "-" + (month + 1);
                    }
                    editor.putString(date,s.toString());
                    editor.commit();
                }catch (Exception e) {
                    Toast.makeText(ItemWorkerActivity.this, "请输入正确的数字", Toast.LENGTH_SHORT).show();
                    s.clear();
                }
            }
        });
        // 显示对话框
        Dialog dialog = builder.create();
        dialog.show();
    }
}
