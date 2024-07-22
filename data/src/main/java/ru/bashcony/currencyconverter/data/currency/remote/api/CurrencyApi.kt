package ru.bashcony.currencyconverter.data.currency.remote.api

import androidx.annotation.Keep
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import ru.bashcony.currencyconverter.data.currency.remote.dto.CurrencyResponse
import ru.bashcony.currencyconverter.data.currency.remote.dto.PairConversionResponse

@Keep
interface CurrencyApi {

    @GET("latest/{currency}")
    fun getCurrencyInfo(
        @Path("currency") currency: String
    ): Single<CurrencyResponse>

    @GET("pair/{from}/{to}/{amount}")
    fun getPairConversion(
        @Path("from") currencyFrom: String,
        @Path("to") currencyTo: String,
        @Path("amount") amount: String
    ): Single<PairConversionResponse>
}