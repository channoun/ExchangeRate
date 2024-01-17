package com.charbelhannoun.currencyexchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import com.charbelhannoun.currencyexchange.api.ExchangeService
import com.charbelhannoun.currencyexchange.api.model.ExchangeRates
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeFragment : Fragment() {
    private var buyUsdTextView: TextView?=null
    private var sellUsdTextView: TextView?=null
    private var convertAmount: TextView?=null
    private var inputField: TextInputEditText?=null
    private var radioGroup: RadioGroup?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchRates()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_exchange,
            container, false)
        buyUsdTextView = view.findViewById(R.id.txtBuyUsdRate)
        sellUsdTextView = view.findViewById(R.id.txtSellUsdRate)
        convertAmount = view.findViewById(R.id.convAmount)
        radioGroup = view.findViewById(R.id.rdGrpTransactionType)
        inputField = view.findViewById(R.id.txtInptExchangeAmount)
        inputField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                convertRate()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        radioGroup?.setOnCheckedChangeListener { group, checkedId ->  convertRate()}
        return view

    }

    private fun fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(object :
            Callback<ExchangeRates> {
            override fun onResponse(call: Call<ExchangeRates>, response:
            Response<ExchangeRates>
            ) {
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

    private fun convertRate() {
        var radioSelection = radioGroup?.checkedRadioButtonId
        if (inputField?.text.toString() == ""){
            convertAmount?.text = "0"
        }
        else if (radioSelection == R.id.rdBtnBuyUsd && buyUsdTextView?.text.toString() != "") {
            convertAmount?.text = (inputField?.text.toString().toFloat() * buyUsdTextView?.text.toString().toFloat()).toString()
        } else if (radioSelection == R.id.rdBtnSellUsd && sellUsdTextView?.text.toString() != ""){
            convertAmount?.text = (inputField?.text.toString().toFloat() * sellUsdTextView?.text.toString().toFloat()).toString()
        }
    }
}