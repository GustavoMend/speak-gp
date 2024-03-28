package org.teslasoft.assistant.preferences

import android.content.Context
import java.time.Instant
import java.time.format.DateTimeFormatter

class Logger {
    companion object {
        /**
         * Get crash log
         * */
        fun getCrashLog(context: Context) : String {
            return EncryptedPreferences.getEncryptedPreference(context, "logs", "crash")
        }

        /**
         * Set crash log
         * */
        private fun setCrashLog(context: Context, log: String) {
            EncryptedPreferences.setEncryptedPreference(context, "logs", "crash", log)
        }

        /**
         * Clear crash log
         * */
        fun clearCrashLog(context: Context) {
            setCrashLog(context, "")
        }

        /**
         * Get event log
         * */
        fun getEventLog(context: Context) : String {
            return EncryptedPreferences.getEncryptedPreference(context, "logs", "event")
        }

        /**
         * Set event log
         * */
        private fun setEventLog(context: Context, log: String) {
            EncryptedPreferences.setEncryptedPreference(context, "logs", "event", log)
        }

        /**
         * Clear event log
         * */
        fun clearEventLog(context: Context) {
            setEventLog(context, "")
        }

        /**
         * Get ads log
         * */
        fun getAdsLog(context: Context) : String {
            return EncryptedPreferences.getEncryptedPreference(context, "logs", "ads")
        }

        /**
         * Set ads log
         * */
        private fun setAdsLog(context: Context, log: String) {
            EncryptedPreferences.setEncryptedPreference(context, "logs", "ads", log)
        }

        /**
         * Clear ads log
         * */
        fun clearAdsLog(context: Context) {
            setAdsLog(context, "")
        }

        /**
         * @param type - type of log (crash/event/ads)
         * @param tag - any tag to identify log message and source
         * @param level - log level (info/error/warning/debug/verbose)
         * @param message - log message
         * */
        fun log(context: Context, type: String, tag: String, level: String, message: String) {
            val installationId = DeviceInfoProvider.getInstallationId(context)

            // If installation ID is zero it means user revoked authorization to collect user data
            // All logs will be skipped
            if (installationId != "00000000-0000-0000-0000-000000000000") {
                if (level == "info" || level == "error" || level == "warning" || level == "debug" || level == "verbose") {
                    val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
                    val logString =
                        "[$timestamp] [$installationId] [$tag] [${level.uppercase()}] $message\n"
                    when (type) {
                        "crash" -> {
                            val log = "${getCrashLog(context)}$logString"
                            setCrashLog(context, log)
                        }

                        "event" -> {
                            val log = "${getEventLog(context)}$logString"
                            setEventLog(context, log)
                        }

                        "ads" -> {
                            val log = "${getAdsLog(context)}$logString"
                            setAdsLog(context, log)
                        }

                        else -> {
                            error("Invalid log type")
                        }
                    }
                } else {
                    error("Invalid log level")
                }
            }
        }

        /**
         * Delete all logs
         * */
        fun deleteAllLogs(context: Context) {
            clearCrashLog(context)
            clearEventLog(context)
            clearAdsLog(context)
        }
    }
}