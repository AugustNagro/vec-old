package com.augustnagro.vec

@main def main(): Unit =
  val vec = Vec(1, 2, 3).map(_ + 1)
  println(vec.show)
