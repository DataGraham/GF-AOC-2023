package aoc2022.day07

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

sealed class FileSystemItem {
    abstract val name: String
    abstract val sizeInBytes: Int

    abstract val allItems: Flow<FileSystemItem>

    data class FileItem(override val name: String, override val sizeInBytes: Int) : FileSystemItem() {
        override val allItems by lazy { flowOf(this) }
    }

    data class DirectoryItem(override val name: String, val parent: DirectoryItem?) : FileSystemItem() {
        // TODO: Store children as a map instead (efficiency and enforced unique names!)?
        private val children = mutableListOf<FileSystemItem>()

        private var cachedSize: Int? = null

        override val sizeInBytes get() = cachedSize ?: children.sumOf { it.sizeInBytes }.also { cachedSize = it }

        operator fun plusAssign(child: FileSystemItem) {
            children += child
            invalidateSize()
        }

        private fun invalidateSize() {
            cachedSize = null
            parent?.invalidateSize()
        }

        fun findChild(name: String) = children.first { it.name == name }

        @OptIn(ExperimentalCoroutinesApi::class)
        override val allItems: Flow<FileSystemItem> =
            flowOf<FileSystemItem>(this)
                .onCompletion {
                    emitAll(children.asFlow().flatMapConcat { it.allItems })
                }
    }
}