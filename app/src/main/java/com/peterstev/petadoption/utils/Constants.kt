package com.peterstev.petadoption.utils

import java.util.regex.Pattern

const val PAGE_KEY = "page_key"
const val TABLE_NAME = "adoption_table"
const val DATABASE_NAME = "adoption_db"

const val YES_NO = "yesno"
const val TEXT = "text"
const val PHOTO = "embeddedphoto"
const val DATE_TIME = "datetime"
const val NUMERIC = "formattednumeric"

val PATTERN = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)