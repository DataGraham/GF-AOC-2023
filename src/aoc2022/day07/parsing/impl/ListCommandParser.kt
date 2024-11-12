package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Command.ListCommand

class ListCommandParser : RegexTerminalLineParser("""\$ ls""") {
    override fun parseMatch(captures: List<String>) = ListCommand
}