package katsu

enum class Environment(
    val cliArgValue: String
) {
    PROD("prod"),
    DEV("dev");

    companion object {
        private val default = DEV

        val current = System.getProperty("katsu.env", "").toLowerCase().let { propertyValue ->
            values().find { it.cliArgValue == propertyValue } ?: default
        }

        val isProd = current == PROD
        val isDev = current == DEV
    }
}