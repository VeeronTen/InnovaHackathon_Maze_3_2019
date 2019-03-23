package veeronten.hackathon.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import veeronten.hackathon.*

class MazeScreen(private val game: TheMazeGame) : Screen {

    private lateinit var stage: Stage
    private lateinit var group: VerticalGroup
    private lateinit var backBtn: TextButton

    private lateinit var straggler: Straggler

    override fun show() {
        stage = Stage()
        group = VerticalGroup()

        group.setBounds(WIDTH.toFloat() - 100, 0F, WIDTH.toFloat(), HEIGHT.toFloat())
        group.width = 50F

        game.inputMultiplexer.addProcessor(stage)

        backBtn = createTextBtn("[BACK]") {
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
        for (y in 0 until MazeSource.mazeY) {
            for (x in 0 until MazeSource.mazeX) {
                val point = Point(x, y)
                when (MazeSource.getValueByPoint(point)) {
                    WALL_VISIBLE -> game.batch.drawByCoord(game.wallVisibleImg, point)
                    WALL_INVISIBLE -> game.batch.drawByCoord(game.wallInvisible, point)
                    KNOWN_FLOOR -> game.batch.drawByCoord(game.knownFloor, point)
                }

                game.batch.drawByCoord(game.stragglerImg, straggler.position)

                straggler.targets.forEach { point ->
                    game.batch.drawByCoord(game.targetImg, point)
                }

            }
        }
        game.batch.end()
        stage.draw()

        straggler.timeGone(delta)
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun hide() {}
}