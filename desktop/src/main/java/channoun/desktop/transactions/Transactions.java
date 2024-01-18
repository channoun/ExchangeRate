package channoun.desktop.transactions;

import channoun.desktop.Authentication;
import channoun.desktop.api.ExchangeService;
import channoun.desktop.api.model.Transaction;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;
public class Transactions implements Initializable {
    public TableColumn lbpAmount;
    public TableColumn usdAmount;
    public TableColumn addedDate;
    public TableView tableView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbpAmount.setCellValueFactory(new
                PropertyValueFactory<Transaction, Float>("lbpAmount"));
        usdAmount.setCellValueFactory(new
                PropertyValueFactory<Transaction, Float>("usdAmount"));
        addedDate.setCellValueFactory(new
                PropertyValueFactory<Transaction, String>("addedDate"));
        ExchangeService.exchangeApi().getTransactions("Bearer " +
                        Authentication.getInstance().getToken())
                .enqueue(new Callback<List<Transaction>>() {
                    @Override
                    public void onResponse(Call<List<Transaction>> call,
                                           Response<List<Transaction>> response) {
                        tableView.getItems().setAll(response.body());
                    }
                    @Override
                    public void onFailure(Call<List<Transaction>> call,
                                          Throwable throwable) {
                    }
                });
    }
}
