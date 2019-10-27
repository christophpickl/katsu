package katsu

@Suppress("SimplifyBooleanWithConstants")
object Konfig {
    val enableHibernateLog = Environment.isDev && false
}
