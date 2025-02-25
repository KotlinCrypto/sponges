# CHANGELOG

## Version 0.4.0 (2025-02-25)
 - Updates `kotlin` to `2.1.10` [[#27]][27]

## Version 0.3.4 (2024-12-28)
 - Utilize typed arrays (`LongArray`, `IntArray`, etc.) instead of generic `Array<N>` [[#25]][25]
 - Optimize `addData` [[#23]][23]

## Version 0.3.3 (2024-12-20)
 - Fixes boxing issue for Jvm due to use of generics [[#21]][21]

## Version 0.3.2 (2024-12-19)
 - Adds benchmarking to repository [[#16]][16] [[#17]][17]
 - Refactors `State` internals (performance improvements) [[#19][19]
     - Deprecates `KeccakP` in favor of new `State.keccakP` extension

## Version 0.3.1 (2024-08-31)
 - Updates dependencies
     - Kotlin `1.9.23` -> `1.9.24`
 - Fixes multiplatform metadata manifest `unique_name` parameter for
   all source sets to be truly unique.
 - Updates jvm `.kotlin_module` with truly unique file name.

## Version 0.3.0 (2024-03-18)
 - Updates dependencies
     - Kotlin `1.9.21` -> `1.9.23`
 - Add experimental support for `wasmJs` & `wasmWasi`
 - Add support for Java9 `JPMS` via Multi-Release jar

## Version 0.2.0 (2023-11-30)
 - Updates dependencies
     - Kotlin `1.8.10` -> `1.9.21`
 - Drops support for the following deprecated targets:
     - `iosArm32`
     - `watchosX86`
     - `linuxArm32Hfp`
     - `linuxMips32`
     - `linuxMipsel32`
     - `mingwX86`
     - `wasm32`

## Version 0.1.0 (2023-04-07)
 - Initial Release

[16]: https://github.com/KotlinCrypto/sponges/pull/16
[17]: https://github.com/KotlinCrypto/sponges/pull/17
[19]: https://github.com/KotlinCrypto/sponges/pull/19
[21]: https://github.com/KotlinCrypto/sponges/pull/21
[23]: https://github.com/KotlinCrypto/sponges/pull/23
[25]: https://github.com/KotlinCrypto/sponges/pull/25
[27]: https://github.com/KotlinCrypto/sponges/pull/27
