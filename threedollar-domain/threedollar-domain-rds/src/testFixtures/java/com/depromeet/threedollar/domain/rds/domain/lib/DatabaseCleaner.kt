package com.depromeet.threedollar.domain.rds.domain.lib

import org.hibernate.Session
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Connection
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

private const val REFERENTIAL_INTEGRITY_SQL = "SET REFERENTIAL_INTEGRITY"
private const val TRUNCATE_TABLE_SQL = "TRUNCATE TABLE"

@Component
class DatabaseCleaner : InitializingBean {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private lateinit var tableNames: List<String>

    override fun afterPropertiesSet() {
        entityManager.unwrap(Session::class.java)
            .doWork { connection -> extractTableNames(connection) }
    }

    private fun extractTableNames(connection: Connection) {
        val tableNames = mutableListOf<String>()
        val tables = connection.metaData.getTables(connection.catalog, null, "%", arrayOf("TABLE"))
        while (tables.next()) {
            tableNames.add(tables.getString("table_name"))
        }
        this.tableNames = tableNames
    }

    @Transactional
    fun cleanUp() {
        entityManager.unwrap(Session::class.java)
            .doWork { connection -> cleanUpDatabase(connection) }
    }

    private fun cleanUpDatabase(connection: Connection) {
        entityManager.flush()
        val statement = connection.createStatement()
        statement.executeUpdate("$REFERENTIAL_INTEGRITY_SQL FALSE")
        for (tableName in tableNames) {
            statement.executeUpdate("$TRUNCATE_TABLE_SQL $tableName")
        }
        statement.executeUpdate("$REFERENTIAL_INTEGRITY_SQL TRUE")
    }

}
