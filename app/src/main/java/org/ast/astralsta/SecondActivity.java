package org.ast.astralsta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import static org.ast.astralsta.MainActivity.context;

@SuppressLint({"MissingInflatedId", "LocalSuppress", "Range"})
public class SecondActivity extends AppCompatActivity {
    private EditText textView;
    private EditText textView2;
    private EditText textView3;
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
        textView2.setText(inOut + "");
        textView3 = findViewById(R.id.editView7);
        textView3.setText(item.getGood_enum());
        Button button = findViewById(R.id.back);
        button.setOnClickListener(v -> {
            Intent intent = getIntent();
            String data = intent.getStringExtra("page");
            intent = new Intent(SecondActivity.this, MainActivity.class);

            intent.putExtra("page",data);
            startActivity(intent);
        });
        Button button2 = findViewById(R.id.change);
        button2.setOnClickListener(v2 -> {
            String good2 = textView.getText().toString();
            String inOut2 = textView2.getText().toString();
            item.setGood(good2);
            item.setIn_out(Double.parseDouble(inOut2));
            item.setGood_enum(textView3.getText().toString());
            dbHelper.updateTransaction(item);
            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
        });

    }
}
