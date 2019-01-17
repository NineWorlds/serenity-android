package us.nineworlds.serenity.core.util

import android.content.SharedPreferences
import android.support.annotation.NonNull
import android.support.annotation.Nullable

class StringPreference(
  @NonNull private val preferences: SharedPreferences,
  @NonNull private val key: String,
  @Nullable private val defaultValue: String
) {

  val isSet: Boolean
    get() = preferences.contains(key)

  @Nullable
  fun get(): String {
    return preferences.getString(key, defaultValue)
  }

  fun set(@Nullable value: String) {
    preferences.edit().putString(key, value).commit()
  }

  fun delete() {
    preferences.edit().remove(key).apply()
  }
}