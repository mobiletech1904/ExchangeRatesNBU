package vasylenko.ua.model;

import com.google.gson.annotations.SerializedName;

/**
 * Класс-модель POJO описывающий предоставляемую сервисом структуру.
 * @Created by Тёма on 21.09.2017.
 * @version 1.0
 */
public class ExchangeRateModel {
    @SerializedName("r030")
    private int id;

    @SerializedName("txt")
    private String name;

    @SerializedName("rate")
    private double valueRate;

    @SerializedName("cc")
    private String shortName;

    @SerializedName("exchangedate")
    private String date;

    public ExchangeRateModel(int id, String name, double valueRate, String shortName, String date) {
        this.id = id;
        this.name = name;
        this.valueRate = valueRate;
        this.shortName = shortName;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValueRate() {
        return valueRate;
    }

    public void setValueRate(double valueRate) {
        this.valueRate = valueRate;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
