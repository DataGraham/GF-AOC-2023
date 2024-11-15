package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Listing.DirectoryListing
import aoc2022.day07.parsing.TerminalLineParser

class DirectoryListingParser : TerminalLineParser by RegexTerminalLineParser.withPattern(
    """dir (.+)""",
    { captures -> DirectoryListing(directoryName = captures.first()) }
)