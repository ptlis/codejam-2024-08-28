package uk.org.fca.tinysubmarine

import java.io.File

class Main

fun main(args: Array<String>) {
    val vertexPairs = readVertexPairs("./data/rawdata")
    println(vertexPairs)

    val vertices = getVertices(vertexPairs)
    println(vertices)

    val vertexConnections = findVerticesConnections(vertices, vertexPairs)
    println(vertexConnections)
}

fun readVertexPairs(fileName: String): List<Pair<String, String>> {
    return File(fileName)
        .useLines { it.toList() }
        .map { it.split('-') }
        .map { Pair(it.first(), it.last()) }
}

fun getVertices(vertexPairs: List<Pair<String, String>>): List<String> {
    var verticies: MutableList<String> = mutableListOf()

    vertexPairs.forEach {
        verticies.add(it.first)
        verticies.add(it.second)
    }

    return verticies.distinct().toList()
}

fun findVerticesConnections(
    vertices: List<String>,
    vertexPairs: List<Pair<String, String>>
): Map<String, List<String>> {
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

    return vertexConnections.mapValues {
        key: String, connections: MutableList<String> -> (key -> connections.toList())
    }.toMap()
}