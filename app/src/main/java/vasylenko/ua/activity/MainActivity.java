package vasylenko.ua.activity;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vasylenko.exchangeratesnbu.R;
import vasylenko.ua.adapter.ExchangeRateAdapter;
import vasylenko.ua.controller.RetrofitController;
import vasylenko.ua.model.ExchangeRateModel;
import vasylenko.ua.rest.NbuApiService;

/**
 * Главная активность проекта.
 * @Created by Тёма on 20.09.2017.
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /** Список моделей представления валют. */
    private ArrayList<ExchangeRateModel> responses = new ArrayList<ExchangeRateModel>();

    /** Получаем инстанс календаря. */
    private Calendar dateInstance = Calendar.getInstance();

    /** Шаблон форматированя даты. */
    private SimpleDateFormat dateFormatter;

    /** Поле для выбора даты. */
    private EditText dateEdit;

    /** Главный компонент списка отображения валют. */
    private ListView mainListView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.refresh_toolbar:
                Toast.makeText(MainActivity.this, "Оновлення...", Toast.LENGTH_SHORT).show();

                // Используем либу AndroidViewAnimations для анимации значка обновить.
                YoYo.with(Techniques.Swing)
                        .duration(2000)
                        .repeat(0)
                        .playOn(findViewById(R.id.refresh_toolbar)
                        );

                // Получаем значение даты в поле.
                final String dateField = dateEdit.getText().toString();

                // Получаем список валют по выбранной дате в отдельном потоке.
                new RetrieveFeedTask().execute(dateField);

                return true;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получаем компонень главного списка отображения валют.
        mainListView = (ListView) findViewById(R.id.main_listview);

        // Получаем поле выбора даты и по-умолчанию ставим на него фокус.
        dateEdit = (EditText) findViewById(R.id.date_edit);
        dateEdit.setInputType(InputType.TYPE_NULL);
        dateEdit.requestFocus();

        // Задаем шаблон отображения даты в поле выбора даты.
        dateFormatter = new SimpleDateFormat("yyyy.MM.dd");

        // Получаем текущую дату и устанавливаем ее в поле выбора даты.
        Date currentDate = Calendar.getInstance().getTime();
        String currentDateText = dateFormatter.format(currentDate);
        dateEdit.setText(currentDateText);

        // Получаем список валют по текущей дате в отдельном потоке.
        new RetrieveFeedTask().execute(currentDateText);

        /*// Обновляем список полученных валют.
        updateCurrencyList(completedBaseUrl(currentDate));*/
    }

     /*
    private static String completedBaseUrl(Date inputDate){
        * Статический метод генерации ссылки по указанной дате для выполнения запроса к сервису.
        * @param inputDate дата для генерации ссылки.
        * @return готовая ссылка с выбранной датой запроса.
        String dateFromTemplate = new SimpleDateFormat("yyyyMMdd").format(inputDate);

        String url = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?date="
                + dateFromTemplate + "&json";

        return url;
    }*/

    /**
     * Статический метод генерации ссылки по указанной дате для выполнения запроса к сервису.
     * @param inputDate дата для генерации ссылки.
     * @return готовая ссылка с выбранной датой запроса.
     */
    private static String completedBaseUrl(String inputDate){
        inputDate = clearDate(inputDate);
        String url = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?date="
                + inputDate + "&json";

        return url;
    }

    /**
     * Статистический метод преобразования строки даты к заданному формату даты в URL.
     * @param tempDate входная строка даты.
     * @return новая преобразованная строка даты.
     */
    private static String clearDate(String tempDate){
        tempDate = tempDate.replace(".","");
        return tempDate;
    }

    /**
     * Метод отображает диалоговое окно для выбора и установки новой даты при нажатии на поле даты.
     * @param view входная вьюшка.
     */
    public void setNewDate(View view) {
        new DatePickerDialog(MainActivity.this, dateSetListener,
                dateInstance.get(Calendar.YEAR),
                dateInstance.get(Calendar.MONTH),
                dateInstance.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    /** Установка обработчика выбора даты. */
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Запоминаем выбранную дату.
            dateInstance.set(Calendar.YEAR, year);
            dateInstance.set(Calendar.MONTH, monthOfYear);
            dateInstance.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Получаем выбранную дату и устанавливаем ее в поле выбора даты.
            Date chooseDate = dateInstance.getTime();
            String chooseDateText = dateFormatter.format(chooseDate);
            dateEdit.setText(chooseDateText);

            // Получаем список валют по выбранной дате в отдельном потоке.
            new RetrieveFeedTask().execute(chooseDateText);

            /* // Обновляем список полученных валют при выборе новой даты.
            updateCurrencyList(completedBaseUrl(chooseDate)); */
        }
    };

    /**
     * Метод обновляет список полученных валют по определенной дате.
     * @param inputUrl готовая ссылка с определенной датой для запроса.
     */
    private void updateCurrencyList(String inputUrl){
        // Во возникновение проблем с кодированием символа '&', юзаем HttpUrl
        HttpUrl url = HttpUrl.parse(inputUrl);

        NbuApiService nbuApiService = RetrofitController.getApi();
        nbuApiService.getCurrency(url.toString()).enqueue(new Callback<List<ExchangeRateModel>>() {

            @Override
            public void onResponse(Call<List<ExchangeRateModel>> call, Response<List<ExchangeRateModel>> response) {
                // Заполняем список моделями из JSONа, перед этим предварительно очистив его.
                responses.clear();
                responses.addAll(response.body());

                // Адаптер для конвертации списка полученных поделей во вьюшку.
                ExchangeRateAdapter adapter = new ExchangeRateAdapter(MainActivity.this, responses);
                adapter.notifyDataSetChanged();

                // Прикрепим созданный адаптер к ListView.
                ListView listView = (ListView) findViewById(R.id.main_listview);
                listView.setAdapter(adapter);

                /*
                call.request().url();
                Log.e("test1",  call.request().url()+"");
                response.code();
                Log.e("test2", response.code()+"");
                Log.e("test3", response.errorBody()+"");
                */
            }

            @Override
            public void onFailure(Call<List<ExchangeRateModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Помилка підключення до мережі!", Toast.LENGTH_LONG).show();
            }

        });
    }

    /**
     * Класс для работы с отдельным потоком.
     */
    class RetrieveFeedTask extends AsyncTask<String, String, Void> {
        // TODO: Необходимо проверить выполнение работы данного потока c методом updateCurrencyList
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Для визуализации обновления валют уменьшаем прозрачность списка и отключаем его.
            mainListView.setAlpha(0.5f);
            mainListView.setEnabled(false);
        }

        @Override
        protected Void doInBackground(String... params) {
            // Вызываем метод для обновления списка валют.
            updateCurrencyList(completedBaseUrl(params[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Возвращаем отображение компонента списка по-умолчанию.
            mainListView.setAlpha(1);
            mainListView.setEnabled(true);
        }

    }
}
