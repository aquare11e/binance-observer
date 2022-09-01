package me.rkomarov.binobs.handler

import dev.inmo.krontab.doInfinity
import me.rkomarov.binobs.config.currencies.HandlerConfiguration
import me.rkomarov.binobs.config.environment.EnvConfiguration
import me.rkomarov.binobs.config.environment.EnvConfigurationParameter
import mu.KotlinLogging

abstract class CronHandler(
    protected open val configuration: HandlerConfiguration,
    private val cronExpression: String?,
    private val runOnceOnStart: Boolean
) {
    protected abstract suspend fun handle(): Unit

    suspend fun start() {
        if (runOnceOnStart) {
            logger.debug { "${javaClass.simpleName}(${configuration.selfDescribe()}) executed once on start" }
            safeHandle()
        }

        doInfinity(cronExpression()) { safeHandle() }
    }

    private suspend fun safeHandle() {
        try {
            handle()
        } catch (e: Exception) {
            logger.error(e) { "Exception happens" }
        }
    }

    private val logger = KotlinLogging.logger {}
    private fun cronExpression() = cronExpression ?: EnvConfiguration[EnvConfigurationParameter.OFFERS_REQUESTS_CRON_EXPRESSION]
}