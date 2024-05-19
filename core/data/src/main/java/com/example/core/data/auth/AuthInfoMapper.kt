package com.example.core.data.auth

import com.example.core.domain.AuthInfo

fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable {
    return AuthInfoSerializable(this.accessToken, this.refreshToken, this.userId)
}

fun AuthInfoSerializable.toAuthInfo(): AuthInfo {
    return AuthInfo(this.accessToken, this.refreshToken, this.userId)
}