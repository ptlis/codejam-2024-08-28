package uk.org.fca.tinysubmarine

import java.io.File

class Main

fun main(args: Array<String>) {
    val vertexPairs = readVertexPairs("./data/rawdata")
    println(vertexPairs)

    val vertexConnections = findVerticesConnections(vertexPairs)
    println(vertexConnections)
}

fun readVertexPairs(fileName: String): List<Pair<String, String>> {
    return File(fileName)
        .useLines { it.toList() }
        .map { it.split('-') }
        .map { Pair(it.first(), it.last()) }
}

fun findVerticesConnections(vertexPairs: List<Pair<String, String>>): Map<String, List<String>> {
    val vertexConnections: MutableMap<String, MutableList<String>> = mutableMapOf();

    vertexPairs.forEach {
        if (!vertexConnections.containsKey(it.first)) {
            vertexConnections[it.first] = mutableListOf(it.second)
        } else {
            vertexConnections[it.first]?.add(it.second)
        }

        if (!vertexConnections.containsKey(it.second)) {
            vertexConnections[it.second] = mutableListOf(it.first)
        } else {
            vertexConnections[it.second]?.add(it.first)
        }
    }

    return vertexConnections.mapValues {it.value.toList()}.toMap()
}