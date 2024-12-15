package us.nineworlds.serenity.core.util

import android.content.SharedPreferences
import androidx.annotation.NonNull
import androidx.annotation.Nullable

class StringPreference(
  private val preferences: SharedPreferences,
  private val key: String,
  private val defaultValue: String?
) {

  val isSet: Boolean
    get() = preferences.contains(key)


  fun get(): String? {
    return preferences.getString(key, defaultValue)
  }

  fun set(value: String) {
    preferences.edit().putString(key, value).commit()
  }

  fun delete() {
    preferences.edit().remove(key).apply()
  }
}