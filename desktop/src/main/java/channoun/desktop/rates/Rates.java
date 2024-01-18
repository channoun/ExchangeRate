package channoun.desktop.rates;

import channoun.desktop.api.ExchangeService;
import channoun.desktop.api.model.ExchangeRates;
import channoun.desktop.api.model.Transaction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rates {
    public Label buyUsdRateLabel;
    public Label sellUsdRateLabel;
    public TextField lbpTextField;
    public TextField usdTextField;
    public ToggleGroup transactionType;

    public void initialize() {
        fetchRates();
    }

    private void fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(new Callback<ExchangeRates>() {
             @Override
             public void onResponse(Call<ExchangeRates> call,
                                    Response<ExchangeRates> response) {
                 ExchangeRates exchangeRates = response.body();
                 Platform.runLater(() -> {
                     buyUsdRateLabel.setText(exchangeRates.lbpToUsd.toString());
                     sellUsdRateLabel.setText(exchangeRates.usdToLbp.toString());
                 });
             }
             @Override
             public void onFailure(Call<ExchangeRates> call, Throwable
                     throwable) {
             }
         });
    }

    public void addTransaction(ActionEvent actionEvent) {
        Transaction transaction = new Transaction(
                Float.parseFloat(usdTextField.getText()),
                Float.parseFloat(lbpTextField.getText()),
                ((RadioButton)
                        transactionType.getSelectedToggle()).getText().equals("Sell USD")
        );

        ExchangeService.exchangeApi().addTransaction(transaction, null).enqueue(new Callback<Object>() {
          @Override
          public void onResponse(Call<Object> call, Response<Object>
                  response) {
              fetchRates();
              Platform.runLater(() -> {
                  usdTextField.setText("");
                  lbpTextField.setText("");
              });
          }
          @Override
          public void onFailure(Call<Object> call, Throwable throwable)
          {
          }
      });
    }
}