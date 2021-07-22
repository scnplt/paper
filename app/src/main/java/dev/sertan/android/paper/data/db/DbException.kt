package dev.sertan.android.paper.data.db

sealed class DbException : Exception() {
    object DataNotFound : DbException()
}
