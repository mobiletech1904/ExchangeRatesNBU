package vasylenko.ua.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vasylenko.exchangeratesnbu.R;
import vasylenko.ua.model.ExchangeRateModel;

/**
 * Адаптер кастомного списка.
 * @Created by Тёма on 20.09.2017.
 * @version 1.0
 */
public class ExchangeRateAdapter extends ArrayAdapter<ExchangeRateModel> {

    public ExchangeRateAdapter(Context context,  ArrayList<ExchangeRateModel> dataList) {
        super(context, 0, dataList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_main_listview, parent, false);
        }

        // Получаем данные по позиции.
        ExchangeRateModel exchangeRateModel = getItem(position);

        // Получаем названия текстовых полей для заполнения данными.
        TextView currencyNameText = (TextView) convertView.findViewById(R.id.currency_name);
        TextView currencyValueText = (TextView) convertView.findViewById(R.id.currency_value);

        // Устанавливаем значение данного поля из текущей полученной модели
        currencyNameText.setText(exchangeRateModel.getName()+"");
        currencyValueText.setText(exchangeRateModel.getValueRate()+"");

        return convertView;
    }

}
