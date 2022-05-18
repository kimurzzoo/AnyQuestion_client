package com.client.anyquestion

data class AccountDTO(
    val name : String,
    val email : String,
    val password : String,
    val passwordConfirm : String
)

data class UserDTO(
    val email : String,
    val password : String
)

data class RegisterResultDTO(
    var ok : Boolean
)

data class TokenDTO(
    val accessToken : String,
    val refreshToken : String
)

data class TokenReissueDTO(
    val refreshToken : String?
)

data class LogoutDTO(
    val ok : Boolean
)

data class WithdrawalDTO(
    val ok : Boolean
)