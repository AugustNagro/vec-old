package com.augustnagro.vec

opaque type Vec[A] = Array[A]

object Vec:

  extension (vi: Vec[Int])
    inline def map(inline fn: Int => Int): Vec[Int] = ???

  private inline def map[A, B](arr: Array[A], fn: A => B): Array[B] =
    val len = arr.length
    val res = new Array[B](len)
    var i = 0
    while i < len do
      res(i) = fn()
      i += 1
