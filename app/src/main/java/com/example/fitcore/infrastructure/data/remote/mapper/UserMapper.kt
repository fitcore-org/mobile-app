package com.example.fitcore.infrastructure.data.remote.mapper
import com.example.fitcore.domain.model.User
import com.example.fitcore.infrastructure.data.remote.dto.UserDto
fun UserDto.toDomain(): User { return User(id = this.id, name = this.name, username = this.username, email = this.email) }
