package ru.netology.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object IntDelegate: ReadWriteProperty<Bundle, Int?> {

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Int?) {
        thisRef.putSerializable(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Int? =
        thisRef.getSerializable(property.name) as? Int
}