package veeronten.hackathon

import com.badlogic.gdx.math.MathUtils
import ktx.collections.GdxArray
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

class Straggler {

    var logicX = 0
    var logicY = 0
//    var worldX = 0
//    var worldY = 0
var counter = 0

    //todo stack
    private var pointsToWatch = GdxArray<Pair<Int, Int>>()
    private var stopPintsToWatch = GdxArray<Pair<Int, Int>>()

    init {

        var startX: Int
        var startY: Int

        do {
            startX = MathUtils.random(1, MazeSource.mazeX - 2)
            startY = MathUtils.random(1, MazeSource.mazeY - 2)
        } while (MazeSource.mazeArray[startY][startX] != EMPTY)

        logicX = startX
        logicY = startY

        for (y in 0 until MazeSource.mazeArray.size) {
            for (x in 0 until MazeSource.mazeArray[y].size) {
                if (MazeSource.mazeArray[y][x] == WALL_VISIBLE) {
                    MazeSource.mazeArray[y][x] = WALL_INVISIBLEE
                }
            }
        }

//        line( 3, 3, 10, 3)
//        line( 3, 3, 3, 10)
//        line( 5, 5, 5, 5)
//        line( 0, 0, 3, 7)
//        line( 0, 0, 3, 7)
//        line( 10, 10, 14, 12)
//        line( 33, 14, 1, 10)

        watch()
    }

//    fun timeGone(timeGone: Float) {
//
//    }

    fun line(startX: Int, startY: Int, endX: Int, endY: Int) {
        val N = max(abs(endX - startX), abs(endY - startY))
        for (step in 0..N) {
            val t: Float = if (N == 0) 0F else (step.toFloat() / N.toFloat())

            val x = lerp(startX.toFloat(), endX.toFloat(), t).roundToInt()
            val y = lerp(startY.toFloat(), endY.toFloat(), t).roundToInt()

            MazeSource.mazeArray[y][x] = KNOWN_FLOOR
        }
    }


    fun watch() {
        counter = 0
        pointsToWatch.clear()
        stopPintsToWatch.clear()

        pointsToWatch.add(Pair(logicX, logicY))

        pointsToWatch.forEach {
            val x = it.first
            val y = it.second

            setVisible(x, y)
//            counter++
//            println(counter)
            if (MazeSource.mazeArray[y][x] != WALL_VISIBLE) {
                if (x - 1 != -1 && squareIsVisible(x - 1, y) && MazeSource.mazeArray[y][x - 1] != WALL_VISIBLE && MazeSource.mazeArray[y][x - 1] != KNOWN_FLOOR) {
                    println("${x - 1} $y")
                    val pair = Pair(x - 1, y)
                    if (!stopPintsToWatch.contains(pair)) {
                        pointsToWatch.add(pair)
                        stopPintsToWatch.add(pair)
                    }
                }
                if (x + 1 != MazeSource.mazeX && squareIsVisible(x + 1, y) && MazeSource.mazeArray[y][x + 1] != WALL_VISIBLE && MazeSource.mazeArray[y][x + 1] != KNOWN_FLOOR) {
                    println("${x + 1} $y")
                    val pair = Pair(x + 1, y)
                    if (!stopPintsToWatch.contains(pair)) {
                        pointsToWatch.add(pair)
                        stopPintsToWatch.add(pair)
                    }
                }
                if (y - 1 != -1 && squareIsVisible(x, y - 1) && MazeSource.mazeArray[y - 1][x] != WALL_VISIBLE && MazeSource.mazeArray[y - 1][x] != KNOWN_FLOOR) {
                    println("$x ${y - 1}")
                    val pair = Pair(x, y - 1)
                    if (!stopPintsToWatch.contains(pair)) {
                        pointsToWatch.add(pair)
                        stopPintsToWatch.add(pair)
                    }
                }
                if (y + 1 != MazeSource.mazeY && squareIsVisible(x, y + 1) && MazeSource.mazeArray[y + 1][x] != WALL_VISIBLE && MazeSource.mazeArray[y + 1][x] != KNOWN_FLOOR) {
                    println("$x ${y + 1}")
                    val pair = Pair(x, y + 1)

                    if (!stopPintsToWatch.contains(pair)) {
                        pointsToWatch.add(pair)
                        stopPintsToWatch.add(pair)
                    }
                }
                println()
            }
        }

        pointsToWatch.clear()
        stopPintsToWatch.clear()
    }

    fun squareIsVisible(x: Int, y: Int): Boolean {
        val N = max(abs(x - logicX), abs(y - logicY))
        for (step in 0..N) {
            val t: Float = if (N == 0) 0F else (step.toFloat() / N.toFloat())

            val x = lerp(logicX.toFloat(), x.toFloat(), t).roundToInt()
            val y = lerp(logicY.toFloat(), y.toFloat(), t).roundToInt()

            if ((MazeSource.mazeArray[y][x] == WALL_VISIBLE || MazeSource.mazeArray[y][x] == WALL_INVISIBLEE) && step != N)
                return false
        }
        return true
    }

    fun setVisible(x: Int, y: Int) {
        when (MazeSource.mazeArray[y][x]) {
            EMPTY -> {
                MazeSource.mazeArray[y][x] = KNOWN_FLOOR
            }
            WALL_INVISIBLEE -> {
                MazeSource.mazeArray[y][x] = WALL_VISIBLE
            }
        }
    }
}