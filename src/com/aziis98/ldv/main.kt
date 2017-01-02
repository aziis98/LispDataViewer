package com.aziis98.ldv

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.layout.Pane
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.nio.file.Files

/**
 * Created by aziis98 on 01/01/2017.
 */

class Viewer : Application() {

    fun generateTreeView(obj: LispObject): TreeView<String> {

        fun generate(obj: LispObject): TreeItem<String> {
            if (obj is LispObject.List) {
                val ti = TreeItem<String>(obj.toString().take(20) + "...")
                ti.isExpanded = true

                obj.list.forEach {
                    ti.children.add(generate(it))
                }

                return ti
            }
            else {
                return TreeItem((obj as LispObject.Value<*>).toString())
            }
        }

        return TreeView(generate(obj))
    }

    override fun start(primaryStage: Stage) {
        val file = if (parameters.raw.size < 1) {
            val chooser = FileChooser()
            chooser.title = "Open Lisp Data File"
            chooser.showOpenDialog(primaryStage)
        }
        else {
            File(parameters.raw[0])
        }

        val lispObject = readLisp(file.readText())

        val root = generateTreeView(lispObject)

        primaryStage.title = "Lisp Data Viewer"
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }

}

fun main(args: Array<String>) {
    Application.launch(Viewer::class.java, *args)
}