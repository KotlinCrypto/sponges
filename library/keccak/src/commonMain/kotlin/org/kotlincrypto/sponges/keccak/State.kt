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

    @JvmField
    protected val state: Array<N>,
): Collection<N> {

    init {
        // state.size will always be 25
        require(state.size == P_LEN) { "state.size must equal $P_LEN" }
    }

    @Throws(IndexOutOfBoundsException::class)
    public operator fun get(index: Int): N = state[index]

    @JvmSynthetic
    @Throws(IndexOutOfBoundsException::class)
    internal operator fun set(index: Int, value: N) { state[index] = value }

    /**
     * Adds [data] to [state] at the provided [index]
     * */
    @Throws(IndexOutOfBoundsException::class)
    public fun addData(index: Int, data: N) {
        withContext { state[index] = xor(state[index], data) }
    }

    public abstract fun copy(): T
    public fun reset() {
        when (this) {
            is F200 -> state.fill(0)
            is F400 -> state.fill(0)
            is F800 -> state.fill(0)
            is F1600 -> state.fill(0)
        }
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

    @JvmSynthetic
    internal abstract fun <T: Any?> withContext(block: Context<N>.() -> T): T

    internal sealed interface Context<N: Number> {
        fun and(a: N, other: N): N
        fun inv(a: N): N
        fun xor(a: N, other: N): N
        fun rotateLeft(a: N, n: Int): N
        fun RC(index: Int): N
    }

    protected companion object {
        internal const val P_LEN: Int = 25

        internal val RC = longArrayOf(
            // 0x0000000000000001, 0x0000000000008082, 0x800000000000808a, 0x8000000080008000,
            1L, 32898L, -9223372036854742902L, -9223372034707259392L,
            // 0x000000000000808b, 0x0000000080000001, 0x8000000080008081, 0x8000000000008009,
            32907L, 2147483649L, -9223372034707259263L, -9223372036854743031L,
            // 0x000000000000008a, 0x0000000000000088, 0x0000000080008009, 0x000000008000000a,
            138L, 136L, 2147516425L, 2147483658L,
            // 0x000000008000808b, 0x800000000000008b, 0x8000000000008089, 0x8000000000008003,
            2147516555L, -9223372036854775669L, -9223372036854742903L, -9223372036854743037L,
            // 0x8000000000008002, 0x8000000000000080, 0x000000000000800a, 0x800000008000000a,
            -9223372036854743038L, -9223372036854775680L, 32778L, -9223372034707292150L,
            // 0x8000000080008081, 0x8000000000008080, 0x0000000080000001, 0x8000000080008008,
            -9223372034707259263L, -9223372036854742912L, 2147483649L, -9223372034707259384L
        )
    }
}
