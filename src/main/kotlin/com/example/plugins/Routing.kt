package com.example.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
            Database.connect("jdbc:mysql://localhost:3306/scheme_app",
                driver = "com.mysql.cj.jdbc.Driver")

            transaction {
                // print sql to std-out
                addLogger(StdOutSqlLogger)

                SchemaUtils.create (Cities)

                // insert new city. SQL: INSERT INTO Cities (name) VALUES ('St. Petersburg')
                val stPeteId = Cities.insert {
                    it[name] = "St. Petersburg"
                } get Cities.id

                // 'select *' SQL: SELECT Cities.id, Cities.name FROM Cities
                println("Cities: ${Cities.selectAll()}")
            }

        }
    }
}

object Cities: IntIdTable() {
    val name = varchar("name", 50)
}
