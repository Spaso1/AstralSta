package org.ast.astralsta;

public class Item {
    private int id;
    private String time;
    private String to;
    private String good;
    private double in_out;
    private String code;
    private String enumValue;

    public Item(int id, String time, String to, String good, double in_out, String code, String enumValue) {
        this.id = id;
        this.time = time;
        this.to = to;
        this.good = good;
        this.in_out = in_out;
        this.code = code;
        this.enumValue = enumValue;
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
