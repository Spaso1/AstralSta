package org.ast.astralsta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.ast.astralsta.MainActivity.*;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<String> data;

    public MyAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        Log.d("TAG", "onCreateViewHolder: " );
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        holder.itemView.findViewById(R.id.divider).setVisibility(View.GONE);
        Item a =itemMap.get(data.get(position));
        String time = a.getTime();
        holder.textView.setText(a.toGeShiHua());
        if(a.getIn_out() < 0) {
            holder.textVie2.setTextColor(R.color.color_red);
        }
        holder.textVie2.setText(time);
    }
    public static String convertTimeFormat(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd");
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");
        SimpleDateFormat outputFormat2= new SimpleDateFormat("MM");
        try {
            Date date = inputFormat.parse(inputDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // 获取星期几
            String dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
            // 获取月份
            String month = outputFormat2.format(date);
            // 获取 MM-dd 格式的日期
            String mmdd = outputFormat.format(date);

            return dayOfWeek + " " + mmdd +  " " + month+ "月";
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid Date";
        }
    }

    /**
     * 将 Calendar.DAY_OF_WEEK 值转换为中文星期几
     *
     * @param dayOfWeek Calendar.DAY_OF_WEEK 值
     * @return 星期几的中文表示
     */
    private static String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "星期日";
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
            default:
                return "未知";
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView textView;
        TextView textVie2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textVie2 = itemView.findViewById(R.id.textViewRRR);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // 获取点击项的数据
                        String itemData = items.get(position);
                        int itemId = itemsSSS.get(position);
                        // 跳转到新的Activity
                        Intent intent = new Intent(context, SecondActivity.class);
                        intent.putExtra("itemData", itemData);
                        intent.putExtra("itemId", itemId);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}
