package us.nineworlds.serenity.core.logger

interface Logger {

  fun initialize()
  fun debug(message: String)
  fun error(message: String)
  fun error(message: String, error: Throwable)
  fun warn(message: String)
  fun info(message: String)
}