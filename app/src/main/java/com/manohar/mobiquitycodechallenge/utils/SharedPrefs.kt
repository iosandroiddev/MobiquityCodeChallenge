@file:JvmName("SharedPreferenceExtension")


import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manohar.mobiquitycodechallenge.model.BookmarkLocationModel


/**
 * Extension to get & Create [SharedPreferences] Instance.
 */
private fun Context.getSharedPreferenceInstance(): SharedPreferences {
    return getSharedPreferences(packageName + "_preferences", Context.MODE_PRIVATE)
}

/**
 * Extension method to get [SharedPreferences.Editor].
 */
private fun Context.getSharedPreferenceInstanceEditor(): SharedPreferences.Editor {
    return getSharedPreferenceInstance().edit()
}

/**
 * Extension for retrieving a String value from [SharedPreferences]
 *
 * @param preferenceKey
 */
fun Context.getStringPreference(preferenceKey: String?): String? {
    return preferenceKey?.let {
        getSharedPreferenceInstance().getString(it, null)
    }
}

/**
 * Extension to write a String value synchronously to [SharedPreferences] with callback
 *
 * @param preferenceKey
 * @param preferenceValue
 * @return true if the new value was successfully written to persistent Storage [SharedPreferences]
 */
fun Context.setStringPreferenceWithCallback(
    preferenceKey: String?,
    preferenceValue: String?
): Boolean {
    return preferenceKey?.let {
        return if (it.isNotEmpty()) {
            getSharedPreferenceInstanceEditor().putString(it, preferenceValue).commit()
        } else {
            false
        }
    } ?: false
}

/**
 * Extension to write a String value asynchronously to [SharedPreferences] without callback
 *
 * @param preferenceKey
 * @param preferenceValue
 */
fun Context.setStringPreferenceAsynchronous(preferenceKey: String?, preferenceValue: String?) {
    preferenceKey?.let {
        if (it.isNotEmpty()) {
            getSharedPreferenceInstanceEditor().putString(it, preferenceValue).apply()
        }
    }
}

/**
 * Extension to write a Boolean value synchronously to [SharedPreferences] with callback
 *
 * @param preferenceKey
 * @param preferenceValue
 * @return true if the new value was successfully written to persistent Storage [SharedPreferences]
 */
fun Context.setBooleanPreferenceWithCallback(
    preferenceKey: String?,
    preferenceValue: Boolean
): Boolean {
    return preferenceKey?.let {
        return if (it.isNotEmpty()) {
            getSharedPreferenceInstanceEditor().putBoolean(it, preferenceValue).commit()
        } else {
            false
        }
    } ?: false
}

/**
 * Extension to write a Boolean value asynchronously to [SharedPreferences] without callback
 *
 * @param preferenceKey
 * @param preferenceValue
 */
fun Context.setBooleanPreferenceAsynchronous(preferenceKey: String?, preferenceValue: Boolean) {
    preferenceKey?.let {
        if (it.isNotEmpty()) {
            getSharedPreferenceInstanceEditor().putBoolean(it, preferenceValue).apply()
        }
    }
}

/**
 * Extension for retrieving a Boolean value from [SharedPreferences] with by default value to false
 *
 * @param preferenceKey
 */
fun Context.getBooleanPreference(preferenceKey: String?): Boolean {
    return preferenceKey?.let {
        getSharedPreferenceInstance().getBoolean(it, false)
    } ?: return false
}

/**
 * Extension for retrieving a Boolean value from [SharedPreferences] with a default value
 *
 * @param preferenceKey
 * @param defaultValue
 */
fun Context.getBooleanPreferenceWithDefaultValue(
    preferenceKey: String?,
    defaultValue: Boolean
): Boolean {
    return preferenceKey?.let {
        getSharedPreferenceInstance().getBoolean(it, defaultValue)
    } ?: return false
}

/**
 * Extension to write a Integer value asynchronously to [SharedPreferences] without callback
 *
 * @param preferenceKey
 * @param preferenceValue
 */
fun Context.setIntegerPreferenceAsynchronous(preferenceKey: String?, preferenceValue: Int) {
    preferenceKey?.let {
        if (it.isNotEmpty()) {
            getSharedPreferenceInstanceEditor().putInt(it, preferenceValue).apply()
        }
    }
}

/**
 * Extension for retrieving a Int value from [SharedPreferences] with by default value to -1
 *
 * @param preferenceKey
 */
fun Context.getIntPreference(preferenceKey: String?): Int {
    return preferenceKey?.let {
        getSharedPreferenceInstance().getInt(it, -1)
    } ?: return -1
}

/**
 * Extension for retrieving a Int value from [SharedPreferences] with a default value
 *
 * @param preferenceKey
 * @param defaultValue
 */
fun Context.getIntPreferenceWithDefaultValue(
    preferenceKey: String?,
    defaultValue: Int
): Int {
    return preferenceKey?.let {
        getSharedPreferenceInstance().getInt(it, defaultValue)
    } ?: return -1
}

/**
 * Extension to write a Float value asynchronously to [SharedPreferences] without callback
 *
 * @param preferenceKey
 * @param preferenceValue
 */
fun Context.setFloatPreferenceAsynchronous(preferenceKey: String?, preferenceValue: Float) {
    preferenceKey?.let {
        if (it.isNotEmpty()) {
            getSharedPreferenceInstanceEditor().putFloat(it, preferenceValue).apply()
        }
    }
}

/**
 * Extension for retrieving a Int value from [SharedPreferences] with by default value to -1
 *
 * @param preferenceKey
 */
fun Context.getFloatPreference(preferenceKey: String?): Float {
    return preferenceKey?.let {
        getSharedPreferenceInstance().getFloat(it, -1F)
    } ?: return -1F
}

/**
 * Extension for retrieving a Float value from [SharedPreferences] with a default value
 *
 * @param preferenceKey
 * @param defaultValue
 */
fun Context.getFloatPreferenceWithDefaultValue(
    preferenceKey: String?,
    defaultValue: Float
): Float {
    return preferenceKey?.let {
        getSharedPreferenceInstance().getFloat(it, defaultValue)
    } ?: return -1F
}

/**
 * Extension to write a Custom ArrayLists asynchronously to [SharedPreferences] without callback
 *
 * @param preferenceKey
 * @param preferenceValue
 */
fun Context.setCustomArrayListPreferenceAsynchronous(
    preferenceKey: String?,
    preferenceValue: ArrayList<BookmarkLocationModel>
) {
    preferenceKey?.let {
        if (it.isNotEmpty()) {
            getSharedPreferenceInstanceEditor().putString(it, Gson().toJson(preferenceValue))
                .apply()
        }
    }
}

/**
 * Extension to retrieve a Custom ArrayLists asynchronously to [SharedPreferences] without callback
 *
 * @param preferenceKey
 */
fun Context.getCustomArrayListPreferenceAsynchronous(preferenceKey: String?): ArrayList<BookmarkLocationModel>? {
    preferenceKey?.let {
        return if (it.isNotEmpty()) {
            val jsonResponse = getStringPreference(it) ?: ""
            if(jsonResponse.isEmpty()){
                null
            }else {
                val type = object : TypeToken<ArrayList<BookmarkLocationModel>>() {
                }.type
                Gson().fromJson(jsonResponse, type) as ArrayList<BookmarkLocationModel>
            }
        } else {
            null
        }
    } ?: return null
}

/**
 * Extension to clear all values from [SharedPreferences]
 *
 */
fun Context.clearPreferences() {
    getSharedPreferenceInstanceEditor().clear().apply()
}