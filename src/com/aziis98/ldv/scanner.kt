package com.aziis98.ldv

/**
 * Created by aziis98 on 01/01/2017.
 */

class KScanner(val source: String) {

    var cursor = 0

    fun nextChar(): Char {
        return source[cursor++]
    }

    fun hasNext(): Boolean {
        return cursor < source.length
    }

    fun next(pattern: String): String? {
        val value = Regex("^" + pattern).find(source, cursor)?.value ?: error("Pattern did not match!")

        cursor += value.length

        return value
    }

    fun takeUntil(pred: (Char) -> Boolean): String {
        val init = cursor

        while (!pred(source[cursor++])) { }

        cursor--

        return source.substring(init, cursor)
    }

    fun takeUntil(target: Char): String {
        val init = cursor

        while (source[cursor++] != target) { }

        cursor--

        return source.substring(init, cursor)
    }

    val remaing: String
        get() = source.substring(cursor)

    val nextChar: Char
        get() = source[cursor]

}