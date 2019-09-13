package katsu

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isInstanceOf
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import com.google.common.collect.Lists
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testng.annotations.Test

@Test
class LoggingTest {
    fun `When configured DEV logging Then console appender added`() {
        Logging.configure(Environment.DEV)

        val appenders = allAppenders()
        assertThat(appenders).hasSize(1)
        assertThat(appenders[0]).isInstanceOf(ConsoleAppender::class)
    }

    fun `When configured PROD logging Then file appender added`() {
        Logging.configure(Environment.PROD)

        val appenders = allAppenders()
        assertThat(appenders).hasSize(1)
        assertThat(appenders[0]).isInstanceOf(FileAppender::class)
    }

    private fun allAppenders(): List<Appender<ILoggingEvent>> {
        val context = LoggerFactory.getILoggerFactory() as LoggerContext
        val rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME)
        return Lists.newArrayList(rootLogger.iteratorForAppenders())
    }
}
