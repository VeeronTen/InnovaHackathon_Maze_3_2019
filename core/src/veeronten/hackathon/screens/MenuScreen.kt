package veeronten.hackathon.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import veeronten.hackathon.*
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator


class MenuScreen(private val game: TheMazeGame) : Screen {

    private lateinit var stage: Stage
    private lateinit var group: VerticalGroup

    private lateinit var regenerateBtn: TextButton
    lateinit var mazeWidthSlider: Slider
    lateinit var mazeHeightSlider: Slider

    private lateinit var addTunnelBtn: TextButton

    private lateinit var addRoomBtn: TextButton
    private lateinit var startBtn: TextButton

    //todo fix font
    private val skin = Skin().apply {
        addRegions(TextureAtlas(Gdx.files.internal("uiskin.atlas")))

        val generator = FreeTypeFontGenerator(Gdx.files.internal("default.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 60
        val font12 = generator.generateFont(parameter)

        generator.dispose()

        add("default-font", font12)
        load(Gdx.files.internal("uiskin.json"))
    }

    var mazeWidth = MazeSource.mazeX
    var mazeHeight = MazeSource.mazeY

    override fun show() {
        stage = Stage()
        group = VerticalGroup()

        group.setBounds(WIDTH.toFloat() - 100, 0F, WIDTH.toFloat(), HEIGHT.toFloat())
        group.width = 50F

        game.inputMultiplexer.addProcessor(stage)

        regenerateBtn = createTextBtn("REGENERATE!") { MazeSource.generate(mazeWidth, mazeHeight) }


        mazeWidthSlider = Slider(5F, 100F, 1F, false, skin).apply {
            addListener {
                mazeWidth = mazeWidthSlider.value.toInt()
                return@addListener false
            }
        }
        //todo починить слайдеры
        mazeHeightSlider = Slider(5F, 100F, 1F, false, skin).apply {
            addListener {
                mazeHeight = mazeHeightSlider.value.toInt()
                return@addListener false
            }
        }

        addTunnelBtn = createTextBtn("ADD TUNNEL!") { MazeSource.addTunnel() }

//        tunnelMinLengthSlider = Slider(2, m)


        addRoomBtn = createTextBtn("ADD ROOM!") { MazeSource.addRoom() }

        startBtn = createTextBtn("START") {
            game.inputMultiplexer.removeProcessor(stage)
            game.screen = MazeScreen(game)
        }

//todo не видит через таргеты
//todo или не прорисовывается там, где был таргет

        stage.addActor(group)
        group.addActor(regenerateBtn)
        group.addActor(mazeHeightSlider)
        group.addActor(mazeWidthSlider)
        group.addActor(addTunnelBtn)
        group.addActor(addRoomBtn)
        group.addActor(startBtn)

        MazeSource.generate(mazeWidth, mazeHeight)
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
                }
            }
        }
        game.batch.end()
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun hide() {}
}