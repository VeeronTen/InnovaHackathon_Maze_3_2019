package veeronten.hackathon

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener


class MenuScreen(val game: TheMazeGame) : Screen {

    lateinit var stage: Stage
    //    lateinit var table: Table
    lateinit var group: VerticalGroup

    lateinit var regenerateBtn: TextButton
    lateinit var mazeWidthSlider: Slider
    lateinit var mazeHeightSlider: Slider

    lateinit var addTunnelBtn: TextButton
//    lateinit var tunnelMinLengthSlider: Slider
//    lateinit var tunnelMaxLengthSlider: Slider

    lateinit var addRoomBtn: TextButton


    var mazeWidht = MazeSource.mazeX
    var mazeHeight = MazeSource.mazeY

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
        stage.draw()
    }

    override fun hide() {}

    override fun show() {
        stage = Stage()
        game.inputMultiplexer.addProcessor(stage)
        group = VerticalGroup()
        stage.addActor(group)

        regenerateBtn = TextButton("REGENERATE!", TextButtonStyle().apply { font = BitmapFont() }).apply {
            addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    super.clicked(event, x, y)
                    MazeSource.generate(mazeWidht, mazeHeight)
                }
            })
        }

        mazeWidthSlider = Slider(5F, 100F, 1F, false, Skin(Gdx.files.internal("uiskin.json"))).apply {
            addListener {
                mazeWidht = mazeWidthSlider.value.toInt()
                return@addListener true
            }
        }
//todo починить слайдеры
        mazeHeightSlider = Slider(5F, 100F, 1F, false, Skin(Gdx.files.internal("uiskin.json"))).apply {
            addListener {
                mazeHeight = mazeHeightSlider.value.toInt()
                return@addListener true
            }
        }

        addTunnelBtn = TextButton("ADD TUNNEL!", TextButtonStyle().apply { font = BitmapFont() }).apply {
            addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    super.clicked(event, x, y)
                    MazeSource.addTunnel()
                }
            })
        }

//        tunnelMinLengthSlider = Slider(2, m)


        addRoomBtn = TextButton("ADD ROOM!", TextButtonStyle().apply { font = BitmapFont() }).apply {
            addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    super.clicked(event, x, y)
                    MazeSource.addRoom()
                }
            })
        }

        group.setBounds(WIDTH.toFloat() - 100, 0F, WIDTH.toFloat(), HEIGHT.toFloat())
        group.width = 50F

//        group.setFillParent(true)
        group.addActor(regenerateBtn)
        group.addActor(mazeWidthSlider)
        group.addActor(mazeHeightSlider)


        group.addActor(addTunnelBtn)
        group.addActor(addRoomBtn)


    }

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun dispose() {}
}