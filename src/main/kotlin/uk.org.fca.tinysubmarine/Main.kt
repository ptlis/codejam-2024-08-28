package uk.org.fca.tinysubmarine

import java.io.File

class Main

fun main(args: Array<String>) {
    if (args.count() != 2) {
        println("A 'from' and 'to' cave must be specified")
        return
    }

    val from = args[0]
    val to = args[1]
    val adjacents = buildAdjacentsMap(readAdjacents("./data/rawdata"))
    val paths = findPaths(adjacents, from, to)

    paths.forEach { println(it) }
    println("${paths.count()} unique roots found between ${from} and ${to}")
}

fun readAdjacents(fileName: String): List<Pair<String, String>> {
    return File(fileName)
        .useLines { it.toList() }
        .map { it.split('-') }
        .map { Pair(it.first(), it.last()) }
}

fun buildAdjacentsMap(rawAdjacents: List<Pair<String, String>>): Map<String, List<String>> {
    val adjacents: MutableMap<String, MutableList<String>> = mutableMapOf();
    fun addToAdjacents(from: String, to: String) {
        val adjacentsList = (adjacents[from] ?: mutableListOf())
        adjacentsList.add(to)
        adjacents[from] = adjacentsList
    }

    rawAdjacents.forEach {
        addToAdjacents(it.first, it.second)
        addToAdjacents(it.second, it.first)
    }

    return adjacents.mapValues {it.value.toList()}.toMap()
}

fun findPaths(
    adjacents: Map<String, List<String>>,
    a: String,
    b: String,
    visited: MutableMap<String, Boolean> = adjacents
        .keys
        .toList()
        .associateBy({it}, {false})
        .toMutableMap(),
    path: MutableList<String> = mutableListOf()
): Set<String> {
    visited[a] = true;
    path.add(a)

    // Target node found
    if (a == b) {
        return mutableSetOf(path.toList().joinToString(" -> "));
    }

    // Not at target node, recurse for each adjacent
    return (adjacents[a] ?: emptyList())
        // Only process unvisited vertices
        .filter { !visited[it]!! }
        // Find any paths built from this path
        .map { findPaths(adjacents, it, b, visited.toMutableMap(), path.toMutableList()) }
        .flatten()
        .toSet()
}