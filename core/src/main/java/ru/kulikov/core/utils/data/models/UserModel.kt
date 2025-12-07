package ru.kulikov.core.utils.data.models

import ru.kulikov.core.utils.domain.entities.User

data class UserModel(
    override val userId: Int,
    override val avatarUrl: String,
    override val name: String,
) : User