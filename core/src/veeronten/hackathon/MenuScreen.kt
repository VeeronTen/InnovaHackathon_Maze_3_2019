package veeronten.hackathon

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20

class MenuScreen(val game: TheMazeGame) : Screen {

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.camera.update()
        game.batch.projectionMatrix = game.camera.combined
        game.batch.begin()
        for (i in 0 until MazeSource.mazeArray.size) {
            for (j in 0 until MazeSource.mazeArray[i].size) {
                if (MazeSource.mazeArray[i][j] == WALL) {
                    game.batch.draw(game.wallVisibleImg, TILE_SIZE.toFloat() * j, HEIGHT - TILE_SIZE.toFloat() * (i + 1), TILE_SIZE.toFloat(), TILE_SIZE.toFloat())
                }
            }
        }
        game.batch.end()
    }

    override fun hide() {}

    override fun show() {}

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun dispose() {}
}