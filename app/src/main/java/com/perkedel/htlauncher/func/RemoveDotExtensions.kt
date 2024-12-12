package com.perkedel.htlauncher.func

fun removeDotExtensions(from:String = "", extensionToRemove:String = ""):String{
    // https://www.techiedelight.com/remove-extension-from-file-name-in-kotlin/
    if(extensionToRemove.isBlank())
        return from.substring(0, from.lastIndexOf(".$extensionToRemove") )
    else return from.substring(0, from.lastIndexOf('.') )
}