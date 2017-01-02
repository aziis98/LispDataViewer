package com.aziis98.test

import com.aziis98.ldv.KScanner
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Created by aziis98 on 01/01/2017.
 */
class KScannerTest {

    @Test
    fun testNext() {

        val scanner = KScanner("(prova 1 2 3)")

        //language=RegExp
        scanner.next("\\(")

        assertEquals("prova 1 2 3)", scanner.remaing)
    }

    @Test
    fun testTakeUntil() {

        val scanner = KScanner("\"stringa di testo\" 4)")

        scanner.nextChar()
        val str = scanner.takeUntil('"')

        assertEquals("stringa di testo", str)
        assertEquals(" 4)", scanner.remaing)
    }

}