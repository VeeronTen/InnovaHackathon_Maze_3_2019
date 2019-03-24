package veeronten.hackathon

import com.badlogic.gdx.math.MathUtils
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

class Straggler {

    var position = Point(0, 0)

    private var timeWaiting: Float = 0F
    var waitTillMove: Float = 1F

    private var pointsToWatch = GdxArray<Point>()
    private var visiblePoints = GdxArray<Point>()

    var targets = GdxArray<Point>()

    private var path = GdxArray<Point>()

    init {
        var startPoint: Point

        do {
            startPoint = Point(MathUtils.random(1, MazeSource.mazeX - 2), MathUtils.random(1, MazeSource.mazeY - 2))
        } while (MazeSource.getValueByPoint(startPoint) != EMPTY)

        position = startPoint

        for (y in 0 until MazeSource.mazeY) {
            for (x in 0 until MazeSource.mazeX) {
                val point = Point(x, y)
                if (MazeSource.getValueByPoint(point) == WALL_VISIBLE) {
                    MazeSource.setValueByPoint(point, WALL_INVISIBLE)
                }
            }
        }

        watch()
        findTargets()
        setTargetPathIfNeed()
    }

    fun timeGone(timeGone: Float) {
        timeWaiting += timeGone
        if (timeWaiting > waitTillMove) {
            move()
            watch()
            findTargets()
            setTargetPathIfNeed()
            timeWaiting = 0F
        }
    }

    private fun watch() {
        val oldVisible = GdxArray<Point>()
        oldVisible.addAll(visiblePoints)

        pointsToWatch.clear()
        visiblePoints.clear()

        pointsToWatch.add(position)

        pointsToWatch.forEach { point ->

            setVisible(point)

            if (MazeSource.getValueByPoint(point) != WALL_VISIBLE) {

                val pointWithIncX = point.withIncX()
                val pointWithIncY = point.withIncY()
                val pointWithDecX = point.withDecX()
                val pointWithDecY = point.withDecY()

                if (point.x - 1 != -1 && pointIsVisible(pointWithDecX) && MazeSource.getValueByPoint(pointWithDecX) != WALL_VISIBLE) {
                    if (!visiblePoints.contains(pointWithDecX)) {
                        pointsToWatch.add(pointWithDecX)
                        visiblePoints.add(pointWithDecX)
                    }
                }
                if (point.x + 1 != MazeSource.mazeX && pointIsVisible(pointWithIncX) && MazeSource.getValueByPoint(pointWithIncX) != WALL_VISIBLE) {
                    if (!visiblePoints.contains(pointWithIncX)) {
                        pointsToWatch.add(pointWithIncX)
                        visiblePoints.add(pointWithIncX)
                    }
                }
                if (point.y - 1 != -1 && pointIsVisible(pointWithDecY) && MazeSource.getValueByPoint(pointWithDecY) != WALL_VISIBLE) {
                    if (!visiblePoints.contains(pointWithDecY)) {
                        pointsToWatch.add(pointWithDecY)
                        visiblePoints.add(pointWithDecY)
                    }
                }
                if (point.y + 1 != MazeSource.mazeY && pointIsVisible(pointWithIncY) && MazeSource.getValueByPoint(pointWithIncY) != WALL_VISIBLE) {
                    if (!visiblePoints.contains(pointWithIncY)) {
                        pointsToWatch.add(pointWithIncY)
                        visiblePoints.add(pointWithIncY)
                    }
                }
            }
        }

        visiblePoints.forEach {
            if (!oldVisible.contains(it)) oldVisible.add(it)
        }
        visiblePoints.clear()
        visiblePoints.addAll(oldVisible)
        pointsToWatch.clear()
    }

    private fun findTargets() {
        targets.clear()
        visiblePoints.forEach { point ->

            if (MazeSource.getValueByPoint(point) != WALL_VISIBLE && MazeSource.getValueByPoint(point) != WALL_INVISIBLE) {
                if (point.x - 1 != -1 && MazeSource.getValueByPoint(point.withDecX()) == EMPTY) {
                    targets.add(point.withDecX())
                }
                if (point.x + 1 != MazeSource.mazeX && MazeSource.getValueByPoint(point.withIncX()) == EMPTY) {
                    targets.add(point.withIncX())
                }
                if (point.y - 1 != -1 && MazeSource.getValueByPoint(point.withDecY()) == EMPTY) {
                    targets.add(point.withDecY())
                }
                if (point.y + 1 != MazeSource.mazeY && MazeSource.getValueByPoint(point.withIncY()) == EMPTY) {
                    targets.add(point.withIncY())
                }
            }
        }
    }

