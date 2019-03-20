package veeronten.hackathon

import com.badlogic.gdx.math.MathUtils

class Straggler {

    var logicX = 0
    var logicY = 0
//    var worldX = 0
//    var worldY = 0

    init {
        var x: Int
        var y: Int

        do {
            x = MathUtils.random(1, MazeSource.mazeX - 2)
            y = MathUtils.random(1, MazeSource.mazeY - 2)
        } while (MazeSource.mazeArray[y][x] != EMPTY)

        logicX = x
        logicY = y
    }
}