package uk.org.fca.tinysubmarine

import java.io.File
import java.util.*

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
    println("${paths.count()} unique roots found between $from and $to")
}

fun readAdjacents(fileName: String): List<List<String>> {
    return File(fileName)
        .useLines { it.toList() }
        .map { it.split('-') }
}

fun buildAdjacentsMap(rawAdjacents: List<List<String>>): Map<String, List<String>> {
    return rawAdjacents.map { mapOf(it.first() to it.last(), it.last() to it.first()) }
        .flatMap { it.entries }
        .groupBy({ it.key }) { it.value }
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
    // small caves, designated being lower case, may be visited only once
    visited[a] = a != a.uppercase(Locale.getDefault())
    path.add(a)

    // Target node found
    if (a == b) {
        return mutableSetOf(path.toList().joinToString(" -> "))
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