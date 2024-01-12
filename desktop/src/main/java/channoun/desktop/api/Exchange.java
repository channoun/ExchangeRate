package channoun.desktop.api;

import channoun.desktop.api.model.ExchangeRates;
import channoun.desktop.api.model.Transaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface Exchange {
    @GET("/exchangeRate")
    Call<ExchangeRates> getExchangeRates();

    @POST("/transaction")
    Call<Object> addTransaction(@Body Transaction transaction);
}
