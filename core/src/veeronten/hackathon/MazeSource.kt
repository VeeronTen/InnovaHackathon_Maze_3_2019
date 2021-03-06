package veeronten.hackathon

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.MathUtils.cos
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

const val EMPTY: Int = 0
const val WALL_VISIBLE: Int = 1
const val WALL_INVISIBLE: Int = 2
const val KNOWN_FLOOR: Int = 3

object MazeSource {

    val configMazeMinSize = 4
    val configMazeMaxSize = 300
    var configStructureMaxSize = configMazeMaxSize - 2
    val configTunnelMinLength = 2
    val configRoomMinSize = 2

    private var mazeArray = GdxArray<GdxArray<Int>>()

    var mazeX = 50
    var mazeY = 50

    private val startPoints = GdxArray<Point>()
    val exitPoints = GdxArray<Point>()

    fun generate(width: Int, height: Int) {
        mazeX = width
        mazeY = height

        configStructureMaxSize = min(width, height) - 2

        startPoints.clear()
        exitPoints.clear()
        setEmptyMaze()

        startPoints.add(Point(1, 1))
        startPoints.forEach {
            generateWays(it)
        }
        addExit()
    }

    private fun generateWays(point: Point) {

        var point = point

        var up = true
        var right = true
        var down = true
        var left = true

        setValueByPoint(point, EMPTY)

        while (true) {
            if (point.x - 1 == 0 || getValueByPoint(point.withDecX()) == EMPTY || pointHaveMoreThan1Way(point.withDecX())) {
                left = false
            }
            if (point.x + 1 == mazeX - 1 || getValueByPoint(point.withIncX()) == EMPTY || pointHaveMoreThan1Way(point.withIncX())) {
                right = false
            }
            if (point.y - 1 == 0 || getValueByPoint(point.withDecY()) == EMPTY || pointHaveMoreThan1Way(point.withDecY())) {
                up = false
            }
            if (point.y + 1 == mazeY - 1 || getValueByPoint(point.withIncY()) == EMPTY || pointHaveMoreThan1Way(point.withIncY())) {
                down = false
            }

            val nextMove = generateNextMove(up, right, down, left)

            when (nextMove) {
                0 -> {
                    point = point.withDecY()
                }
                1 -> {
                    point = point.withIncX()
                }
                2 -> {
                    point = point.withIncY()
                }
                3 -> {
                    point = point.withDecX()
                }
                null -> return
            }

            startPoints.add(point)

            setValueByPoint(point, EMPTY)

            up = true
            right = true
            down = true
            left = true
        }
    }

    private fun pointHaveMoreThan1Way(point: Point): Boolean {
        var ways = 0

        if (getValueByPoint(point.withIncY()) == EMPTY) ways++
        if (getValueByPoint(point.withDecY()) == EMPTY) ways++
        if (getValueByPoint(point.withIncX()) == EMPTY) ways++
        if (getValueByPoint(point.withDecX()) == EMPTY) ways++

        return ways > 1
    }

    private fun generateNextMove(up: Boolean, right: Boolean, down: Boolean, left: Boolean): Int? {
        var max = -1

        if (up) max++
        if (right) max++
        if (down) max++
        if (left) max++

        if (max != -1) {
            var x = MathUtils.random(0, max)
            if (up) {
                x--
                if (x == -1) return 0
            }

            if (right) {
                x--
                if (x == -1) return 1
            }

            if (down) {
                x--
                if (x == -1) return 2
            }

            if (left) {
                x--
                if (x == -1) return 3
            }

        }

        return null
    }

    private fun setEmptyMaze() {
        mazeArray.clear()
        for (y in 0 until mazeY) {
            val line = gdxArrayOf<Int>()
            for (x in 0 until mazeX) {
                line.add(WALL_VISIBLE)
            }
            mazeArray.add(line)
        }
    }

    fun addTunnel(length: Int) {
        val vertical = chance(50)

        val tunnel = GdxArray<GdxArray<Int>>().apply {
            if (vertical) {
                for (y in 0 until length) {
                    this.add(gdxArrayOf(EMPTY))
                }
            } else {
                val line = gdxArrayOf<Int>().apply {
                    for (x in 0 until length) {
                        this.add(EMPTY)
                    }
                }
                this.add(line)
            }
        }

        stamp(tunnel, false)
    }

    fun addRoom(width: Int, height: Int) {

        val room = GdxArray<GdxArray<Int>>().apply {
            for (y in 0 until height) {
                val line = GdxArray<Int>()
                for (x in 0 until width) {
                    line.add(EMPTY)
                }
                add(line)
            }
        }

        stamp(room, false)
    }

