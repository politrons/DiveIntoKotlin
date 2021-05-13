package spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ServerBootable {

}

fun main(args: Array<String>) {
    runApplication<ServerBootable>(*args)
}