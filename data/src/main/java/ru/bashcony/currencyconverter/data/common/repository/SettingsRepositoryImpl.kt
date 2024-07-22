package ru.bashcony.currencyconverter.data.common.repository

import android.content.SharedPreferences
import ru.bashcony.currencyconverter.data.infra.utils.delegates
import ru.bashcony.currencyconverter.domain.common.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    preferences: SharedPreferences
) : SettingsRepository {
    override var timeNextUpdateUnix: Long by preferences.delegates.long(0L, TIME_NEXT_UPDATE)

    companion object {
        private const val TIME_NEXT_UPDATE = "cc_time_next_update_unix"
    }
}