package org.ast.astralsta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import static org.ast.astralsta.MainActivity.context;
import static org.ast.astralsta.MainActivity.itemPos;

@SuppressLint({"MissingInflatedId", "LocalSuppress", "Range"})
public class SecondActivity extends AppCompatActivity {
    private EditText textView;
    private EditText textView2;
    private EditText textView3;
    private int po;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item2);
        int id = getIntent().getIntExtra("itemId",0);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.queryById(String.valueOf(id));
        cursor.moveToFirst();
        String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME));
        String to = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TO));
        String good = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOOD));
        good = good.replace("\"","");
        double inOut = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_IN_OUT));
        String code = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CODE));
        String enumValue = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ENUM));
        String enumalue = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOODENUM));
        Item item = new Item(id, time, to, good, inOut, code, enumValue, enumalue);
        textView = findViewById(R.id.textView);
        textView.setText(item.getGood());
        textView2 = findViewById(R.id.textView5);
        textView2.setText((inOut + "").replace("-", ""));
        textView3 = findViewById(R.id.editView7);
        textView3.setText(item.getGood_enum());
        TextView textView4 = findViewById(R.id.textView4);
        Switch switch1 = findViewById(R.id.switch2);
        TextView textView7 = findViewById(R.id.textView3);
        if (item.getIn_out() < 0) {
            switch1.setChecked(true);
        }
        if (switch1.isChecked()) {
            switch1.setText("支出");
            textView7.setText("商品");
            textView4.setText("出");
        }else {
            switch1.setText("收入");
            textView7.setText("来源");
            textView4.setText("入");
        }
        switch1.setOnClickListener(v5 -> {
            if (switch1.isChecked()) {
                switch1.setText("支出");
                textView7.setText("商品");
                textView4.setText("出");
            } else {
                switch1.setText("收入");
                textView7.setText("来源");
                textView4.setText("入");
            }
        });
        Button button = findViewById(R.id.back);
        Intent intent2 = getIntent();
        po = intent2.getIntExtra("po",0);
        button.setOnClickListener(v -> {
            Intent intent = getIntent();
            String data = intent.getStringExtra("page");
            intent = new Intent(SecondActivity.this, MainActivity.class);

            Log.d("po",po+"");
            intent.putExtra("po",po);
            intent.putExtra("page",data);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        Button button2 = findViewById(R.id.change);
        button2.setOnClickListener(v2 -> {
            String good2 = textView.getText().toString();
            String inOut2 = textView2.getText().toString();
            if (switch1.isChecked()) {
                inOut2 = "-" + inOut2;
            }
            item.setGood(good2);
            item.setIn_out(Double.parseDouble(inOut2));
            item.setGood_enum(textView3.getText().toString());
            dbHelper.updateTransaction(item);
            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
        });

    }
}
