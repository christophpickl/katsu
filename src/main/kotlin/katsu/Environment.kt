package katsu

import mu.KotlinLogging.logger

enum class Environment(
    val cliArgValue: String
) {
    PROD("prod"),
    DEV("dev"),
    UI_TEST("ui_test");

    companion object {
        private val log = logger {}

        val current: Environment = System.getProperty("katsu.env", "").toLowerCase().let { propertyValue ->
            values().find { it.cliArgValue == propertyValue }?.also { log.info { "Detected environment: $it" } }
                ?: throw IllegalStateException("Invalid katsu.env set: '$propertyValue'")
        }

        val isProd = current == PROD
    }
}
