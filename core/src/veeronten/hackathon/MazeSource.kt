package veeronten.hackathon

import com.badlogic.gdx.math.MathUtils
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf

const val EMPTY: Int = 0
const val WALL: Int = 1

//todo bug могут появляться нетронутые участки стен

object MazeSource {

    private var tunnelsCount = 5
    private var tunnelMaxLength = 10

    private val roomsCount = 6
    private val roomMixSize = 8

    var mazeArray = GdxArray<GdxArray<Int>>()

    var mazeX = 50
    var mazeY = 50

    //todo stack
    private val startPoints = GdxArray<Pair<Int, Int>>()

    init {
        setEmptyMaze()

        startPoints.add(Pair(1, 1))
        startPoints.forEach {
            generateWays(it.first, it.second)
        }

//        for (i in 0 until tunnelsCount) addTunnel()
//        for (i in 0 until roomsCount) addRoom()

    }


    // 0
    //3 1
    // 2
    private fun generateWays(x: Int, y: Int) {

        var x = x
        var y = y

        var up = true
        var right = true
        var down = true
        var left = true

        mazeArray[y][x] = EMPTY

        while (true) {
            if (x - 1 == 0 || mazeArray[y][x - 1] == EMPTY || squareHaveMoreThan1Way(x - 1, y)) {
                left = false
            }
            if (x + 1 == mazeX - 1 || mazeArray[y][x + 1] == EMPTY || squareHaveMoreThan1Way(x + 1, y)) {
                right = false
            }
            if (y - 1 == 0 || mazeArray[x][y - 1] == EMPTY || squareHaveMoreThan1Way(x, y - 1)) {
                up = false
            }
            if (y + 1 == mazeY - 1 || mazeArray[x][y + 1] == EMPTY || squareHaveMoreThan1Way(x, y + 1)) {
                down = false
            }

//            println("       $up")
//            println("$left      $right")
//            println("       $down")

            val nextMove = generateNextMove(up, right, down, left)

            when (nextMove) {
                0 -> y -= 1
                1 -> x += 1
                2 -> y += 1
                3 -> x -= 1
                null -> return
            }

            startPoints.add(Pair(x, y))

            mazeArray[y][x] = EMPTY

            up = true
            right = true
            down = true
            left = true
        }
    }

    private fun squareHaveMoreThan1Way(x: Int, y: Int): Boolean {
        var ways = 0

        if (mazeArray[y + 1][x] == EMPTY) ways++
        if (mazeArray[y - 1][x] == EMPTY) ways++
        if (mazeArray[y][x + 1] == EMPTY) ways++
        if (mazeArray[y][x - 1] == EMPTY) ways++

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
                if (x == -1) {
                    return 0
                }
            }

            if (right) {
                x--
                if (x == -1) {
                    return 1
                }
            }

            if (down) {
                x--
                if (x == -1) {
                    return 2
                }
            }

            if (left) {
                x--
                if (x == -1) {
                    return 3
                }
            }

        }

        return null
    }

    private fun setEmptyMaze() {
        mazeArray.clear()
        for (y in 0 until mazeY) {
            val line = gdxArrayOf<Int>()
            for (x in 0 until mazeX) {
                line.add(WALL)
            }
            mazeArray.add(line)
        }
    }

    private fun addTunnel() {
        val vertical = chance(50)

        val length = MathUtils.random(2, tunnelMaxLength)

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

    private fun addRoom() {
        val width = MathUtils.random(2, roomMixSize)
        val height = MathUtils.random(2, roomMixSize)

        val room = GdxArray<GdxArray<Int>>().apply {
            for (y in 0 until height) {
                val line = GdxArray<Int>()
                for (x in 0 until width) {
                    line.add(EMPTY)
                }
                add(line)
            }
        }

        stamp(room, 1, 1, mazeX - 2, mazeY - 2)
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
}