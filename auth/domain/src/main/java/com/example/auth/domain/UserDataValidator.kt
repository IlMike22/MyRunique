package com.example.auth.domain

class UserDataValidator(
    private val patternValidator: PatternValidator
) {
    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasDigit = hasDigit,
            hasLowerCaseCharacter = hasLowercase,
            hasUpperCaseCharacter = hasUppercase
        )
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 9
    }
}