package com.charbelhannoun.currencyexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import com.charbelhannoun.currencyexchange.api.ExchangeService
import com.charbelhannoun.currencyexchange.api.model.ExchangeRates
import com.charbelhannoun.currencyexchange.api.model.Transaction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var buyUsdTextView: TextView?=null
    private var sellUsdTextView: TextView?=null
    private var fab: FloatingActionButton? = null
    private var transactionDialog: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buyUsdTextView = findViewById(R.id.txtBuyUsdRate)
        sellUsdTextView = findViewById(R.id.txtSellUsdRate)

        fab = findViewById(R.id.fab)
        fab?.setOnClickListener { view ->
            showDialog()
        }

        fetchRates()
    }
    private fun showDialog() {
        transactionDialog = LayoutInflater.from(this)
            .inflate(R.layout.dialog_transaction, null, false)
        MaterialAlertDialogBuilder(this).setView(transactionDialog)
            .setTitle("Add Transaction")
            .setMessage("Enter transaction details")
            .setPositiveButton("Add") { dialog, _ ->
                val usdAmount =
                    transactionDialog?.findViewById<TextInputLayout>(R.id.txtInptUsdAmount)?.editText?.text.toString().toFloat()
                val lbpAmount =
                    transactionDialog?.findViewById<TextInputLayout>(R.id.txtInptLbpAmount)?.editText?.text.toString().toFloat()
                val radioSelection =
                    transactionDialog?.findViewById<RadioGroup>(R.id.rdGrpTransactionType)?.checkedRadioButtonId
                if (radioSelection == -1) {
                    throw IllegalStateException("No button is selected")
                }
                val usdToLbp = radioSelection == R.id.rdBtnSellUsd
                var transaction = Transaction()
                transaction.lbpAmount = lbpAmount
                transaction.usdAmount = usdAmount
                transaction.usdToLbp = usdToLbp
                addTransaction(transaction)
                dialog.dismiss()
                fetchRates()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(object :
            Callback<ExchangeRates> {
            override fun onResponse(call: Call<ExchangeRates>, response:
            Response<ExchangeRates>) {
                val responseBody: ExchangeRates? = response.body();
                if (responseBody != null) {
                    val usdToLbp = responseBody.usdToLbp
                    val lbpToUsd = responseBody.lbpToUsd
                    buyUsdTextView?.apply {text = lbpToUsd.toString()}
                    sellUsdTextView?.apply {text = usdToLbp.toString()}
                }
            }
            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                return;
                TODO("Not yet implemented")
            }
        })
    }
    private fun addTransaction(transaction: Transaction) {
        ExchangeService.exchangeApi().addTransaction(transaction).enqueue(object :
            Callback<Any> {
            override fun onResponse(call: Call<Any>, response:
            Response<Any>) {
                Snackbar.make(fab as View, "Transaction added!",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Snackbar.make(fab as View, "Could not add transaction.",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
        })
    }
}