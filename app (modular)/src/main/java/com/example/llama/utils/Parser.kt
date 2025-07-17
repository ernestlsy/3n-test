package com.example.llama.utils

object Parser {

    fun parseFields(fieldNames: String): List<String> {
        return fieldNames.split(',').map() { string -> string.trim()}
    }

}
