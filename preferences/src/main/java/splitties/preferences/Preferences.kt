/*
 * Copyright (c) 2017. Louis Cognault Ayeva Derman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package splitties.preferences

import android.content.Context
import android.content.SharedPreferences
import splitties.init.appCtx
import kotlin.reflect.KProperty

@Suppress("NOTHING_TO_INLINE")
abstract class Preferences(ctx: Context = appCtx, name: String, mode: Int = Context.MODE_PRIVATE) {
    protected val prefs: SharedPreferences = ctx.getSharedPreferences(name, mode)

    operator fun contains(o: Any) = prefs === o

    protected fun intPref(defaultValue: Int) = IntPrefProvider(defaultValue = defaultValue)
    protected fun floatPref(defaultValue: Float) = FloatPrefProvider(defaultValue = defaultValue)
    protected fun longPref(defaultValue: Long) = LongPrefProvider(defaultValue = defaultValue)
    protected fun stringPref(defaultValue: String? = null) = StringPrefProvider(defaultValue = defaultValue)
    protected fun stringSetPref(defaultValue: MutableSet<String>? = null) = StringSetPrefProvider(defaultValue = defaultValue)

    protected inner class BoolPref(val key: String, val defaultValue: Boolean) {
        inline operator fun setValue(thisRef: Preferences, prop: KProperty<*>, value: Boolean) {
            prefs.edit().putBoolean(key, value).apply()
        }

        inline operator fun getValue(thisRef: Preferences, prop: KProperty<*>): Boolean {
            return prefs.getBoolean(key, defaultValue)
        }
    }

    protected inner class IntPref(val key: String, val defaultValue: Int) {
        inline operator fun getValue(thisRef: Preferences, prop: KProperty<*>): Int {
            return prefs.getInt(key, defaultValue)
        }

        inline operator fun setValue(thisRef: Preferences, prop: KProperty<*>, value: Int) {
            prefs.edit().putInt(key, value).apply()
        }
    }

    protected inner class FloatPref(val key: String, val defaultValue: Float) {
        inline operator fun getValue(thisRef: Preferences, prop: KProperty<*>): Float {
            return prefs.getFloat(key, defaultValue)
        }

        inline operator fun setValue(thisRef: Preferences, prop: KProperty<*>, value: Float) {
            prefs.edit().putFloat(key, value).apply()
        }
    }

    protected inner class LongPref(val key: String, val defaultValue: Long) {
        inline operator fun getValue(thisRef: Preferences, prop: KProperty<*>): Long {
            return prefs.getLong(key, defaultValue)
        }

        inline operator fun setValue(thisRef: Preferences, prop: KProperty<*>, value: Long) {
            prefs.edit().putLong(key, value).apply()
        }
    }

    protected inner class StringPref(val key: String, val defaultValue: String? = null) {
        inline operator fun getValue(thisRef: Preferences, prop: KProperty<*>): String? {
            return prefs.getString(key, defaultValue)
        }

        inline operator fun setValue(thisRef: Preferences, prop: KProperty<*>, value: String?) {
            prefs.edit().putString(key, value).apply()
        }
    }

    protected inner class StringSetPref(val key: String,
                                        val defaultValue: MutableSet<String>? = null) {
        inline operator fun getValue(thisRef: Preferences, prop: KProperty<*>): MutableSet<String>? {
            return prefs.getStringSet(key, defaultValue)
        }

        inline operator fun setValue(thisRef: Preferences, prop: KProperty<*>, value: MutableSet<String>?) {
            prefs.edit().putStringSet(key, value).apply()
        }
    }

    abstract class PrefProvider(private val key: String) {
        protected fun getKey(prop: KProperty<*>) = if (key === PROP_NAME) prop.name else key
    }

    protected inner class BoolPrefProvider(val key: String = PROP_NAME, val defaultValue: Boolean)
        : PrefProvider(key) {
        inline operator fun provideDelegate(thisRef: Preferences, prop: KProperty<*>): BoolPref {
            return BoolPref(getKey(prop), defaultValue)
        }
    }

    protected inner class FloatPrefProvider(val key: String = PROP_NAME, val defaultValue: Float)
        : PrefProvider(key) {
        inline operator fun provideDelegate(thisRef: Preferences, prop: KProperty<*>): FloatPref {
            return FloatPref(getKey(prop), defaultValue)
        }
    }

    protected inner class LongPrefProvider(val key: String = PROP_NAME, val defaultValue: Long)
        : PrefProvider(key) {
        inline operator fun provideDelegate(thisRef: Preferences, prop: KProperty<*>): LongPref {
            return LongPref(getKey(prop), defaultValue)
        }
    }

    protected inner class IntPrefProvider(val key: String = PROP_NAME, val defaultValue: Int)
        : PrefProvider(key) {
        inline operator fun provideDelegate(thisRef: Preferences, prop: KProperty<*>): IntPref {
            return IntPref(getKey(prop), defaultValue)
        }
    }

    protected inner class StringPrefProvider(val key: String = PROP_NAME, val defaultValue: String? = null)
        : PrefProvider(key) {
        inline operator fun provideDelegate(thisRef: Preferences, prop: KProperty<*>): StringPref {
            return StringPref(getKey(prop), defaultValue)
        }
    }

    protected inner class StringSetPrefProvider(val key: String = PROP_NAME, val defaultValue: MutableSet<String>? = null)
        : PrefProvider(key) {
        inline operator fun provideDelegate(thisRef: Preferences, prop: KProperty<*>): StringSetPref {
            return StringSetPref(getKey(prop), defaultValue)
        }
    }
}

private val PROP_NAME = "__splitties__"
