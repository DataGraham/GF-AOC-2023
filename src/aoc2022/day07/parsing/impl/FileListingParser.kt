package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Listing.FileListing

class FileListingParser : RegexTerminalLineParser("""(\d+) (.+)""") {
    override fun parseMatch(captures: List<String>) =
        FileListing(fileSize = captures[0].toInt(), fileName = captures[1])
}