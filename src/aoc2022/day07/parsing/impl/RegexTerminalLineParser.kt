package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine
import aoc2022.day07.parsing.TerminalLineParser

class RegexTerminalLineParser(
    private val pattern: String,
    private val processCaptures: (List<String>) -> TerminalLine
) : TerminalLineParser {

    private val regex by lazy { Regex(pattern) }

    override fun parse(line: String) = regex.matchEntire(line)?.let { match ->
        processCaptures(match.groupValues.drop(1))
    }
}