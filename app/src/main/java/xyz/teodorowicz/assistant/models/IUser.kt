package xyz.teodorowicz.assistant.models

interface IUser {
    val uid: String
    val firstName: String
    val lastName: String
    val email: String
    val photoUrl: String
}