package veeronten.hackathon

import com.badlogic.gdx.math.MathUtils
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import kotlin.math.min

const val EMPTY: Int = 0
const val WALL_VISIBLE: Int = 1
const val WALL_INVISIBLE: Int = 2
const val KNOWN_FLOOR: Int = 3

//todo bug могут появляться нетронутые участки стен

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

    fun generate(width: Int, height: Int) {
        mazeX = width
        mazeY = height

        configStructureMaxSize = min(width, height) - 2

        startPoints.clear()
        setEmptyMaze()

        startPoints.add(Point(1, 1))
        startPoints.forEach {
            generateWays(it)
        }
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

        stamp(tunnel, 1, 1, mazeX - 1, mazeY - 1)
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

        stamp(room, 1, 1, mazeX - 1, mazeY - 1)
    }

    private fun stamp(structure: GdxArray<GdxArray<Int>>, minX: Int, minY: Int, maxX: Int, maxY: Int) {
        val sizeX = structure[0].size
        val sizeY = structure.size

//        for (y in 0 until sizeY) {
//            for (x in 0 until sizeX) {
//                print(structure[y][x])
//            }
//            println()
//        }
//        println()

        val fromX = MathUtils.random(minX, maxX - sizeX)
        val fromY = MathUtils.random(minY, maxY - sizeY)

        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                mazeArray[fromY + y][fromX + x] = structure[y][x]
            }
        }
    }

    fun getValueByPoint(point: Point): Int = mazeArray[point.y][point.x]
    fun setValueByPoint(point: Point, value: Int) {
        mazeArray[point.y][point.x] = value
    }
}