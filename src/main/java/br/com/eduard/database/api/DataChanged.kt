package br.com.eduard.database.api

class DataChanged(
    val data: Any,
    val action: SQLAction,
    vararg val collumnsNames: String
)