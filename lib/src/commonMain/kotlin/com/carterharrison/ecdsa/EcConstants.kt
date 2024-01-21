package com.carterharrison.ecdsa

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger

/**
 * An object to hold static values used when doing point math.
 */
object EcConstants {
    val ZERO: BigInteger = BigInteger.ZERO
    val ONE: BigInteger = 1.toBigInteger()
    val TWO: BigInteger = 2.toBigInteger()
    val THREE: BigInteger = 3.toBigInteger()
}