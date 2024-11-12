package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine
import aoc2022.day07.parsing.TerminalLineParser

abstract class RegexTerminalLineParser(private val pattern: String) : TerminalLineParser {
    private val regex by lazy { Regex(pattern) }

    final override fun parse(line: String) = regex.matchEntire(line)?.let { match ->
        parseMatch(captures = match.groupValues.drop(1))
    }

    protected abstract fun parseMatch(captures: List<String>): TerminalLine
}