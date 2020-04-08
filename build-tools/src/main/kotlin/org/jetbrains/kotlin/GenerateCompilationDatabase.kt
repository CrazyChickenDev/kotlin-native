/*
 * Copyright 2010-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

package org.jetbrains.kotlin

import java.io.File
import javax.inject.Inject
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

open class GenerateCompilationDatabase @Inject constructor(@Input val directory: String,
                                                           @Input val files: List<String>,
                                                           @Input val arguments: List<String>,
                                                           @Input val output: String): DefaultTask() {

    @OutputFile
    lateinit var outputFile: File

    @Serializable
    data class Entry(val directory: String, val file: String, val arguments: List<String>, val output: String)

    @TaskAction
    fun run() {
        val json = Json(JsonConfiguration.Stable)
        val entries: List<Entry> = files.map { Entry(directory, it, arguments, output) }
        outputFile.writeText(json.stringify(Entry.serializer().list, entries))
    }
}
