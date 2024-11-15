package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Listing.FileListing
import aoc2022.day07.parsing.TerminalLineParser

class FileListingParser : TerminalLineParser by RegexTerminalLineParser.withPattern (
    """(\d+) (.+)""",
    { captures -> FileListing(fileSize = captures[0].toInt(), fileName = captures[1]) }
)