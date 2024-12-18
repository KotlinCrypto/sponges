/*
 * Copyright (c) 2024 Matthew Nelson
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
package org.kotlincrypto.sponges.benchmarks

import kotlinx.benchmark.*
import org.kotlincrypto.sponges.keccak.F1600
import org.kotlincrypto.sponges.keccak.keccakP

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 2)
@Measurement(iterations = 5, time = 4)
open class KeccakPBenchmark {

    private val state = F1600()

    @Setup
    fun setup() {
        repeat(21) { index -> state.addData(index, (index + 10).toLong()) }
    }

    @Benchmark
    fun keccakP() { state.keccakP() }
}
