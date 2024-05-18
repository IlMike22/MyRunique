package com.example.auth.domain

data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasDigit: Boolean = false,
    val hasLowerCaseCharacter: Boolean = false,
    val hasUpperCaseCharacter: Boolean = false
) {
    val isValidPassword: Boolean
        get() = hasMinLength && hasDigit && hasLowerCaseCharacter && hasUpperCaseCharacter
}
