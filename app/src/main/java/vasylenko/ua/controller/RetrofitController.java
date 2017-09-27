package vasylenko.ua.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vasylenko.ua.rest.NbuApiService;

import static retrofit2.converter.gson.GsonConverterFactory.create;

/**
 * Класс-контроллер для инициализации Retrofit.
 * @Created by Тёма on 21.09.2017.
 * @version 1.0
 */
public class RetrofitController {
    /** Базовая ссылка запроса. */
    static final private String  BASE_URL = "https://bank.gov.ua/NBUStatService/v1/";

    /**
     * Метод инициализирует Retrofit и задает способ парсинга JSONа.
     * @return готовый экземпляр реализации апишки.
     */
    public static NbuApiService getApi(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NbuApiService nbuApiService = retrofit
                .create(NbuApiService.class);

        return nbuApiService;
    }

}
