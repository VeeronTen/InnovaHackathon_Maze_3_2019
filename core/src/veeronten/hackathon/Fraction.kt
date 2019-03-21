package veeronten.hackathon

class Fraction(numerator: Int, denominator: Int) {

    val numerator: Int
    val denominator: Int

    init {
        if (numerator == 0) {
            this.numerator = numerator
            this.denominator = denominator
        } else {
            val nod = nod(numerator, denominator)
            this.numerator = numerator / nod
            this.denominator = denominator / nod

        }
    }

    fun multiply(int: Int) = Fraction(numerator * int, denominator)

    fun multiply(fraction: Fraction) = Fraction(numerator * fraction.numerator, denominator * fraction.denominator)

    fun plus(fraction: Fraction) = Fraction(numerator + fraction.numerator, denominator)

    fun toFloat() = numerator.toFloat() / denominator.toFloat()

    override fun toString() = "$numerator/$denominator"

    private fun nok(a: Int, b: Int): Int {
        var a = a
        var b = b

        val mult = a * b
        while (a != 0 && b != 0) {
            if (a > b) {
                a %= b
            } else {
                b %= a
            }
        }

        return mult / (a + b)
    }

    private fun nod(a: Int, b: Int): Int {
        var a = a
        var b = b

        while (a != 0 && b != 0) {
            if (a > b) {
                a %= b
            } else {
                b %= a
            }
        }

        return a + b
    }

    override fun equals(other: Any?) =
            if (other is Fraction) {
                numerator == other.numerator && denominator == other.denominator
            } else false

}