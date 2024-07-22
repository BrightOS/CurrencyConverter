package ru.bashcony.currencyconverter.data.common.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.bashcony.currencyconverter.data.common.repository.SettingsRepositoryImpl
import ru.bashcony.currencyconverter.domain.common.SettingsRepository

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {

    @Provides
    @Reusable
    fun providePreferences(@ApplicationContext context: Context): SettingsRepository =
        SettingsRepositoryImpl(
            context.getSharedPreferences(
                "currency_converter_preferences",
                Context.MODE_PRIVATE
            )
        )
}