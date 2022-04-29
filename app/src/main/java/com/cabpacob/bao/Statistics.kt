package com.cabpacob.bao
import kotlinx.serialization.Serializable

@Serializable
class Statistics{
    val map: MutableMap<String, Pair<Int, Int>> = mutableMapOf()

    private fun getValue(name: String) = map[name] ?: Pair(0, 0)

    fun registerWin(name: String) {
        val (win, lose) = getValue(name)

        map[name] = Pair(win + 1, lose)
    }

    fun registerLose(name: String) {
        val (win, lose) = getValue(name)

        map[name] = Pair(win, lose + 1)
    }
}
