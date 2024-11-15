package aoc2022.day07

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

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
        override var sizeInBytes = 0
            private set

        operator fun plusAssign(child: FileSystemItem) {
            children += child
            addSize(child.sizeInBytes)
        }

        private fun addSize(bytesToAdd: Int) {
            sizeInBytes += bytesToAdd
            parent?.addSize(bytesToAdd)
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