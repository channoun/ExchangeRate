package com.charbelhannoun.currencyexchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.charbelhannoun.currencyexchange.api.ExchangeService
import com.charbelhannoun.currencyexchange.api.model.ExchangeRates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeFragment : Fragment() {
    private var buyUsdTextView: TextView?=null
    private var sellUsdTextView: TextView?=null

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
}