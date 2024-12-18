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

/**
 * [State] for Keccak-f[1600]
 * */
public class F1600: State<Long, F1600> {
    public constructor(): super(roundCount = 24, state = Array(P_LEN) { 0 })
    private constructor(state: F1600): super(state.roundCount, state.state.copyOf())
    public override fun copy(): F1600 = F1600(this)
    protected override fun Long.XOR(data: Long): Long = this xor data
    protected override fun Long.toN(): Long = this
}
