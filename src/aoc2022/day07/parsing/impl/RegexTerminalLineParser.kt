package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine
import aoc2022.day07.parsing.TerminalLineParser

abstract class RegexTerminalLineParser(private val pattern: String) : TerminalLineParser {

    companion object {
        fun withPattern(pattern: String, processCaptures: (List<String>) -> TerminalLine) =
            object : RegexTerminalLineParser(pattern = pattern) {
                override fun processCaptures(captures: List<String>) = processCaptures(captures)
            }
    }

    private val regex by lazy { Regex(pattern) }

    final override fun parse(line: String) = regex.matchEntire(line)?.let { match ->
        processCaptures(captures = match.groupValues.drop(1))
    }

    protected abstract fun processCaptures(captures: List<String>): TerminalLine
}