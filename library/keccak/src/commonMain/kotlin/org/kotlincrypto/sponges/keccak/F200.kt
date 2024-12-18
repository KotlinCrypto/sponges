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
 * [State] for Keccak-f[200]
 * */
public class F200: State<Byte, F200> {
    public constructor(): super(roundCount = 18, state = Array(P_LEN) { 0 })
    private constructor(state: F200): super(state.roundCount, state.state.copyOf())
    public override fun copy(): F200 = F200(this)
    protected override fun Byte.mixIn(data: Byte): Byte = this xor data
    @JvmSynthetic
    internal override fun RC(index: Int): Byte = RC[index].toByte()
}
