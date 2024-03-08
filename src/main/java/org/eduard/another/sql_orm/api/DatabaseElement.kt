package org.eduard.another.sql_orm.api

import org.eduard.another.sql_orm.SQLManager

interface DatabaseElement {

    val sqlManager : SQLManager

    fun insert(){
        sqlManager.insertData(this )
    }
    fun insertQueue(){
        sqlManager.insertDataQueue(this )
    }
    fun delete(){
        sqlManager.deleteData(this )
    }
    fun deleteQueue(){
        sqlManager.deleteDataQueue(this )
    }
    fun update(){
        sqlManager.updateData(this,*arrayOf() )
    }

    fun updateOnly(vararg collumnsNames : String){
        sqlManager.updateData(this,*collumnsNames )
    }

    fun updateQueue(){
        sqlManager.updateDataQueue(this, *arrayOf())
    }

    fun updateOnlyQueue(vararg columnsNames: String){
        sqlManager.updateDataQueue(this, *columnsNames)
    }

    fun updateCache(){
        sqlManager.updateCache(this)
    }


}