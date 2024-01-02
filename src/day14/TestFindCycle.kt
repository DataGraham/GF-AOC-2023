package day14

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TestFindCycle {
    @Test
    fun testFindTypicalCycle() {
        val nextIndex = listOf(1, 2, 3, 4, 5, 2)
        /*
        0 -> 1 -> 2 -> 3 -> 4 -> 5
                  ^              |
                  ---------------
        */
        val expectedCycle = CycleInfo(
            distanceToStart = 2,
            startValue = 2,
            length = 4
        )
        val cycle = findCycle(0) { nextIndex[this] }
        assertThat(cycle).isEqualTo(expectedCycle)
    }
}