    fun addCircleRoom(radius: Int) {
        val result = GdxArray<GdxArray<Int>>().apply {
            for (y in 0..radius * 2) {
                val line = GdxArray<Int>()
                for (x in 0..radius * 2) {
                    line.add(WALL_INVISIBLE)
                }
                add(line)
            }
            val PI: Double = 3.1415926535
            var angle: Double = 0.0
            var x1: Double
            var y1: Double

            while (angle < 360) {
                x1 = radius * cos((angle * PI / 180))

                y1 = radius * sin((angle * PI / 180))

                this[MathUtils.round((y1 + radius).toFloat())][MathUtils.round((x1 + radius).toFloat())] = EMPTY
                angle += 1.0
            }
        }

        val printEmpty = false
        for(y in 0 until result.size) {
            for(x in 0 until result[0].size) {
                if (printEmpty) {
                    result[y][x] = EMPTY
                }
            }
        }
        stamp(result, false)
    }
    private fun stamp(structure: GdxArray<GdxArray<Int>>, wallsToo: Boolean) {
        val sizeX = structure[0].size
        val sizeY = structure.size

//        for (y in 0 until sizeY) {
//            for (x in 0 until sizeX) {
//                print(structure[y][x])
//            }
//            println()
//        }
//        println()

        val fromX = MathUtils.random(1, mazeX - 1 - sizeX)
        val fromY = MathUtils.random(1, mazeY - 1 - sizeY)

        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                if (structure[y][x] == WALL_INVISIBLE && !wallsToo) {}
                else {
                    mazeArray[fromY + y][fromX + x] = structure[y][x]
                }
            }
        }
    }

    fun addExit() {

        var stepsTillAdd = MathUtils.random(1, mazeY + mazeY)
        var exit: Point? = null

        var upInfinite = false
        var rightInfinite = false
        var downInfinite = false
        var leftInfinite = false

        while ( stepsTillAdd > 0 && (!upInfinite || !rightInfinite || !downInfinite || !leftInfinite)) {
            var x = MathUtils.random(3)
            when(x) {
                0 -> {
                    if (upInfinite) {}
                    else {
                        upInfinite = true
                        do {
                            for (x in 1 until mazeX - 1) {
                                val point = Point(x, 0)
                                if (getValueByPoint(point) != EMPTY && haveOneWay(point)) {
                                    if (stepsTillAdd > 0) {
                                        upInfinite = false
                                        exit = point
                                        stepsTillAdd--
                                    }
                                }
                            }
                        } while (!upInfinite && stepsTillAdd > 0)
                    }
                }
                1 -> {
                    if (rightInfinite) {}
                    else {
                        rightInfinite = true
                        do{
                            for (y in 1 until mazeY - 1) {
                                val point = Point(mazeX - 1, y)
                                if (getValueByPoint(point) != EMPTY && haveOneWay(point)) {
                                    if (stepsTillAdd > 0) {
                                        rightInfinite = false
                                        exit = point
                                        stepsTillAdd--
                                    }
                                }
                            }
                        } while (!rightInfinite && stepsTillAdd > 0)
                    }
                }
                2 -> {
                    if (downInfinite) {}
                    else {
                        downInfinite = true
                        do {
                            for (x in 1 until mazeX - 1) {
                                val point = Point(x, mazeY - 1)
                                if (getValueByPoint(point) != EMPTY && haveOneWay(point)) {
                                    if (stepsTillAdd > 0) {
                                        downInfinite = false
                                        exit = point
                                        stepsTillAdd--
                                    }
                                }
                            }
                        } while (!downInfinite && stepsTillAdd > 0)
                    }
                }
                3 -> {
                    if (leftInfinite) {}
                    else {
                        leftInfinite = true
                        do {
                            for (y in 1 until mazeY - 1) {
                                val point = Point(0, y)
                                if (getValueByPoint(point) != EMPTY && haveOneWay(point)) {
                                    if (stepsTillAdd > 0) {
                                        leftInfinite = false
                                        exit = point
                                        stepsTillAdd--
                                    }
                                }
                            }
                        } while (!leftInfinite && stepsTillAdd > 0)
                    }
                }
            }
        }

        exit?.let {
            setValueByPoint(exit, EMPTY)
            exitPoints.add(it)
        }
    }

    private fun haveOneWay(point: Point): Boolean {
        val haveExitNear =
                ( (point.y == 0 || point.y == mazeY - 1) && (point.x - 1 != -1 && getValueByPoint(point.withDecX()) == EMPTY) )
                ||
                ( (point.y == 0 || point.y == mazeY - 1) && (point.x + 1 != mazeX && getValueByPoint(point.withIncX()) == EMPTY) )
                ||
                ( (point.x == 0 || point.x == mazeX - 1) && (point.y - 1 != -1 && getValueByPoint(point.withDecY()) == EMPTY) )
                ||
                ( (point.x == 0 || point.x == mazeX - 1) && (point.y + 1 != mazeY && getValueByPoint(point.withIncY()) == EMPTY) )

        return if (haveExitNear) false
        else (point.x - 1 != -1 && getValueByPoint(point.withDecX()) == EMPTY)
                ||
                (point.x + 1 != mazeX && getValueByPoint(point.withIncX()) == EMPTY)
                ||
                (point.y - 1 != -1 && getValueByPoint(point.withDecY()) == EMPTY)
                ||
                (point.y + 1 != mazeY && getValueByPoint(point.withIncY()) == EMPTY)

    }



    fun getValueByPoint(point: Point): Int = mazeArray[point.y][point.x]
    fun setValueByPoint(point: Point, value: Int) {
        mazeArray[point.y][point.x] = value
    }
}