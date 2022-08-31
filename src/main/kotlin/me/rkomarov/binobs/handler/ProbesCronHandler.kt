package me.rkomarov.binobs.handler

import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.rkomarov.binobs.config.currencies.HandlerConfiguration
import me.rkomarov.binobs.config.environment.EnvConfiguration
import me.rkomarov.binobs.config.environment.EnvConfigurationParameter
import me.rkomarov.binobs.config.environment.EnvConfigurationParameter.PROBES_CRON_EXPRESSION

class ProbesCronHandler: CronHandler(ProbesConfiguration, EnvConfiguration[PROBES_CRON_EXPRESSION], true) {
    private object ProbesConfiguration: HandlerConfiguration {
        override fun selfDescribe() = ""
    }

    override suspend fun handle() {
        withContext(Dispatchers.IO) {
            File(EnvConfiguration[EnvConfigurationParameter.LIVENESS_PROBES_FILE]).createNewFile()
        }
    }
}