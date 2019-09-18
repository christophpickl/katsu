package katsu.persistence

import org.h2.Driver
import org.h2.jdbcx.JdbcDataSource
import org.hibernate.jpa.HibernatePersistenceProvider
import java.io.File
import java.net.URL
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.SharedCacheMode
import javax.persistence.ValidationMode
import javax.persistence.spi.ClassTransformer
import javax.persistence.spi.PersistenceUnitInfo
import javax.persistence.spi.PersistenceUnitTransactionType
import javax.sql.DataSource
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

object HibernateOpener {
    fun open(config: HibernateConfig): EntityManager {
        val persistenceUnitInfo = createPersistenceUnitInfo(config)
        val emf = HibernatePersistenceProvider().createContainerEntityManagerFactory(persistenceUnitInfo, Properties())
        return emf.createEntityManager()
    }

    private fun createPersistenceUnitInfo(config: HibernateConfig): PersistenceUnitInfo {
        val properties = Properties().apply {
            put("driver", Driver::class.java.name)
            put("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
            put("hibernate.hbm2ddl.auto", "update")
        }
        val dataSource = JdbcDataSource()
        dataSource.setURL(config.connection.dataSourceUrl)
        val managedClasses = config.managedClasses.map { it.jvmName }
        return PersistenceUnitInfoImpl(properties, managedClasses, dataSource)
    }
}

class HibernateConfig(
    val connection: HibernateConnection,
    val managedClasses: List<KClass<*>>
)

sealed class HibernateConnection {
    abstract val dataSourceUrl: String

    class InMemoryConnection(name: String) : HibernateConnection() {
        override val dataSourceUrl = "jdbc:h2:mem:$name;DB_CLOSE_DELAY=-1"
    }

    class FileConnection(db: File) : HibernateConnection() {
        override val dataSourceUrl = "jdbc:h2:${db.canonicalPath}"
    }
}

@Suppress("TooManyFunctions", "EmptyFunctionBlock")
private class PersistenceUnitInfoImpl(
    private val properties: Properties,
    private val managedClasses: List<String>,
    private val dataSource: DataSource
) : PersistenceUnitInfo {
    companion object {
        const val UNIT_NAME = "KatsuPersistence"
        private const val JPA_VERSION = "2.1"
    }

    override fun getNonJtaDataSource() = dataSource
    override fun getPersistenceProviderClassName(): String = HibernatePersistenceProvider::class.java.name
    override fun getPersistenceUnitName() = UNIT_NAME
    override fun getProperties() = properties
    override fun getManagedClassNames(): MutableList<String> = managedClasses.toMutableList()
    override fun getTransactionType() = PersistenceUnitTransactionType.RESOURCE_LOCAL
    override fun getPersistenceXMLSchemaVersion() = JPA_VERSION

    override fun getValidationMode() = ValidationMode.AUTO
    override fun getSharedCacheMode() = SharedCacheMode.UNSPECIFIED
    override fun getClassLoader(): ClassLoader = Thread.currentThread().contextClassLoader
    override fun excludeUnlistedClasses(): Boolean = false
    override fun getJarFileUrls(): MutableList<URL> = mutableListOf()
    override fun getMappingFileNames(): MutableList<String> = mutableListOf()
    override fun addTransformer(transformer: ClassTransformer?) {}
    override fun getNewTempClassLoader(): ClassLoader? = null
    override fun getPersistenceUnitRootUrl(): URL? = null
    override fun getJtaDataSource(): DataSource? = null

}
