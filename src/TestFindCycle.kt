import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TestFindCycle {
    @Test
    fun findMinimalCycle() {
        val expectedCycle = CycleInfo(
            distanceToStart = 0,
            startValue = 0,
            length = 1
        )
        val cycle = findCycle(0) { 0 }
        assertThat(cycle).isEqualTo(expectedCycle)
    }

    @Test
    fun findSemiMinimalCycle() {
        val expectedCycle = CycleInfo(
            distanceToStart = 0,
            startValue = false,
            length = 2
        )
        val cycle = findCycle(false) { !this }
        assertThat(cycle).isEqualTo(expectedCycle)
    }

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