package org.ast.astralsta;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import static org.ast.astralsta.MainActivity.context;

@SuppressLint({"MissingInflatedId", "LocalSuppress", "Range"})
public class SecondActivity extends AppCompatActivity {

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
        double inOut = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_IN_OUT));
        String code = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CODE));
        String enumValue = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ENUM));
        Item item = new Item(id, time, to, good, inOut, code, enumValue);
        EditText textView = findViewById(R.id.textView);
        textView.setText(item.getGood());
        EditText textView2 = findViewById(R.id.textView5);
        textView2.setText(inOut + "");



    }
}
