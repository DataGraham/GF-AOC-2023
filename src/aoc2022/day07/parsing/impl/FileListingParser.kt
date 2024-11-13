package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Listing.FileListing

class FileListingParser : RegexTerminalLineParser("""(\d+) (.+)""") {
    override fun parseMatch(captures: List<String>) =
        FileListing(size = captures[0].toInt(), name = captures[1])
}