package aoc2022.day07.parsing.impl

import RegexParser
import aoc2022.day07.TerminalLine
import aoc2022.day07.TerminalLine.Command.ListCommand

fun listCommandParser() = RegexParser<TerminalLine>(pattern = """\$ ls""") { ListCommand }
