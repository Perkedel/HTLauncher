package com.perkedel.htlauncher.data

import kotlinx.serialization.Serializable

@Serializable
data class TestJsonData(
    val test:String = "Hello world Empty"
)
