package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Output.DirectoryListing

class DirectoryListingParser : RegexTerminalLineParser("""dir (.+)""") {
    override fun parseMatch(captures: List<String>) =
        DirectoryListing(name = captures.first())
}