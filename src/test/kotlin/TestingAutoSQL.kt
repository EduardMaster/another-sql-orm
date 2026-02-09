import br.com.eduard.database.AutoSQL
import br.com.eduard.database.DatabaseManager
import br.com.eduard.database.annotations.ColumnPrimary
import br.com.eduard.database.annotations.TableName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import java.util.Properties
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestingAutoSQL {
    fun log(testMessage: String) {
        println("[TestingAutoSQL] $testMessage")
    }
    @TableName("peoples")
    class People{
        @ColumnPrimary
        var id: Long = 0
        var name : String = "Jonh"
        var age : Int = 20

        /**
         * Must have an Empty Constructor
         */
        constructor(){

        }
        constructor(name : String, age : Int){
            this.name = name
            this.age = age
        }
    }

    lateinit var database: DatabaseManager
    lateinit var auto : AutoSQL
    @Test
    @Order(1)
    fun openDatabaseConnectionTest(){
        log("Getting database credentials...")
        val properties = Properties()

        val inputStream = javaClass.getResourceAsStream("./.env")
            ?: throw RuntimeException("File '.env' not found in resources folder!")

        inputStream.use { properties.load(it) }

        val user = properties.getProperty("USER")
        val pass = properties.getProperty("PASSWORD")
        log("Credentials available!")
        log("Trying to connnect database...")
        val manager = DatabaseManager(user,pass,"","localhost")
        manager.openConnection()
        assert(manager.hasConnection())
        this.database = manager;
        this.auto = AutoSQL(manager)
    }

    @Test
    @Order(2)
    fun createDatabaseExample(){
        log("Creating a database...")
        val newDatabaseName = "example";
        assert(this.database.createDatabase(newDatabaseName))
        log("Database example created!")
        log("Selecting same database...")
        assert(this.database.useDatabase(newDatabaseName));
        log("Database example selected!")
    }
    @Test
    @Order(3)
    fun createTableAutomatic(){
        log("Creating table People automatic...")
        this.database.engineUsed.createTable(People::class.java)
        val table = this.database.engineUsed.getTable(People::class.java);
        assert(table.created);
        log("Table People created!");

    }

    @Test
    @Order(4)
    fun insertTableNewPeopleAutomatic(){
        log("Getting table People")

        val peoples = this.auto.getAll<People>()
        val table = this.auto.getTable<People>();
        if (peoples.isEmpty()) {
            val jonhPerson = People()
            log("Inserting new Person John")
            this.auto.insertDataQueue(jonhPerson)
            this.auto.runChanges()

            // table.selectAll()
            assert(table.elements.isNotEmpty());
            log("Data John People Added!");
        } else {
            val jonh = this.auto.getData<People>("name","John");
            assertNull(jonh, "John must not be null")
            log("John finded!")
        }
    }

    @Test
    @Order(10)
    fun closeDatabaseConnectionTest(){
        this.database.closeConnection()
        assertTrue { !this.database.hasConnection() }
        log("Closed database connection!")
    }
}
