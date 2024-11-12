package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Command.ChangeDirectoryCommand

class ChangeDirectoryParser : RegexTerminalLineParser("""\$ cd (.+)""") {
    override fun parseMatch(captures: List<String>) =
        ChangeDirectoryCommand(directoryName = captures.first())
}