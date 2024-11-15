package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Command.ListCommand

fun listCommandParser() = RegexTerminalLineParser(pattern = """\$ ls""") { ListCommand }
