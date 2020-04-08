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
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

open class MergeCompilationDatabases @Inject constructor(): DefaultTask() {
    @InputFiles
    val inputFiles = mutableListOf<File>()

    @OutputFile
    lateinit var outputFile: File

    @TaskAction
    fun run() {
        val serializer = GenerateCompilationDatabase.Entry.serializer().list
        val entries = mutableListOf<GenerateCompilationDatabase.Entry>()
        for (file in inputFiles) {
            entries.addAll(Json.parse(serializer, file.readText()))
        }
        val json = Json(JsonConfiguration.Stable)
        outputFile.writeText(json.stringify(serializer, entries))
    }
}
