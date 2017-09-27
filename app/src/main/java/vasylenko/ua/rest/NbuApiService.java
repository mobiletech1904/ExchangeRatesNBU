package vasylenko.ua.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import vasylenko.ua.model.ExchangeRateModel;

/**
 * Интерфейс описания операций апишки.
 * @Created by Тёма on 21.09.2017.
 * @version 1.0
 */
public interface NbuApiService {
    // TODO: Первые два метода предствлены для тестирования возможности обращения к сервису и их можно будет удалить.
    @GET("statdirectory/exchange")
    Call<List<ExchangeRateModel>> getExchangeRate();

    @GET("statdirectory/exchange")
    Call<List<ExchangeRateModel>>  getExchangeRate(
            @Query(value = "date", encoded = true) String inputDate
    );

    /**
     * Описываем метод получения списка моделей валют.
     * В данном случае возникла проблема правильного кодирования символа '&' в ссылке запроса данных к сервису.
     * Вопрос был решен с помощью stackoverflow.com. Ссылка на варианты решения прилагается.
     * https://stackoverflow.com/questions/46277907/retrofit-2-query-encoded-false-dont-work/46279804#46279804
     */
    @GET
    Call<List<ExchangeRateModel>>  getCurrency(
            @Url String url
    );
}
