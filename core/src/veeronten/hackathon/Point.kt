package veeronten.hackathon
//todo remove android
data class Point(val x: Int, val y: Int) {

    fun withIncX() = Point(x + 1, y)
    fun withIncY() = Point(x, y + 1)
    fun withDecX() = Point(x - 1, y)
    fun withDecY() = Point(x, y - 1)
}