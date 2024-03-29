package org.eduard.another.sql_orm

import java.sql.Connection
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * SQLManager é parecido com Hibernate porem com bem menos Features
 */
@Suppress("unused")
class SQLManager(var dbManager: DBManager) {
    enum class SQLAction {
        UPDATE, DELETE, INSERT
    }

    class DataChanged(
        val data: Any,
        val action: SQLAction,
        vararg val collumnsNames: String
    )

    val actions: Queue<DataChanged> = ConcurrentLinkedQueue()

    var changesLimitPerTime = 100

    fun runChanges(): Int {
        var amount = 0
        // val updatesDone = mutableListOf<Any>()
        // val deletesDone = mutableListOf<Any>()
        for (currentChange in 0 until changesLimitPerTime) {
            val dataChange = actions.poll() ?: break
            if (dataChange.action == SQLAction.INSERT) {
                insertData(dataChange.data)
            } else if (dataChange.action == SQLAction.UPDATE) {
                /*
                if (dataChange.collumnsNames.isEmpty()){
                    if (!updatesDone.contains(dataChange.data)) {
                        updatesDone.add(dataChange.data)
                    }else{
                        continue
                    }
                }
                 */
                updateData(dataChange.data, *dataChange.collumnsNames)
            } else if (dataChange.action == SQLAction.DELETE) {
                /*
                if (deletesDone.contains(dataChange.data)) {
                    continue
                }
                deletesDone.add(dataChange.data)
                 */
                deleteData(dataChange.data)
            }

            /*
            else if (dataChange.action == SQLAction.UPDATE_CACHE) {
                updateCache(dataChange.data)
            }
             */
            amount++
        }
        return amount
    }

    fun hasConnection(): Boolean {
        return dbManager.hasConnection()
    }

    /**
     *
     * @param primaryKeyValue
     * @param <E> dataType
     * @return
     */
    inline fun <reified E : Any> getData(primaryKeyValue: Any): E? {
        return getData(E::class.java, primaryKeyValue)
    }

    inline fun <reified E : Any> getDataOf(reference: Any): E? {
        return getDataOf(E::class.java, reference)
    }

    inline fun <reified E : Any> getDatasOf(reference: Any): MutableList<E> {
        return getDatasOf(E::class.java, reference)
    }

