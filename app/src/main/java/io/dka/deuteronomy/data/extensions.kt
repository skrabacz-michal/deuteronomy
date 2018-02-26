package io.dka.deuteronomy.data

import io.dka.deuteronomy.data.model.UserEntity
import io.dka.deuteronomy.domain.model.User

fun UserEntity.map(): User = User(id = this.id, name = this.name)
fun User.map(): UserEntity = UserEntity(id = this.id, name = this.name, username = "", email = "")
