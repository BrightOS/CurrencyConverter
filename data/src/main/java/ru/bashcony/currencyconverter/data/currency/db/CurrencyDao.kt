package ru.bashcony.currencyconverter.data.currency.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Single

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateCurrencies(currencies: List<CurrencyLocalModel>)

    @Query("SELECT * FROM currency_table")
    fun getAllCurrencies(): Single<List<CurrencyLocalModel>>
}