package katsu

import org.h2.Driver
import org.h2.jdbcx.JdbcDataSource
import org.hibernate.jpa.HibernatePersistenceProvider
import java.net.URL
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.SharedCacheMode
import javax.persistence.Table
import javax.persistence.ValidationMode
import javax.persistence.spi.ClassTransformer
import javax.persistence.spi.PersistenceUnitInfo
import javax.persistence.spi.PersistenceUnitTransactionType
import javax.sql.DataSource


@Entity
@Table
data class Foo(
    @Id
    val name: String
)

fun main() {
//    val katsuUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    val katsuUrl = "jdbc:h2:~/katsu_test.db"

//    Class.forName("org.h2.Driver")
    val properties = Properties().apply {
//        put("javax.persistence.jdbc.driver", Driver::class.java.name)
        put("driver", Driver::class.java.name)

        // hibernate.connection.url  ||   javax.persistence.jdbc.url
//        put(AvailableSettings.URL, katsuUrl)
//        put(AvailableSettings.USER, "sa")
//        put(AvailableSettings.PASS, "")
        put("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
        put("hibernate.hbm2ddl.auto", "update")
    }
    val dataSource = JdbcDataSource()
    dataSource.setURL(katsuUrl)
    val managedClasses = listOf(Foo::class.java.name)
    val persistenceUnitInfo = PersistenceUnitInfoImpl(properties, managedClasses, dataSource)

    val emf = HibernatePersistenceProvider().createContainerEntityManagerFactory(persistenceUnitInfo, properties)
    val em = emf.createEntityManager()

//    em.transaction.begin()
//    em.persist(Foo("bar"))
//    em.transaction.commit()
    val foos = em.createQuery("from Foo", Foo::class.java).resultList
    println("fetched ${foos.size} foos.")

    em.close()
}

@Suppress("TooManyFunctions", "EmptyFunctionBlock")
class PersistenceUnitInfoImpl(
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
    override fun getMappingFileNames(): MutableList<String>  = mutableListOf()
    override fun addTransformer(transformer: ClassTransformer?) {}
    override fun getNewTempClassLoader(): ClassLoader? = null
    override fun getPersistenceUnitRootUrl(): URL? = null
    override fun getJtaDataSource(): DataSource?  = null

}
