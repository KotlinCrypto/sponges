/*
 * Copyright (c) 2023 Matthew Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
@file:Suppress("FunctionName")

package org.kotlincrypto.sponges.keccak

import kotlin.jvm.JvmField
import kotlin.jvm.JvmSynthetic

/**
 * Core abstraction for Keccak-p[b, nr] state, or "lanes".
 *
 * @see [addData]
 * @see [F200]
 * @see [F400]
 * @see [F800]
 * @see [F1600]
 * */
public sealed class State<N: Number, T: State<N, T>>(

    /**
     * The maximum number of rounds for Keccak-p
     * */
    @JvmField
    public val roundCount: Byte,

    private val state: Array<N>,
): Collection<N> {

    init {
        // state.size will always be 25
        require(state.size == P_LEN) { "state.size must equal $P_LEN" }
    }

    @Throws(IndexOutOfBoundsException::class)
    public operator fun get(index: Int): N = state[index]

    /**
     * Adds [data] to [state] at the provided [index]
     *
     * @throws [IndexOutOfBoundsException] if [index] is not between 0 and 24 (inclusive)
     * */
    @Throws(IndexOutOfBoundsException::class)
    public fun addData(index: Int, data: N) {
        state[index] = state[index] XOR data
    }

    public abstract fun copy(): T

    public fun reset() {
        val zero = when (this) {
            is F1600 -> 0L
            is F800 -> 0
            is F400 -> 0.toShort()
            is F200 -> 0.toByte()
        }
        @Suppress("UNCHECKED_CAST")
        state.fill(zero as N)
    }

    final override val size: Int get() = P_LEN
    final override fun isEmpty(): Boolean = false
    final override operator fun contains(element: N): Boolean = state.contains(element)
    final override fun iterator(): Iterator<N> = object : Iterator<N> {
        private val delegate = state.iterator()

        override fun hasNext(): Boolean = delegate.hasNext()
        override fun next(): N = delegate.next()

        override fun equals(other: Any?): Boolean = delegate == other
        override fun hashCode(): Int = delegate.hashCode()
        override fun toString(): String = delegate.toString()
    }
    final override fun containsAll(elements: Collection<N>): Boolean {
        elements.forEach { n ->
            if (!state.contains(n)) return false
        }

        return true
    }

    protected abstract infix fun N.XOR(data: N): N

    @JvmSynthetic
    internal fun lanes(): Array<N> = state

    protected companion object {
        internal const val P_LEN: Int = 25
    }
}
