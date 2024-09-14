package org.ast.astralsta;

import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Item  implements Comparable<Item> {
    private int id;
    private String time;
    private String to;
    private String good;
    private double in_out;
    private String code;
    private String enumValue;
    private LocalDateTime dateTime;
    public Item(int id, String time, String to, String good, double in_out, String code, String enumValue) {
        this.id = id;
        this.time = time;
        this.to = to;
        this.good = good;
        this.in_out = in_out;
        this.code = code;
        this.enumValue = enumValue;
        Log.d("Item", "Item created with time: " + time);
        try {
            this.dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        }catch (Exception e) {
            this.dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public void setIn_out(double in_out) {
        this.in_out = in_out;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int compareTo(Item other) {
        return this.dateTime.compareTo(other.dateTime); // 根据日期时间排序
    }
    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getTo() {
        return to;
    }

    public String getGood() {
        return good;
    }

    public double getIn_out() {
        return in_out;
    }

    public String getCode() {
        return code;
    }

    public String getEnumValue() {
        return enumValue;
    }

    public String toGeShiHua() {
        if (in_out > 0) {
            return "收入" + good +"+" + in_out;
        }
        return "商品" + good + "\n 价格:" + in_out;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", to='" + to + '\'' +
                ", good='" + good + '\'' +
                ", in_out=" + in_out +
                ", code='" + code + '\'' +
                ", enumValue='" + enumValue + '\'' +
                '}';
    }
}
