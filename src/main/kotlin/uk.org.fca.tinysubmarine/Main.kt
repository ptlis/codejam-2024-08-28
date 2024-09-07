package uk.org.fca.tinysubmarine

import java.io.File

class Main

fun main(args: Array<String>) {
    val vertexPairs = readVertexPairs("./data/rawdata")
    val adjacents = buildAdjacentsMap(vertexPairs)
    val paths = findPaths(adjacents, "start", "end")

    paths.forEach { println(it) }
    println("${paths.count()} unique roots found between start and end")
}

fun readVertexPairs(fileName: String): List<Pair<String, String>> {
    return File(fileName)
        .useLines { it.toList() }
        .map { it.split('-') }
        .map { Pair(it.first(), it.last()) }
}

fun buildAdjacentsMap(vertexPairs: List<Pair<String, String>>): Map<String, List<String>> {
    val adjacents: MutableMap<String, MutableList<String>> = mutableMapOf();

    vertexPairs.forEach {
        if (!adjacents.containsKey(it.first)) {
            adjacents[it.first] = mutableListOf(it.second)
        } else {
            adjacents[it.first]?.add(it.second)
        }

        if (!adjacents.containsKey(it.second)) {
            adjacents[it.second] = mutableListOf(it.first)
        } else {
            adjacents[it.second]?.add(it.first)
        }
    }

    return adjacents.mapValues {it.value.toList()}.toMap()
}

fun findPaths(adjacents: Map<String, List<String>>, a: String, b: String): Set<String> {
    val visited: MutableMap<String, Boolean> = adjacents
        .keys
        .toList()
        .associateBy({it}, {false})
        .toMutableMap()

    return findPathsInner(adjacents, a, b, visited, mutableListOf())
}

fun findPathsInner(
    adjacents: Map<String, List<String>>,
    a: String,
    b: String,
    visited: MutableMap<String, Boolean>,
    path: MutableList<String>
): Set<String> {
    visited[a] = true;
    path.add(a)

    // Target node found
    if (a == b) {
        return mutableSetOf(path.toList().joinToString(" -> "));
    }

    // Not at target node, recurse for each adjacent
    return (adjacents[a] ?: emptyList())
        .filter { !visited[it]!! }
        .map { findPathsInner(adjacents, it, b, visited.toMutableMap(), path.toMutableList()) }
        .flatten()
        .toSet()
}