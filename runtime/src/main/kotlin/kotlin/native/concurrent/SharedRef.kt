/*
 * Copyright 2010-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

package kotlin.native.concurrent

import kotlin.native.internal.*

@SymbolName("Kotlin_SharedRef_createSharedRef")
external private fun createSharedRef(value: Any): NativePtr

@Frozen
@NoReorderFields
@ExportTypeInfo("theSharedRefTypeInfo")
public class SharedRef<out T : Any> private constructor(private var ptr: NativePtr) {

    companion object {
        fun <T : Any> create(value: T) = SharedRef<T>(createSharedRef(value))
    }

    @SymbolName("Kotlin_SharedRef_derefSharedRef")
    external fun get(): T
}

@Frozen
public class DisposableSharedRef<out T : Any> private constructor(
        private val ref: AtomicReference<SharedRef<T>?>
) {

    companion object {
        fun <T : Any> create(value: T): DisposableSharedRef<T> =
                DisposableSharedRef(AtomicReference(SharedRef.create(value)))
    }

    fun dispose() {
        ref.value = null
    }

    fun get(): T {
        return ref.value?.get() ?: throw NullPointerException()
    }
}
