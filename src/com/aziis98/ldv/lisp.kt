package com.aziis98.ldv

import java.util.*

/**
 * Created by aziis98 on 01/01/2017.
 */

sealed class LispObject {

    class List(val list: kotlin.collections.List<LispObject>) : LispObject() {
        override fun toString() = list.joinToString(" ", "(", ")")
    }

    sealed class Value<out T>(val value: T) : LispObject() {
        class Symbol(value: kotlin.String) : Value<kotlin.String>(value) {
            override fun toString() = value
        }

        class Integer(value: Int) : Value<Int>(value) {
            override fun toString() = value.toString()
        }
        class Decimal(value: Double) : Value<Double>(value) {
            override fun toString() = value.toString()
        }
        class String(value: kotlin.String) : Value<kotlin.String>(value) {
            override fun toString() = "\"$value\""
        }
    }

}

fun readLisp(source: String): LispObject {
    return parseLisp(tokenize(source))
}

fun tokenize(source: String): List<String> {
    val list = mutableListOf<String>()
    val scanner = KScanner(source)

    while (scanner.hasNext()) {
        if (scanner.nextChar == '(') {
            list += scanner.nextChar().toString()
        }
        else if (scanner.nextChar == ')') {
            list += scanner.nextChar().toString()
        }
        else if (scanner.nextChar == '"') {
            scanner.nextChar()
            list += "\"" + scanner.takeUntil('"') + "\""
            scanner.nextChar()
        }
        else if (scanner.nextChar.isWhitespace()) {
            scanner.nextChar()
        }
        else {
            list += scanner.takeUntil { it.isWhitespace() || it == ')' }
        }
    }

    return list
}

fun parseLisp(tokens: List<String>): LispObject {

    val buffer = tokens.toCollection(LinkedList())

    fun parseValue(): LispObject.Value<*> {
        val token = buffer.pop()

        return when {

            token.first() == '"' && token.last() == '"' -> {
                LispObject.Value.String(token.substring(1, token.length - 1))
            }

            token.matches(Regex("\\d+(\\.\\d*)?"))      -> {
                if (token.contains('.'))
                    LispObject.Value.Decimal(token.toDouble())
                else
                    LispObject.Value.Integer(token.toInt())
            }

            else                                        -> {
                LispObject.Value.Symbol(token)
            }
        }
    }

    fun parseList(): LispObject.List {

        val list = mutableListOf<LispObject>()

        assert(buffer.pop() == "(")

        while (buffer.peek() != ")") {
            if (buffer.peek() == "(") {
                list += parseList()
            }
            else {
                list += parseValue()
            }
        }

        assert(buffer.pop() == ")")

        return LispObject.List(list)
    }

    return parseList()
}