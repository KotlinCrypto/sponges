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
import org.kotlincrypto.sponges.keccak.*
import org.kotlincrypto.sponges.keccak.State as KState

private const val ITERATIONS = 5
private const val TIME_WARMUP = 2
private const val TIME_MEASURE = 4

abstract class KeccakPBenchmarkBase<N: Number> {

    protected abstract val state: KState<N, *>
    protected abstract fun Int.toN(): N

    @Setup
    fun setup() {
        repeat(state.roundCount.toInt()) { i -> state.addData(i, (i + 10).toN()) }
    }
}

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.MICROSECONDS)
@Warmup(iterations = ITERATIONS, time = TIME_WARMUP)
@Measurement(iterations = ITERATIONS, time = TIME_MEASURE)
open class F1600Benchmark: KeccakPBenchmarkBase<Long>() {

    override val state = F1600()
    override fun Int.toN(): Long = toLong()

    @Benchmark
    fun exec() = state.keccakP()
}

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.MICROSECONDS)
@Warmup(iterations = ITERATIONS, time = TIME_WARMUP)
@Measurement(iterations = ITERATIONS, time = TIME_MEASURE)
open class F800Benchmark: KeccakPBenchmarkBase<Int>() {

    override val state = F800()
    override fun Int.toN(): Int = this

    @Benchmark
    fun exec() = state.keccakP()
}

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.MICROSECONDS)
@Warmup(iterations = ITERATIONS, time = TIME_WARMUP)
@Measurement(iterations = ITERATIONS, time = TIME_MEASURE)
open class F400Benchmark: KeccakPBenchmarkBase<Short>() {

    override val state = F400()
    override fun Int.toN(): Short = toShort()

    @Benchmark
    fun exec() = state.keccakP()
}

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.MICROSECONDS)
@Warmup(iterations = ITERATIONS, time = TIME_WARMUP)
@Measurement(iterations = ITERATIONS, time = TIME_MEASURE)
open class F200Benchmark: KeccakPBenchmarkBase<Byte>() {

    override val state = F200()
    override fun Int.toN(): Byte = toByte()

    @Benchmark
    fun exec() = state.keccakP()
}