    private fun pointIsVisible(point: Point): Boolean {
        val n = max(abs(point.x - position.x), abs(point.y - position.y))
        for (step in 0..n) {
            val t: Float = if (n == 0) 0F else (step.toFloat() / n.toFloat())

            val point = Point(lerp(position.x.toFloat(), point.x.toFloat(), t).roundToInt(), lerp(position.y.toFloat(), point.y.toFloat(), t).roundToInt())

            if ((MazeSource.getValueByPoint(point) == WALL_VISIBLE || MazeSource.getValueByPoint(point) == WALL_INVISIBLE) && step != n)
                return false
        }
        return true
    }

    private fun setVisible(point: Point) {
        when (MazeSource.getValueByPoint(point)) {
            EMPTY -> {
                MazeSource.setValueByPoint(point, KNOWN_FLOOR)
            }
            WALL_INVISIBLE -> {
                MazeSource.setValueByPoint(point, WALL_VISIBLE)
            }
        }
    }

    private fun setTargetPathIfNeed() {
        if (path.isEmpty || !targets.contains(path.last())) {
            path.clear()
            path.addAll(pathToSquare(targets.last()))
        }
    }

    private fun move() {
        position = path[0]
        path.removeIndex(0)
        targets.removeValue(position, true)
    }

    private fun pathToSquare(targetPoint: Point): GdxArray<Point> {
        val result = GdxArray<Point>()
        val waveArray = gdxArrayOf<GdxArray<Int>>().apply {
            for (y in 0 until MazeSource.mazeY) {
                add(gdxArrayOf<Int>().apply {
                    for (x in 0 until MazeSource.mazeX) add(Int.MAX_VALUE)
                })
            }
        }

        var currentValue = 0

        waveArray[targetPoint.y][targetPoint.x] = currentValue

        while (waveArray[position.y][position.x] == Int.MAX_VALUE) {
            for (yy in 0 until MazeSource.mazeY) {
                for (xx in 0 until MazeSource.mazeX) {
                    if (waveArray[yy][xx] == currentValue) {

                        val point = Point(xx, yy)
                        if (xx - 1 != -1
                                && MazeSource.getValueByPoint(point.withDecX()) != WALL_VISIBLE
                                && MazeSource.getValueByPoint(point.withDecX()) != WALL_INVISIBLE
                                && waveArray[yy][xx - 1] > currentValue + 1)
                            waveArray[yy][xx - 1] = currentValue + 1

                        if (xx + 1 != MazeSource.mazeX
                                && MazeSource.getValueByPoint(point.withIncX()) != WALL_VISIBLE
                                && MazeSource.getValueByPoint(point.withIncX()) != WALL_INVISIBLE
                                && waveArray[yy][xx + 1] > currentValue + 1)
                            waveArray[yy][xx + 1] = currentValue + 1

                        if (yy - 1 != -1
                                && MazeSource.getValueByPoint(point.withDecY()) != WALL_VISIBLE
                                && MazeSource.getValueByPoint(point.withDecY()) != WALL_INVISIBLE
                                && waveArray[yy - 1][xx] > currentValue + 1)
                            waveArray[yy - 1][xx] = currentValue + 1

                        if (yy + 1 != MazeSource.mazeY
                                && MazeSource.getValueByPoint(point.withIncY()) != WALL_VISIBLE
                                && MazeSource.getValueByPoint(point.withIncY()) != WALL_INVISIBLE
                                && waveArray[yy + 1][xx] > currentValue + 1)
                            waveArray[yy + 1][xx] = currentValue + 1
                    }
                }
            }
            currentValue++
        }

        currentValue = waveArray[position.y][position.x]

        var pointToMove = position

        while (pointToMove != targetPoint) {
            when {
                waveArray[pointToMove.y + 1][pointToMove.x] < currentValue -> {
                    currentValue = waveArray[pointToMove.y + 1][pointToMove.x]
                    result.add(pointToMove.withIncY())
                    pointToMove = pointToMove.withIncY()
                }
                waveArray[pointToMove.y - 1][pointToMove.x] < currentValue -> {
                    currentValue = waveArray[pointToMove.y - 1][pointToMove.x]
                    result.add(pointToMove.withDecY())
                    pointToMove = pointToMove.withDecY()
                }
                waveArray[pointToMove.y][pointToMove.x - 1] < currentValue -> {
                    currentValue = waveArray[pointToMove.y][pointToMove.x - 1]
                    result.add(pointToMove.withDecX())
                    pointToMove = pointToMove.withDecX()
                }
                waveArray[pointToMove.y][pointToMove.x + 1] < currentValue -> {
                    currentValue = waveArray[pointToMove.y][pointToMove.x + 1]
                    result.add(pointToMove.withIncX())
                    pointToMove = pointToMove.withIncX()
                }
            }
        }

        return result
    }

}