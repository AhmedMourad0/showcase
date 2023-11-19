package dev.ahmedmourad.showcase.common.pickers.date.model

import androidx.compose.runtime.Immutable
import dev.ahmedmourad.showcase.common.*
import kotlin.jvm.JvmInline

@JvmInline
@Parcelize
@Immutable
value class DayOfMonth(val v: Int) : Parcelable