    /**
     *
     * @param dataClass
     * @param fieldName
     * @param fieldValue
     * @param <E>
     * @return
    </E> */
    fun <E : Any> getData(dataClass: Class<E>, fieldName: String, fieldValue: Any): E? {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .findByColumn(fieldName, fieldValue)
        } else null
    }


    fun <E : Any> getDataOf(dataClass: Class<E>, reference: Any): E? {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .findByReference(reference)
        } else null
    }


    fun <E : Any> getDatasOf(dataClass: Class<E>, reference: Any): MutableList<E> {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .selectByReference(reference)
        } else mutableListOf()
    }

    /**
     *
     * @param dataClass
     * @param primaryKeyValue
     * @param <E>
     * @return
    </E> */
    fun <E : Any> getData(dataClass: Class<E>, primaryKeyValue: Any): E? {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .findByPrimary(primaryKeyValue)
        } else null
    }

    inline fun <reified E : Any> getAll(): MutableList<E> {
        return getAllData(E::class.java)
    }

    fun <E : Any> getAllData(dataClass: Class<E>): MutableList<E> {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .selectAll()
        } else mutableListOf()
    }

    inline fun <reified E : Any> getSome(
        collums: String = "*",
        where: String = "",
        orderBy: String = "id",
        desc: Boolean = true,
        limit: Int = 10
    ): MutableList<E> {
        return getSome(E::class.java, collums, where, orderBy, desc, limit)
    }

    /**
     * Alias para getSome()
     */
    fun <E : Any> getAllData(
        dataClass: Class<E>,
        where: String,
        orderBy: String,
        desc: Boolean,
        limit: Int
    ): MutableList<E> {
        return getSome(dataClass, "*", where, orderBy, !desc, limit)
    }

    /**
     * Retorna alguns dados da tabela pesquisa mais complexa do que getAll()
     * @param dataClass Classe
     * @param where Where
     * @param orderBy Coluna de Ordenar
     * @param desc Se é Decrescente
     * @param limit Numero Limite
     * @param <E>
     * @return
    </E> */
    fun <E : Any> getSome(
        dataClass: Class<E>,
        collums: String,
        where: String,
        orderBy: String,
        desc: Boolean,
        limit: Int
    ): MutableList<E> {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .select(collums, where, orderBy, !desc, limit)
        } else mutableListOf()
    }

    fun <E : Any> insertData(data: E) {
        if (hasConnection()) {
            val dataClass = data.javaClass
            dbManager.engineUsed.getTable(dataClass)
                .insert(data)
        }
    }

    val connection: Connection
        get() = dbManager.connection

    /**
     *
     * @param data
     */
    fun <E : Any> updateDataQueue(data: E, vararg columnsNames: String) {
        actions.offer(DataChanged(data, SQLAction.UPDATE, *columnsNames))
    }

    /**
     *
     * @param data
     */
    fun <E : Any> deleteDataQueue(data: E) {
        actions.offer(DataChanged(data, SQLAction.DELETE))
    }

    /**
     *
     * @param data
     */
    fun <E : Any> insertDataQueue(data: E) {
        actions.offer(DataChanged(data, SQLAction.INSERT))
    }

    fun <T : Any> updateData(data: T) {
        return updateData(data, *arrayOf())
    }

    /**
     *
     * @param data
     * @param <T>
    </T> */
    fun <T : Any> updateData(data: T, vararg columnsNames: String) {
        if (hasConnection()) {

            val dataClass = data.javaClass
            dbManager.engineUsed.getTable(dataClass)
                .update(data, *columnsNames)

        }
    }

    fun <T : Any> deleteData(data: T) {
        if (hasConnection()) {
            dbManager.engineUsed.getTable(data.javaClass)
                .delete(data)

        }
    }

    inline fun <reified T : Any> createTable() {
        createTable(T::class.java)
    }

    fun <T : Any> createTable(dataClass: Class<T>) {
        if (hasConnection()) {
            dbManager.engineUsed.createTable(dataClass)
        }
    }

    inline fun <reified E : Any> deleteTable() {
        deleteTable(E::class.java)
    }

    fun <E : Any> deleteTable(dataClass: Class<E>) {
        if (hasConnection()) {
            dbManager.engineUsed.deleteTable(dataClass)
        }
    }

    inline fun <reified E : Any> clearTable() {
        clearTable(E::class.java)
    }


    fun <E : Any> clearTable(dataClass: Class<E>) {
        if (hasConnection()) {
            dbManager.engineUsed.clearTable(dataClass)
        }
    }

    inline fun <reified E : Any> deleteReferences() {
        deleteReferences(E::class.java)
    }


    fun <E : Any> deleteReferences(dataClass: Class<E>) {
        if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass).deleteReferences()
        }
    }

    inline fun <reified E : Any> createReferences() {
        createReferences(E::class.java)
    }


    fun <T : Any> createReferences(dataClass: Class<T>) {
        if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .createReferences()
        }
    }

    fun updateAllReferences() {
        if (hasConnection()) {
            dbManager.engineUsed.updateReferences()
        }
    }


    inline fun <reified E : Any> updateReferences() {
        if (hasConnection()) {
            dbManager.engineUsed.getTable(E::class.java)
                .updateReferences()
        }
    }

    fun updateCache(data: Any) {
        if (hasConnection()) {
            dbManager.engineUsed.updateCache(data)
        }
    }

    fun cacheInfo() {
        if (hasConnection()) {
            dbManager.engineUsed.cacheInfo()
        }
    }
}