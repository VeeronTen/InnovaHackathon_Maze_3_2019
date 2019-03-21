package veeronten.hackathon

import com.badlogic.gdx.math.MathUtils
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

class Straggler {

    var logicX = 0
    var logicY = 0

    private var pointsToWatch = GdxArray<Pair<Int, Int>>()
    private var visiblePoints = GdxArray<Pair<Int, Int>>()

    var targets = GdxArray<Pair<Int, Int>>()

    var path = GdxArray<Pair<Int, Int>>()

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

        watch()
        findTargets()
        setTargetPathIfNeed()
    }

    var time: Float = 0F

    fun timeGone(timeGone: Float) {
        time += timeGone
        if (time > 1) {
//            logicX = targets.last().first
//            logicY = targets.last().second
//            time = 0F

            move()
            watch()
            findTargets()
            setTargetPathIfNeed()
            time = 0F
        }
    }

    fun watch() {
        pointsToWatch.clear()

        pointsToWatch.add(Pair(logicX, logicY))

        pointsToWatch.forEach {
            val x = it.first
            val y = it.second

            setVisible(x, y)

            if (MazeSource.mazeArray[y][x] != WALL_VISIBLE) {
                if (x - 1 != -1 && squareIsVisible(x - 1, y) && MazeSource.mazeArray[y][x - 1] != WALL_VISIBLE && MazeSource.mazeArray[y][x - 1] != KNOWN_FLOOR) {
                    val pair = Pair(x - 1, y)
                    if (!visiblePoints.contains(pair)) {
                        pointsToWatch.add(pair)
                        visiblePoints.add(pair)
                    }
                }
                if (x + 1 != MazeSource.mazeX && squareIsVisible(x + 1, y) && MazeSource.mazeArray[y][x + 1] != WALL_VISIBLE && MazeSource.mazeArray[y][x + 1] != KNOWN_FLOOR) {
                    val pair = Pair(x + 1, y)
                    if (!visiblePoints.contains(pair)) {
                        pointsToWatch.add(pair)
                        visiblePoints.add(pair)
                    }
                }
                if (y - 1 != -1 && squareIsVisible(x, y - 1) && MazeSource.mazeArray[y - 1][x] != WALL_VISIBLE && MazeSource.mazeArray[y - 1][x] != KNOWN_FLOOR) {
                    val pair = Pair(x, y - 1)
                    if (!visiblePoints.contains(pair)) {
                        pointsToWatch.add(pair)
                        visiblePoints.add(pair)
                    }
                }
                if (y + 1 != MazeSource.mazeY && squareIsVisible(x, y + 1) && MazeSource.mazeArray[y + 1][x] != WALL_VISIBLE && MazeSource.mazeArray[y + 1][x] != KNOWN_FLOOR) {
                    val pair = Pair(x, y + 1)
                    if (!visiblePoints.contains(pair)) {
                        pointsToWatch.add(pair)
                        visiblePoints.add(pair)
                    }
                }
            }
        }

        pointsToWatch.clear()
    }

    fun findTargets() {
        targets.clear()
        visiblePoints.forEach {
            val x = it.first
            val y = it.second

            if (MazeSource.mazeArray[y][x] != WALL_VISIBLE && MazeSource.mazeArray[y][x] != WALL_INVISIBLEE) {
                if (x - 1 != -1 && MazeSource.mazeArray[y][x - 1] == EMPTY) {
                    targets.add(Pair(x - 1, y))
                }
                if (x + 1 != MazeSource.mazeX && MazeSource.mazeArray[y][x + 1] == EMPTY) {
                    targets.add(Pair(x + 1, y))
                }
                if (y - 1 != -1 && MazeSource.mazeArray[y - 1][x] == EMPTY) {
                    targets.add(Pair(x, y - 1))
                }
                if (y + 1 != MazeSource.mazeY && MazeSource.mazeArray[y + 1][x] == EMPTY) {
                    targets.add(Pair(x, y + 1))
                }
            }
        }
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

    fun setTargetPathIfNeed() {
        if (path.isEmpty) {
            path.addAll(pathToSquare(targets.last().first, targets.last().second))
        }
    }

    fun move() {
        logicX = path[0].first
        logicY = path[0].second
        path.removeIndex(0)
        targets.removeValue(Pair(logicX, logicY), true)
    }

    fun pathToSquare(targetX: Int, targetY: Int): GdxArray<Pair<Int, Int>> {
        var result = GdxArray<Pair<Int, Int>>()
        var waveArray = gdxArrayOf<GdxArray<Int>>().apply {
            for (y in 0 until MazeSource.mazeY) {
                add(gdxArrayOf<Int>().apply {
                    for (x in 0 until MazeSource.mazeX) add(Int.MAX_VALUE)
                })
            }
        }

        var currentValue = 0
        var targetX = targetX
        var targetY = targetY

        waveArray[targetY][targetX] = currentValue

        while (waveArray[logicY][logicX] == Int.MAX_VALUE) {
            for (yy in 0 until MazeSource.mazeY) {
                for (xx in 0 until MazeSource.mazeX) {
                    if (waveArray[yy][xx] == currentValue) {

                        if (xx - 1 != -1
                                && MazeSource.mazeArray[yy][xx - 1] != WALL_VISIBLE
                                && MazeSource.mazeArray[yy][xx - 1] != WALL_INVISIBLEE
                                && waveArray[yy][xx - 1] > currentValue + 1)
                            waveArray[yy][xx - 1] = currentValue + 1

                        if (xx + 1 != MazeSource.mazeX
                                && MazeSource.mazeArray[yy][xx + 1] != WALL_VISIBLE
                                && MazeSource.mazeArray[yy][xx + 1] != WALL_INVISIBLEE
                                && waveArray[yy][xx + 1] > currentValue + 1)
                            waveArray[yy][xx + 1] = currentValue + 1

                        if (yy - 1 != -1
                                && MazeSource.mazeArray[yy - 1][xx] != WALL_VISIBLE
                                && MazeSource.mazeArray[yy - 1][xx] != WALL_INVISIBLEE
                                && waveArray[yy - 1][xx] > currentValue + 1)
                            waveArray[yy - 1][xx] = currentValue + 1

                        if (yy + 1 != MazeSource.mazeY
                                && MazeSource.mazeArray[yy + 1][xx] != WALL_VISIBLE
                                && MazeSource.mazeArray[yy + 1][xx] != WALL_INVISIBLEE
                                && waveArray[yy + 1][xx] > currentValue + 1)
                            waveArray[yy + 1][xx] = currentValue + 1
                    }
                }
            }
            currentValue++
        }

        currentValue = waveArray[logicY][logicX]

        var xToMove = logicX
        var yToMove = logicY

        while (xToMove != targetX || yToMove != targetY) {
            when {
                waveArray[yToMove + 1][xToMove] < currentValue -> {
                    currentValue = waveArray[yToMove + 1][xToMove]
                    result.add(Pair(xToMove, yToMove + 1))
                    yToMove++
                }
                waveArray[yToMove - 1][xToMove] < currentValue -> {
                    currentValue = waveArray[yToMove - 1][xToMove]
                    result.add(Pair(xToMove, yToMove - 1))
                    yToMove--
                }
                waveArray[yToMove][xToMove - 1] < currentValue -> {
                    currentValue = waveArray[yToMove][xToMove - 1]
                    result.add(Pair(xToMove - 1, yToMove))
                    xToMove--
                }
                waveArray[yToMove][xToMove + 1] < currentValue -> {
                    currentValue = waveArray[yToMove][xToMove + 1]
                    result.add(Pair(xToMove + 1, yToMove))
                    xToMove++
                }
            }
        }

        return result
    }

}