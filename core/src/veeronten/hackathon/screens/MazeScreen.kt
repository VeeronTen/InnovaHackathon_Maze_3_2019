package veeronten.hackathon.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import veeronten.hackathon.*

class MazeScreen(private val game: TheMazeGame) : Screen {

    private lateinit var stage: Stage
    private lateinit var group: VerticalGroup

    private lateinit var waitTillMoveLabel: Label
    private lateinit var waitTillMoveSlider: Slider

    private lateinit var emptyLine1: Label

    private lateinit var backBtn: TextButton

    private lateinit var straggler: Straggler

    private val skin = Skin().apply {
        addRegions(TextureAtlas(Gdx.files.internal("uiskin.atlas")))
        add("default-font", BitmapFont())
        load(Gdx.files.internal("uiskin.json"))
    }

    override fun show() {

        straggler = Straggler()

        stage = Stage()
        group = VerticalGroup()

        group.setBounds(WIDTH - 100, 0F, WIDTH, HEIGHT)
        group.width = 50F

        game.inputMultiplexer.addProcessor(stage)

        waitTillMoveLabel = Label("Move every 1.0 seconds", skin)
        waitTillMoveSlider = createSlider(0F, 5F, 1F, 0.1F, skin) {
            waitTillMoveLabel.setText("Move every ${((it * 10).toInt().toFloat() / 10)} seconds")
            straggler.waitTillMove = it
        }

        emptyLine1 = Label("\n", skin)

        backBtn = createTextBtn("[BACK]") {
            game.inputMultiplexer.removeProcessor(stage)
            game.screen = MenuScreen(game)
        }

        stage.addActor(group)
        group.apply {
            addActor(waitTillMoveLabel)
            addActor(waitTillMoveSlider)
            addActor(emptyLine1)
            addActor(backBtn)
        }
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