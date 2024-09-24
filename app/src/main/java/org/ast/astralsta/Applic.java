package org.ast.astralsta;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Applic extends Application {
    @Override
    public void  onTerminate() {
        super.onTerminate();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Toast.makeText(this, "onTerminate", Toast.LENGTH_SHORT).show();
        // 在应用退出时清除所有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // 清除所有数据
        editor.apply(); // 提交更改
    }
}
