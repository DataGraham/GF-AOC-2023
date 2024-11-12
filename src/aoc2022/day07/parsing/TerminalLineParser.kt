package aoc2022.day07.parsing

import aoc2022.day07.TerminalLine

interface TerminalLineParser {
    fun parse(line: String): TerminalLine?
}