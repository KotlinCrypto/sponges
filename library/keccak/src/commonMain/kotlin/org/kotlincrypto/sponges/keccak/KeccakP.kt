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
@file:Suppress("KotlinRedundantDiagnosticSuppress", "LocalVariableName")

package org.kotlincrypto.sponges.keccak

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.xor
import kotlin.jvm.JvmOverloads

private val RC = longArrayOf(
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

// Unfortunately, cannot utilize generic N: Number and inline
// functions as Kotlin will use boxing when compiled for Java
// which is considerably slower. So, implementation is just
// copy/pasted for each...

/**
 * Keccak-f[1600] sponge function
 *
 * @throws [IllegalArgumentException] when [rounds] exceeds [F1600.roundCount]
 * */
@JvmOverloads
@Throws(IllegalArgumentException::class)
public fun F1600.keccakP(rounds: Byte = roundCount) {
    checkRounds(rounds) ?: return
    val A = array

    var a00 = A[ 0]; var a01 = A[ 1]; var a02 = A[ 2]; var a03 = A[ 3]; var a04 = A[ 4]
    var a05 = A[ 5]; var a06 = A[ 6]; var a07 = A[ 7]; var a08 = A[ 8]; var a09 = A[ 9]
    var a10 = A[10]; var a11 = A[11]; var a12 = A[12]; var a13 = A[13]; var a14 = A[14]
    var a15 = A[15]; var a16 = A[16]; var a17 = A[17]; var a18 = A[18]; var a19 = A[19]
    var a20 = A[20]; var a21 = A[21]; var a22 = A[22]; var a23 = A[23]; var a24 = A[24]

    for (rc in (roundCount - rounds) until roundCount) {
        // Theta
        var c0 = a00 xor a05 xor a10 xor a15 xor a20
        var c1 = a01 xor a06 xor a11 xor a16 xor a21
        val c2 = a02 xor a07 xor a12 xor a17 xor a22
        val c3 = a03 xor a08 xor a13 xor a18 xor a23
        val c4 = a04 xor a09 xor a14 xor a19 xor a24

        val d1 = c1.rotateLeft(1) xor c4
        val d2 = c2.rotateLeft(1) xor c0
        val d3 = c3.rotateLeft(1) xor c1
        val d4 = c4.rotateLeft(1) xor c2
        val d0 = c0.rotateLeft(1) xor c3

        a00 = a00 xor d1
        a01 = a01 xor d2
        a02 = a02 xor d3
        a03 = a03 xor d4
        a04 = a04 xor d0
        a05 = a05 xor d1
        a06 = a06 xor d2
        a07 = a07 xor d3
        a08 = a08 xor d4
        a09 = a09 xor d0
        a10 = a10 xor d1
        a11 = a11 xor d2
        a12 = a12 xor d3
        a13 = a13 xor d4
        a14 = a14 xor d0
        a15 = a15 xor d1
        a16 = a16 xor d2
        a17 = a17 xor d3
        a18 = a18 xor d4
        a19 = a19 xor d0
        a20 = a20 xor d1
        a21 = a21 xor d2
        a22 = a22 xor d3
        a23 = a23 xor d4
        a24 = a24 xor d0

        // Rho Pi
        c1  = a01.rotateLeft( 1)
        a01 = a06.rotateLeft(44)
        a06 = a09.rotateLeft(20)
        a09 = a22.rotateLeft(61)
        a22 = a14.rotateLeft(39)
        a14 = a20.rotateLeft(18)
        a20 = a02.rotateLeft(62)
        a02 = a12.rotateLeft(43)
        a12 = a13.rotateLeft(25)
        a13 = a19.rotateLeft( 8)
        a19 = a23.rotateLeft(56)
        a23 = a15.rotateLeft(41)
        a15 = a04.rotateLeft(27)
        a04 = a24.rotateLeft(14)
        a24 = a21.rotateLeft( 2)
        a21 = a08.rotateLeft(55)
        a08 = a16.rotateLeft(45)
        a16 = a05.rotateLeft(36)
        a05 = a03.rotateLeft(28)
        a03 = a18.rotateLeft(21)
        a18 = a17.rotateLeft(15)
        a17 = a11.rotateLeft(10)
        a11 = a07.rotateLeft( 6)
        a07 = a10.rotateLeft( 3)
        a10 = c1

        // Chi
        c0  = a00 xor (a01.inv() and a02)
        c1  = a01 xor (a02.inv() and a03)
        a02 = a02 xor (a03.inv() and a04)
        a03 = a03 xor (a04.inv() and a00)
        a04 = a04 xor (a00.inv() and a01)
        a00 = c0
        a01 = c1

        c0  = a05 xor (a06.inv() and a07)
        c1  = a06 xor (a07.inv() and a08)
        a07 = a07 xor (a08.inv() and a09)
        a08 = a08 xor (a09.inv() and a05)
        a09 = a09 xor (a05.inv() and a06)
        a05 = c0
        a06 = c1

        c0  = a10 xor (a11.inv() and a12)
        c1  = a11 xor (a12.inv() and a13)
        a12 = a12 xor (a13.inv() and a14)
        a13 = a13 xor (a14.inv() and a10)
        a14 = a14 xor (a10.inv() and a11)
        a10 = c0
        a11 = c1

        c0  = a15 xor (a16.inv() and a17)
        c1  = a16 xor (a17.inv() and a18)
        a17 = a17 xor (a18.inv() and a19)
        a18 = a18 xor (a19.inv() and a15)
        a19 = a19 xor (a15.inv() and a16)
        a15 = c0
        a16 = c1

        c0  = a20 xor (a21.inv() and a22)
        c1  = a21 xor (a22.inv() and a23)
        a22 = a22 xor (a23.inv() and a24)
        a23 = a23 xor (a24.inv() and a20)
        a24 = a24 xor (a20.inv() and a21)
        a20 = c0
        a21 = c1

        // Iota
        a00 = a00 xor RC[rc]
    }

    A[ 0] = a00; A[ 1] = a01; A[ 2] = a02; A[ 3] = a03; A[ 4] = a04
    A[ 5] = a05; A[ 6] = a06; A[ 7] = a07; A[ 8] = a08; A[ 9] = a09
    A[10] = a10; A[11] = a11; A[12] = a12; A[13] = a13; A[14] = a14
    A[15] = a15; A[16] = a16; A[17] = a17; A[18] = a18; A[19] = a19
    A[20] = a20; A[21] = a21; A[22] = a22; A[23] = a23; A[24] = a24
}

/**
 * Keccak-f[800] sponge function
 *
 * @throws [IllegalArgumentException] when [rounds] exceeds [F800.roundCount]
 * */
@JvmOverloads
@Throws(IllegalArgumentException::class)
public fun F800.keccakP(rounds: Byte = roundCount) {
    checkRounds(rounds) ?: return
    val A = array

    var a00 = A[ 0]; var a01 = A[ 1]; var a02 = A[ 2]; var a03 = A[ 3]; var a04 = A[ 4]
    var a05 = A[ 5]; var a06 = A[ 6]; var a07 = A[ 7]; var a08 = A[ 8]; var a09 = A[ 9]
    var a10 = A[10]; var a11 = A[11]; var a12 = A[12]; var a13 = A[13]; var a14 = A[14]
    var a15 = A[15]; var a16 = A[16]; var a17 = A[17]; var a18 = A[18]; var a19 = A[19]
    var a20 = A[20]; var a21 = A[21]; var a22 = A[22]; var a23 = A[23]; var a24 = A[24]

    for (rc in (roundCount - rounds) until roundCount) {
        // Theta
        var c0 = a00 xor a05 xor a10 xor a15 xor a20
        var c1 = a01 xor a06 xor a11 xor a16 xor a21
        val c2 = a02 xor a07 xor a12 xor a17 xor a22
        val c3 = a03 xor a08 xor a13 xor a18 xor a23
        val c4 = a04 xor a09 xor a14 xor a19 xor a24

        val d1 = c1.rotateLeft(1) xor c4
        val d2 = c2.rotateLeft(1) xor c0
        val d3 = c3.rotateLeft(1) xor c1
        val d4 = c4.rotateLeft(1) xor c2
        val d0 = c0.rotateLeft(1) xor c3

        a00 = a00 xor d1
        a01 = a01 xor d2
        a02 = a02 xor d3
        a03 = a03 xor d4
        a04 = a04 xor d0
        a05 = a05 xor d1
        a06 = a06 xor d2
        a07 = a07 xor d3
        a08 = a08 xor d4
        a09 = a09 xor d0
        a10 = a10 xor d1
        a11 = a11 xor d2
        a12 = a12 xor d3
        a13 = a13 xor d4
        a14 = a14 xor d0
        a15 = a15 xor d1
        a16 = a16 xor d2
        a17 = a17 xor d3
        a18 = a18 xor d4
        a19 = a19 xor d0
        a20 = a20 xor d1
        a21 = a21 xor d2
        a22 = a22 xor d3
        a23 = a23 xor d4
        a24 = a24 xor d0

        // Rho Pi
        c1  = a01.rotateLeft( 1)
        a01 = a06.rotateLeft(44)
        a06 = a09.rotateLeft(20)
        a09 = a22.rotateLeft(61)
        a22 = a14.rotateLeft(39)
        a14 = a20.rotateLeft(18)
        a20 = a02.rotateLeft(62)
        a02 = a12.rotateLeft(43)
        a12 = a13.rotateLeft(25)
        a13 = a19.rotateLeft( 8)
        a19 = a23.rotateLeft(56)
        a23 = a15.rotateLeft(41)
        a15 = a04.rotateLeft(27)
        a04 = a24.rotateLeft(14)
        a24 = a21.rotateLeft( 2)
        a21 = a08.rotateLeft(55)
        a08 = a16.rotateLeft(45)
        a16 = a05.rotateLeft(36)
        a05 = a03.rotateLeft(28)
        a03 = a18.rotateLeft(21)
        a18 = a17.rotateLeft(15)
        a17 = a11.rotateLeft(10)
        a11 = a07.rotateLeft( 6)
        a07 = a10.rotateLeft( 3)
        a10 = c1

        // Chi
        c0  = a00 xor (a01.inv() and a02)
        c1  = a01 xor (a02.inv() and a03)
        a02 = a02 xor (a03.inv() and a04)
        a03 = a03 xor (a04.inv() and a00)
        a04 = a04 xor (a00.inv() and a01)
        a00 = c0
        a01 = c1

        c0  = a05 xor (a06.inv() and a07)
        c1  = a06 xor (a07.inv() and a08)
        a07 = a07 xor (a08.inv() and a09)
        a08 = a08 xor (a09.inv() and a05)
        a09 = a09 xor (a05.inv() and a06)
        a05 = c0
        a06 = c1

        c0  = a10 xor (a11.inv() and a12)
        c1  = a11 xor (a12.inv() and a13)
        a12 = a12 xor (a13.inv() and a14)
        a13 = a13 xor (a14.inv() and a10)
        a14 = a14 xor (a10.inv() and a11)
        a10 = c0
        a11 = c1

        c0  = a15 xor (a16.inv() and a17)
        c1  = a16 xor (a17.inv() and a18)
        a17 = a17 xor (a18.inv() and a19)
        a18 = a18 xor (a19.inv() and a15)
        a19 = a19 xor (a15.inv() and a16)
        a15 = c0
        a16 = c1

        c0  = a20 xor (a21.inv() and a22)
        c1  = a21 xor (a22.inv() and a23)
        a22 = a22 xor (a23.inv() and a24)
        a23 = a23 xor (a24.inv() and a20)
        a24 = a24 xor (a20.inv() and a21)
        a20 = c0
        a21 = c1

        // Iota
        a00 = a00 xor RC[rc].toInt()
    }

    A[ 0] = a00; A[ 1] = a01; A[ 2] = a02; A[ 3] = a03; A[ 4] = a04
    A[ 5] = a05; A[ 6] = a06; A[ 7] = a07; A[ 8] = a08; A[ 9] = a09
    A[10] = a10; A[11] = a11; A[12] = a12; A[13] = a13; A[14] = a14
    A[15] = a15; A[16] = a16; A[17] = a17; A[18] = a18; A[19] = a19
    A[20] = a20; A[21] = a21; A[22] = a22; A[23] = a23; A[24] = a24
}

/**
 * Keccak-f[400] sponge function
 *
 * @throws [IllegalArgumentException] when [rounds] exceeds [F400.roundCount]
 * */
@JvmOverloads
@Throws(IllegalArgumentException::class)
public fun F400.keccakP(rounds: Byte = roundCount) {
    checkRounds(rounds) ?: return
    val A = array

    var a00 = A[ 0]; var a01 = A[ 1]; var a02 = A[ 2]; var a03 = A[ 3]; var a04 = A[ 4]
    var a05 = A[ 5]; var a06 = A[ 6]; var a07 = A[ 7]; var a08 = A[ 8]; var a09 = A[ 9]
    var a10 = A[10]; var a11 = A[11]; var a12 = A[12]; var a13 = A[13]; var a14 = A[14]
    var a15 = A[15]; var a16 = A[16]; var a17 = A[17]; var a18 = A[18]; var a19 = A[19]
    var a20 = A[20]; var a21 = A[21]; var a22 = A[22]; var a23 = A[23]; var a24 = A[24]

    for (rc in (roundCount - rounds) until roundCount) {
        // Theta
        var c0 = a00 xor a05 xor a10 xor a15 xor a20
        var c1 = a01 xor a06 xor a11 xor a16 xor a21
        val c2 = a02 xor a07 xor a12 xor a17 xor a22
        val c3 = a03 xor a08 xor a13 xor a18 xor a23
        val c4 = a04 xor a09 xor a14 xor a19 xor a24

        val d1 = c1.rotateLeft(1) xor c4
        val d2 = c2.rotateLeft(1) xor c0
        val d3 = c3.rotateLeft(1) xor c1
        val d4 = c4.rotateLeft(1) xor c2
        val d0 = c0.rotateLeft(1) xor c3

        a00 = a00 xor d1
        a01 = a01 xor d2
        a02 = a02 xor d3
        a03 = a03 xor d4
        a04 = a04 xor d0
        a05 = a05 xor d1
        a06 = a06 xor d2
        a07 = a07 xor d3
        a08 = a08 xor d4
        a09 = a09 xor d0
        a10 = a10 xor d1
        a11 = a11 xor d2
        a12 = a12 xor d3
        a13 = a13 xor d4
        a14 = a14 xor d0
        a15 = a15 xor d1
        a16 = a16 xor d2
        a17 = a17 xor d3
        a18 = a18 xor d4
        a19 = a19 xor d0
        a20 = a20 xor d1
        a21 = a21 xor d2
        a22 = a22 xor d3
        a23 = a23 xor d4
        a24 = a24 xor d0

        // Rho Pi
        c1  = a01.rotateLeft( 1)
        a01 = a06.rotateLeft(44)
        a06 = a09.rotateLeft(20)
        a09 = a22.rotateLeft(61)
        a22 = a14.rotateLeft(39)
        a14 = a20.rotateLeft(18)
        a20 = a02.rotateLeft(62)
        a02 = a12.rotateLeft(43)
        a12 = a13.rotateLeft(25)
        a13 = a19.rotateLeft( 8)
        a19 = a23.rotateLeft(56)
        a23 = a15.rotateLeft(41)
        a15 = a04.rotateLeft(27)
        a04 = a24.rotateLeft(14)
        a24 = a21.rotateLeft( 2)
        a21 = a08.rotateLeft(55)
        a08 = a16.rotateLeft(45)
        a16 = a05.rotateLeft(36)
        a05 = a03.rotateLeft(28)
        a03 = a18.rotateLeft(21)
        a18 = a17.rotateLeft(15)
        a17 = a11.rotateLeft(10)
        a11 = a07.rotateLeft( 6)
        a07 = a10.rotateLeft( 3)
        a10 = c1

        // Chi
        c0  = a00 xor (a01.inv() and a02)
        c1  = a01 xor (a02.inv() and a03)
        a02 = a02 xor (a03.inv() and a04)
        a03 = a03 xor (a04.inv() and a00)
        a04 = a04 xor (a00.inv() and a01)
        a00 = c0
        a01 = c1

        c0  = a05 xor (a06.inv() and a07)
        c1  = a06 xor (a07.inv() and a08)
        a07 = a07 xor (a08.inv() and a09)
        a08 = a08 xor (a09.inv() and a05)
        a09 = a09 xor (a05.inv() and a06)
        a05 = c0
        a06 = c1

        c0  = a10 xor (a11.inv() and a12)
        c1  = a11 xor (a12.inv() and a13)
        a12 = a12 xor (a13.inv() and a14)
        a13 = a13 xor (a14.inv() and a10)
        a14 = a14 xor (a10.inv() and a11)
        a10 = c0
        a11 = c1

        c0  = a15 xor (a16.inv() and a17)
        c1  = a16 xor (a17.inv() and a18)
        a17 = a17 xor (a18.inv() and a19)
        a18 = a18 xor (a19.inv() and a15)
        a19 = a19 xor (a15.inv() and a16)
        a15 = c0
        a16 = c1

        c0  = a20 xor (a21.inv() and a22)
        c1  = a21 xor (a22.inv() and a23)
        a22 = a22 xor (a23.inv() and a24)
        a23 = a23 xor (a24.inv() and a20)
        a24 = a24 xor (a20.inv() and a21)
        a20 = c0
        a21 = c1

        // Iota
        a00 = a00 xor RC[rc].toShort()
    }

    A[ 0] = a00; A[ 1] = a01; A[ 2] = a02; A[ 3] = a03; A[ 4] = a04
    A[ 5] = a05; A[ 6] = a06; A[ 7] = a07; A[ 8] = a08; A[ 9] = a09
    A[10] = a10; A[11] = a11; A[12] = a12; A[13] = a13; A[14] = a14
    A[15] = a15; A[16] = a16; A[17] = a17; A[18] = a18; A[19] = a19
    A[20] = a20; A[21] = a21; A[22] = a22; A[23] = a23; A[24] = a24
}

/**
 * Keccak-f[200] sponge function
 *
 * @throws [IllegalArgumentException] when [rounds] exceeds [F200.roundCount]
 * */
@JvmOverloads
@Throws(IllegalArgumentException::class)
public fun F200.keccakP(rounds: Byte = roundCount) {
    checkRounds(rounds) ?: return
    val A = array

    var a00 = A[ 0]; var a01 = A[ 1]; var a02 = A[ 2]; var a03 = A[ 3]; var a04 = A[ 4]
    var a05 = A[ 5]; var a06 = A[ 6]; var a07 = A[ 7]; var a08 = A[ 8]; var a09 = A[ 9]
    var a10 = A[10]; var a11 = A[11]; var a12 = A[12]; var a13 = A[13]; var a14 = A[14]
    var a15 = A[15]; var a16 = A[16]; var a17 = A[17]; var a18 = A[18]; var a19 = A[19]
    var a20 = A[20]; var a21 = A[21]; var a22 = A[22]; var a23 = A[23]; var a24 = A[24]

    for (rc in (roundCount - rounds) until roundCount) {
        // Theta
        var c0 = a00 xor a05 xor a10 xor a15 xor a20
        var c1 = a01 xor a06 xor a11 xor a16 xor a21
        val c2 = a02 xor a07 xor a12 xor a17 xor a22
        val c3 = a03 xor a08 xor a13 xor a18 xor a23
        val c4 = a04 xor a09 xor a14 xor a19 xor a24

        val d1 = c1.rotateLeft(1) xor c4
        val d2 = c2.rotateLeft(1) xor c0
        val d3 = c3.rotateLeft(1) xor c1
        val d4 = c4.rotateLeft(1) xor c2
        val d0 = c0.rotateLeft(1) xor c3

        a00 = a00 xor d1
        a01 = a01 xor d2
        a02 = a02 xor d3
        a03 = a03 xor d4
        a04 = a04 xor d0
        a05 = a05 xor d1
        a06 = a06 xor d2
        a07 = a07 xor d3
        a08 = a08 xor d4
        a09 = a09 xor d0
        a10 = a10 xor d1
        a11 = a11 xor d2
        a12 = a12 xor d3
        a13 = a13 xor d4
        a14 = a14 xor d0
        a15 = a15 xor d1
        a16 = a16 xor d2
        a17 = a17 xor d3
        a18 = a18 xor d4
        a19 = a19 xor d0
        a20 = a20 xor d1
        a21 = a21 xor d2
        a22 = a22 xor d3
        a23 = a23 xor d4
        a24 = a24 xor d0

        // Rho Pi
        c1  = a01.rotateLeft( 1)
        a01 = a06.rotateLeft(44)
        a06 = a09.rotateLeft(20)
        a09 = a22.rotateLeft(61)
        a22 = a14.rotateLeft(39)
        a14 = a20.rotateLeft(18)
        a20 = a02.rotateLeft(62)
        a02 = a12.rotateLeft(43)
        a12 = a13.rotateLeft(25)
        a13 = a19.rotateLeft( 8)
        a19 = a23.rotateLeft(56)
        a23 = a15.rotateLeft(41)
        a15 = a04.rotateLeft(27)
        a04 = a24.rotateLeft(14)
        a24 = a21.rotateLeft( 2)
        a21 = a08.rotateLeft(55)
        a08 = a16.rotateLeft(45)
        a16 = a05.rotateLeft(36)
        a05 = a03.rotateLeft(28)
        a03 = a18.rotateLeft(21)
        a18 = a17.rotateLeft(15)
        a17 = a11.rotateLeft(10)
        a11 = a07.rotateLeft( 6)
        a07 = a10.rotateLeft( 3)
        a10 = c1

        // Chi
        c0  = a00 xor (a01.inv() and a02)
        c1  = a01 xor (a02.inv() and a03)
        a02 = a02 xor (a03.inv() and a04)
        a03 = a03 xor (a04.inv() and a00)
        a04 = a04 xor (a00.inv() and a01)
        a00 = c0
        a01 = c1

        c0  = a05 xor (a06.inv() and a07)
        c1  = a06 xor (a07.inv() and a08)
        a07 = a07 xor (a08.inv() and a09)
        a08 = a08 xor (a09.inv() and a05)
        a09 = a09 xor (a05.inv() and a06)
        a05 = c0
        a06 = c1

        c0  = a10 xor (a11.inv() and a12)
        c1  = a11 xor (a12.inv() and a13)
        a12 = a12 xor (a13.inv() and a14)
        a13 = a13 xor (a14.inv() and a10)
        a14 = a14 xor (a10.inv() and a11)
        a10 = c0
        a11 = c1

        c0  = a15 xor (a16.inv() and a17)
        c1  = a16 xor (a17.inv() and a18)
        a17 = a17 xor (a18.inv() and a19)
        a18 = a18 xor (a19.inv() and a15)
        a19 = a19 xor (a15.inv() and a16)
        a15 = c0
        a16 = c1

        c0  = a20 xor (a21.inv() and a22)
        c1  = a21 xor (a22.inv() and a23)
        a22 = a22 xor (a23.inv() and a24)
        a23 = a23 xor (a24.inv() and a20)
        a24 = a24 xor (a20.inv() and a21)
        a20 = c0
        a21 = c1

        // Iota
        a00 = a00 xor RC[rc].toByte()
    }

    A[ 0] = a00; A[ 1] = a01; A[ 2] = a02; A[ 3] = a03; A[ 4] = a04
    A[ 5] = a05; A[ 6] = a06; A[ 7] = a07; A[ 8] = a08; A[ 9] = a09
    A[10] = a10; A[11] = a11; A[12] = a12; A[13] = a13; A[14] = a14
    A[15] = a15; A[16] = a16; A[17] = a17; A[18] = a18; A[19] = a19
    A[20] = a20; A[21] = a21; A[22] = a22; A[23] = a23; A[24] = a24
}

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalArgumentException::class)
private inline fun State<*, *>.checkRounds(rounds: Byte): Unit? {
    require(rounds <= roundCount) { "rounds cannot exceed $roundCount" }
    return if (rounds < 1) null else Unit
}

/**
 * Implementation of the Keccak-p function
 *
 * @see [State]
 * @throws [IllegalArgumentException] If [numRounds] exceeds [State.roundCount]
 * @suppress
 * */
@Deprecated(
    message = "Use State.keccakP extension",
    replaceWith = ReplaceWith("state.keccakP(numRounds)")
)
@JvmOverloads
@Suppress("FunctionName")
@Throws(IllegalArgumentException::class)
public fun <N: Number> KeccakP(state: State<N, *>, numRounds: Byte = state.roundCount) {
    when (state) {
        is F200 -> state.keccakP(numRounds)
        is F400 -> state.keccakP(numRounds)
        is F800 -> state.keccakP(numRounds)
        is F1600 -> state.keccakP(numRounds)
    }
}
