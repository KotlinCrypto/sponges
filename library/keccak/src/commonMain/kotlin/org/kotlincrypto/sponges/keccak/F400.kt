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

import kotlin.experimental.xor
import kotlin.jvm.JvmSynthetic

/**
 * [State] for Keccak-f[400]
 *
 * @see [keccakP]
 * */
public class F400: State<Short, F400> {

    @get:JvmSynthetic
    internal val array: ShortArray

    public constructor(): super(roundCount = 20) {
        array = ShortArray(P_LEN) { 0 }
    }
    private constructor(state: F400): super(state.roundCount) {
        array = state.array.copyOf()
    }

    @Throws(IndexOutOfBoundsException::class)
    public override operator fun get(index: Int): Short = array[index]

    @Throws(IndexOutOfBoundsException::class)
    public override fun addData(index: Int, data: Short) { array[index] = array[index] xor data }
    public override fun copy(): F400 = F400(this)
    public override fun reset() { array.fill(0) }

    public override fun iterator(): Iterator<Short> = array.iterator()
    public override fun contains(element: Short): Boolean = array.contains(element)
    public override fun containsAll(elements: Collection<Short>): Boolean {
        elements.forEach { n ->
            if (!array.contains(n)) return false
        }
        return true
    }
}
