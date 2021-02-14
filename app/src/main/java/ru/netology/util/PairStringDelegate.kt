package ru.netology.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object PairStringDelegate: ReadWriteProperty<Bundle, Pair<String, String?>?> {

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Pair<String, String?>?) {
        thisRef.putSerializable(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Pair<String, String?>? =
        thisRef.getSerializable(property.name) as? Pair<String, String?>
}