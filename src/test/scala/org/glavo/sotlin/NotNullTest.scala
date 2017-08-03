package org.glavo.sotlin

/**
  * Created by Glavo on 17-8-3.
  *
  * @author Glavo
  * @since 0.1.0
  */
object NotNullTest {
    def test[A](a: A)(implicit nn: NotNull[a.type]): A = a

    def main(args: Array[String]): Unit = {
        val a: String = ""

        implicit val anonnull = NotNull(a)

        test(a)


    }
}
