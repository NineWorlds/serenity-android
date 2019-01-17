package us.nineworlds.serenity.core.logger

import timber.log.Timber

class TimberLogger : Logger {

  override fun initialize() {
    Timber.plant(Timber.DebugTree())
  }

  override fun debug(message: String) {
    Timber.d(message)
  }

  override fun error(message: String) {
    Timber.e(message)
  }

  override fun error(message: String, error: Throwable) {
    Timber.e(message, error)
  }

  override fun warn(message: String) {
    Timber.w(message)
  }

  override fun info(message: String) {
    Timber.i(message)
  }
}