package com.neo.vault.utils.extension

import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.util.*

val Context.locale: Locale get() = ConfigurationCompat.getLocales(resources.configuration)[0]