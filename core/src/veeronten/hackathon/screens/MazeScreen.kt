package veeronten.hackathon.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import veeronten.hackathon.*

class MazeScreen(val game: TheMazeGame) : Screen {

    lateinit var stage: Stage
    lateinit var group: VerticalGroup
    lateinit var backBtn: TextButton

    lateinit var straggler: Straggler

    override fun show() {
        stage = Stage()
        group = VerticalGroup()

        group.setBounds(WIDTH.toFloat() - 100, 0F, WIDTH.toFloat(), HEIGHT.toFloat())
        group.width = 50F

        game.inputMultiplexer.addProcessor(stage)

        backBtn = createTextBtn("BACK") {
            game.inputMultiplexer.removeProcessor(stage)
            game.screen = MenuScreen(game)
        }

        stage.addActor(group)
        group.addActor(backBtn)

        straggler = Straggler()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.camera.update()
        game.batch.projectionMatrix = game.camera.combined
        game.batch.begin()
        for (i in 0 until MazeSource.mazeArray.size) {
            for (j in 0 until MazeSource.mazeArray[i].size) {

                when (MazeSource.mazeArray[i][j]) {
                    WALL_VISIBLE -> game.batch.drawByCoord(game.wallVisibleImg, j, i)
                    WALL_INVISIBLEE -> game.batch.drawByCoord(game.wallInvisible, j, i)
                    KNOWN_FLOOR -> game.batch.drawByCoord(game.knownFloor, j, i)
                }

                game.batch.drawByCoord(game.stragglerImg, straggler.logicX, straggler.logicY)

            }
        }
        game.batch.end()
        stage.draw()

//        straggler.timeGone(delta)
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun hide() {}
}