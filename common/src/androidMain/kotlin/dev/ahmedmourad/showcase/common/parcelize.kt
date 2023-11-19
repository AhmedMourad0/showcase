package dev.ahmedmourad.showcase.common

import android.os.Parcel
import kotlinx.datetime.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

actual typealias Parcelize = kotlinx.parcelize.Parcelize
actual typealias Parcelable = android.os.Parcelable
actual typealias Parceler<T> = kotlinx.parcelize.Parceler<T>
actual typealias TypeParceler<T, P> = kotlinx.parcelize.TypeParceler<T, P>
actual typealias IgnoredOnParcel = kotlinx.parcelize.IgnoredOnParcel

actual object InstantParceler : Parceler<Instant?> {
    override fun create(parcel: Parcel): Instant? {
        val millis = parcel.readString()?.toLongOrNull() ?: return null
        return Instant.fromEpochMilliseconds(millis)
    }
    override fun Instant?.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this?.toEpochMilliseconds()?.toString())
    }
}

actual object LocalDateTimeParceler : Parceler<LocalDateTime?> {
    override fun create(parcel: Parcel): LocalDateTime? {
        val millis = parcel.readString()?.toLongOrNull() ?: return null
        return Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC)
    }
    override fun LocalDateTime?.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this?.toInstant(TimeZone.UTC)?.toEpochMilliseconds()?.toString())
    }
}

actual object LocalDateParceler : Parceler<LocalDate?> {
    override fun create(parcel: Parcel): LocalDate? {
        val millis = parcel.readString()?.toLongOrNull() ?: return null
        return Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date
    }
    override fun LocalDate?.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this?.atTime(0, 0)
            ?.toInstant(TimeZone.UTC)
            ?.toEpochMilliseconds()
            ?.toString()
        )
    }
}

actual object LocalTimeParceler : Parceler<LocalTime?> {
    override fun create(parcel: Parcel): LocalTime? {
        if (parcel.readInt() == -1) return null
        return LocalTime(
            hour = parcel.readInt(),
            minute = parcel.readInt(),
            second = parcel.readInt(),
            nanosecond = parcel.readInt()
        )
    }
    override fun LocalTime?.write(parcel: Parcel, flags: Int) {
        if (this == null) {
            parcel.writeInt(-1)
            return
        } else {
            parcel.writeInt(1)
            parcel.writeInt(this.hour)
            parcel.writeInt(this.minute)
            parcel.writeInt(this.second)
            parcel.writeInt(this.nanosecond)
        }
    }
}

actual object DurationParceler : Parceler<Duration?> {
    override fun create(parcel: Parcel): Duration? {
        val seconds = parcel.readString()?.toLongOrNull() ?: return null
        return seconds.seconds
    }
    override fun Duration?.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this?.inWholeSeconds?.toString())
    }
}

actual object MonthParceler : Parceler<Month?> {
    override fun create(parcel: Parcel): Month? {
        if (parcel.readInt() == -1) return null
        return Month(parcel.readInt())
    }
    override fun Month?.write(parcel: Parcel, flags: Int) {
        if (this == null) {
            parcel.writeInt(-1)
            return
        } else {
            parcel.writeInt(1)
            parcel.writeInt(this.value)
        }
    }
}
