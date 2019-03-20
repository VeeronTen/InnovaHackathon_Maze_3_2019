package veeronten.hackathon

import com.badlogic.gdx.math.MathUtils

fun chance(chance: Int) = MathUtils.randomBoolean(chance.toFloat() / 100)