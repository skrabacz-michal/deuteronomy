package io.dka.deuteronomy.view.model

import io.dka.deuteronomy.domain.model.User

data class UsersState(val isLoading: Boolean, val users: List<User>)
