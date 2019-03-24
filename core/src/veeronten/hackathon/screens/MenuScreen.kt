package veeronten.hackathon.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import veeronten.hackathon.*

class MenuScreen(private val game: TheMazeGame) : Screen {

    private lateinit var stage: Stage
    private lateinit var rootGroup: VerticalGroup

    private lateinit var mazeWidthLabel: Label
    private lateinit var mazeWidthSlider: Slider
    private lateinit var mazeHeightLabel: Label
    private lateinit var mazeHeightSlider: Slider
    private lateinit var regenerateBtn: TextButton

    private lateinit var emptyLine1: Label

    private lateinit var tunnelLengthLabel: Label
    private lateinit var tunnelLengthSlider: Slider
    private lateinit var addTunnelBtn: TextButton

    private lateinit var emptyLine2: Label

    private lateinit var roomWidthLabel: Label
    private lateinit var roomWidthSlider: Slider
    private lateinit var roomHeightLabel: Label
    private lateinit var roomHeightSlider: Slider
    private lateinit var addRoomBtn: TextButton

    private lateinit var emptyLine3: Label

    private lateinit var addExit: TextButton

    private lateinit var emptyLine4: Label

    private lateinit var startBtn: TextButton

    private val skin = Skin().apply {
        addRegions(TextureAtlas(Gdx.files.internal("uiskin.atlas")))
        add("default-font", BitmapFont())
        load(Gdx.files.internal("uiskin.json"))
    }
    //todo при старте смотреть на центр лабиринта, эффектная картинка
    override fun show() {
        stage = Stage()
        rootGroup = VerticalGroup().apply {
            setBounds(WIDTH - 100, 0F, WIDTH, HEIGHT)
            width = 50F
        }

        game.inputMultiplexer.addProcessor(stage)

        emptyLine1 = Label("\n", skin)
        emptyLine2 = Label("\n", skin)
        emptyLine3 = Label("\n", skin)
        emptyLine4 = Label("\n", skin)




        MazeSource.generate(mazeWidthDefault(), mazeHeightDefault())

        mazeWidthLabel = Label("Maze width = ${mazeWidthDefault()}", skin)
        mazeWidthSlider = createSlider(MazeSource.configMazeMinSize.toFloat(), MazeSource.configMazeMaxSize.toFloat(), mazeWidthDefault().toFloat(), 1F, skin) { mazeWidthLabel.setText("Maze width = ${it.toInt()}") }
        mazeHeightLabel = Label("Maze height = ${mazeHeightDefault()}", skin)
        mazeHeightSlider = createSlider(MazeSource.configMazeMinSize.toFloat(), MazeSource.configMazeMaxSize.toFloat(), mazeHeightDefault().toFloat(), 1F, skin) { mazeHeightLabel.setText("Maze height = ${it.toInt()}") }
        regenerateBtn = createTextBtn("[REGENERATE]") {
            MazeSource.generate(mazeWidthSlider.value.toInt(), mazeHeightSlider.value.toInt())

            tunnelLengthLabel.setText("Tunnel length is ${tunnelLengthDefault()}")
            tunnelLengthSlider.value = tunnelLengthDefault().toFloat()
            tunnelLengthSlider.setRange(MazeSource.configTunnelMinLength.toFloat(), MazeSource.configStructureMaxSize.toFloat())
            tunnelLengthSlider.value = tunnelLengthDefault().toFloat()

            roomWidthLabel.setText("Room width = ${roomWidthDefault()}")
            roomHeightLabel.setText("Room height = ${roomWidthDefault()}")
            roomWidthSlider.value = roomWidthDefault().toFloat()
            roomHeightSlider.value = roomHeightDefault().toFloat()
            roomWidthSlider.setRange(MazeSource.configRoomMinSize.toFloat(), MazeSource.configStructureMaxSize.toFloat())
            roomHeightSlider.setRange(MazeSource.configRoomMinSize.toFloat(), MazeSource.configStructureMaxSize.toFloat())
            roomWidthSlider.value = roomWidthDefault().toFloat()
            roomHeightSlider.value = roomHeightDefault().toFloat()
        }

        tunnelLengthLabel = Label("Tunnel length is ${tunnelLengthDefault()}", skin)
        tunnelLengthSlider = createSlider(MazeSource.configTunnelMinLength.toFloat(), MazeSource.configStructureMaxSize.toFloat(), tunnelLengthDefault().toFloat(), 1F, skin) { tunnelLengthLabel.setText("Tunnel length is ${it.toInt()}") }
        addTunnelBtn = createTextBtn("[ADD TUNNEL]") { MazeSource.addTunnel(tunnelLengthSlider.value.toInt()) }

        roomWidthLabel = Label("Room width = ${roomWidthDefault()}", skin)
        roomWidthSlider = createSlider(MazeSource.configRoomMinSize.toFloat(), MazeSource.configStructureMaxSize.toFloat(), roomWidthDefault().toFloat(), 1F, skin) { roomWidthLabel.setText("Room width = ${it.toInt()}")}
        roomHeightLabel = Label("Room height = ${roomHeightDefault()}", skin)
        roomHeightSlider = createSlider(MazeSource.configRoomMinSize.toFloat(), MazeSource.configStructureMaxSize.toFloat(), roomHeightDefault().toFloat(), 1F, skin) { roomHeightLabel.setText("Room height = ${it.toInt()}")}
        addRoomBtn = createTextBtn("[ADD ROOM]") { MazeSource.addRoom(roomWidthSlider.value.toInt(), roomHeightSlider.value.toInt()) }


        addExit = createTextBtn("[ADD EXIT]") { MazeSource.addExit() }

        startBtn = createTextBtn("[START]") {
            game.inputMultiplexer.removeProcessor(stage)
            game.screen = MazeScreen(game)
        }
//todo добавить комнату с колоннами и круги
//todo оптимизация
//todo концовка
//todo графика
//todo камера двигается при работе с ползунками

        stage.addActor(rootGroup)

        rootGroup.apply {
            addActor(mazeWidthLabel)
            addActor(mazeWidthSlider)
            addActor(mazeHeightLabel)
            addActor(mazeHeightSlider)
            addActor(regenerateBtn)

            addActor(emptyLine1)

            addActor(tunnelLengthLabel)
            addActor(tunnelLengthSlider)
            addActor(addTunnelBtn)

            addActor(emptyLine2)

            addActor(roomWidthLabel)
            addActor(roomWidthSlider)
            addActor(roomHeightLabel)
            addActor(roomHeightSlider)
            addActor(addRoomBtn)

            addActor(emptyLine3)

            addActor(addExit)

            addActor(emptyLine4)

            addActor(startBtn)
        }
    }

    private fun mazeWidthDefault() = (MazeSource.configMazeMinSize + MazeSource.configMazeMaxSize) / 2
    private fun mazeHeightDefault() = mazeWidthDefault()
    private fun tunnelLengthDefault() = (MazeSource.configTunnelMinLength + MazeSource.configStructureMaxSize) / 2
    private fun roomWidthDefault() = (MazeSource.configRoomMinSize + MazeSource.configStructureMaxSize) / 2
    private fun roomHeightDefault() = roomWidthDefault()

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