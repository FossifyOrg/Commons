package org.fossify.commons.compose.extensions

import android.content.Context
import org.fossify.commons.helpers.BaseConfig

val Context.config: BaseConfig get() = BaseConfig.newInstance(applicationContext)
