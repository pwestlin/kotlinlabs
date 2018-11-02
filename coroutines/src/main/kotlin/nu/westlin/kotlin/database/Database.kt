package nu.westlin.kotlin.database

class Database(val name: String) {
    private val tables = mutableListOf<DatabaseTable>()

    fun <T> createTable(name: String) {
        if(tables.find { it.name == name } != null) throw RuntimeException("Table $name already exist")

        tables.add(DatabaseTable(name))
    }

    fun listTablenames() = tables.map { it.name }.sorted()
    // TODO petves: Overloading
    fun getTable(name: String) = tables.find { it.name == name }
    //fun <T> getTable(name: String, type: T) = tables.find { it.name == name && it is String }
    fun insert(tablename: String, key: String, data: Any) {
        getTable(tablename)?.addRow(DatabaseTableRow(key, data))
    }
}

class DatabaseTable(val name: String) {
    var rows = mutableListOf<DatabaseTableRow<Any>>()

    fun addRow(row: DatabaseTableRow<Any>) {
        rows.add(row)
    }
}

class DatabaseTableRow<T>(val key: String, val data: T)