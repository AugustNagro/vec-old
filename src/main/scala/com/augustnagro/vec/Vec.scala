package com.augustnagro.vec

import scala.reflect.ClassTag
import scala.collection.immutable.{
  AbstractSeq,
  ArraySeq,
  IndexedSeqOps,
  StrictOptimizedSeqOps
}
import scala.compiletime.*
import java.util.Arrays
import scala.collection.{
  IterableFactoryDefaults,
  SeqFactory,
  StrictOptimizedSeqFactory,
  mutable
}
import scala.collection.generic.DefaultSerializable

class Vec[+A] private (
    arr: Array[?],
    _length: Int
) extends AbstractSeq[A],
      IndexedSeq[A],
      IndexedSeqOps[A, Vec, Vec[A]],
      StrictOptimizedSeqOps[A, Vec, Vec[A]],
      IterableFactoryDefaults[A, Vec],
      DefaultSerializable:

  override def appended[B >: A](elem: B): Vec[B] = ???

  override def appendedAll[B >: A](suffix: IterableOnce[B]): Vec[B] = ???

  override def className: String = super.className

  override def copyToArray[B >: A](xs: Array[B], start: Int, len: Int): Int =
    ???

  override def drop(n: Int): Vec[A] = ???

  override def dropRight(n: Int): Vec[A] = ???

  override def foreach[U](f: A => U): Unit = ???

  override def head: A = ???

  override def iterableFactory: SeqFactory[Vec] = Vec

  override def iterator: Iterator[A] = ???

  override def last: A = ???

  override def prepended[B >: A](elem: B): Vec[B] = ???

  override def prependedAll[B >: A](prefix: IterableOnce[B]): Vec[B] = ???

  override def tail: Vec[A] = ???

  override def take(n: Int): Vec[A] = ???

  override def takeRight(n: Int): Vec[A] = ???

  override def updated[B >: A](index: Int, elem: B): Vec[B] = ???

  override def apply(i: Int): A = ???

  override def length: Int = _length

object Vec extends StrictOptimizedSeqFactory[Vec]:
  override def from[A](source: IterableOnce[A]): Vec[A] = ???

  private val emptyVec = new Vec[Nothing](Array.emptyObjectArray, 0)

  override def empty[A]: Vec[A] = emptyVec

  override def newBuilder[A]: mutable.Builder[A, Vec[A]] = ???

  private class VecBuilder[A: ClassTag] extends mutable.Builder[A, Vec[A]]:
    var arrays: Array[Array[A]] = null
    var arraysLen = 0
    var size = 0

    override def clear(): Unit =
      arrays = null
      size = 0

    override inline def addOne(elem: A): VecBuilder.this.type =
      if arraysLen == 0 then


      val idx = arraysLen * 32 - size - 32
      if idx > 0 then
        arrays(arraysLen - 1)(idx) = elem
      else
        arrays()
      this



    override def result(): Vec[A] = ???



private inline def getLevel(length: Int): Int =
  val log2Ceil = 32 - Integer.numberOfLeadingZeros(length - 1)
  // log_32(len) = log_2(len) / log_2(32)
  // = log_2(len) / 5
  // round-up division formula: (num + divisor - 1) / divisor
  // = (log_2(len) + 5 - 1) / 5
  (log2Ceil + 4) / 5

// todo consider memoizing in an array
private inline def levelElemSize(numLevels: Int, level: Int, len: Int): Int =
  var lenRemainder = len
  var l = numLevels - 1
  var elemSize = 0
  while l >= level do
    elemSize = (lenRemainder + 31) / 32
    lenRemainder -= elemSize
    l -= 1
  elemSize

/*
  inline def apply[T](ts: T*): Vec[T] =
    inline erasedValue[T] match
      case _: Int =>
        ts match
          case as: ArraySeq[?] =>
            val cTag = summon[ClassTag[Int]].asInstanceOf[ClassTag[T]]
            applyImpl(as.unsafeArray.asInstanceOf[Array[T]])(using cTag)
          case _ => applySeqImpl(ts)

//  class Builder[T: ClassTag]:
//    private var parent: Array[Object] = null
//    private var arr: Array[T] = null
//    private var totalLen = 0
//    def addOne(t: T): Unit =
//      val level = getLevel(totalLen)
//      // ie, totalLen + 1 > 32^level
//      val newLevelRequired = (totalLen + 1) > (32 << (level * 5))
//      if newLevelRequired then
//        if parent eq null then
//          parent = new Array[Object](1)
//        else
//          val newParent = new Array[Object](1)
//          newParent(0) = parent

  private inline def getLevel(length: Int): Int =
    val log2Ceil = 32 - Integer.numberOfLeadingZeros(length - 1)
    // log_32(len) = log_2(len) / log_2(32)
    // = log_2(len) / 5
    // round-up division formula: (num + divisor - 1) / divisor
    // = (log_2(len) + 5 - 1) / 5
    (log2Ceil + 4) / 5

  // todo consider memoizing in an array
  private inline def levelElemSize(numLevels: Int, level: Int, len: Int): Int =
    var lenRemainder = len
    var l = numLevels - 1
    var elemSize = 0
    while l >= level do
      elemSize = (lenRemainder + 31) / 32
      lenRemainder -= elemSize
      l -= 1
    elemSize

  private def applyLoop[T: ClassTag](
      numLevels: Int,
      level: Int,
      levelElemSizes: Array[Int],
      parent: Array[Object],
      arr: Array[T],
      arrPos: Int
  ): Array[Object] =
    val elemSize = levelElemSizes(level)


  private def applyImpl[T: ClassTag](arr: Array[T]): Vec[T] =
    val len = arr.length
    if len <= 32 then return new Vec[T](arr, len)

    // todo make applyLoop recursion
    val numLevels = getLevel(len)
    val level0Size = levelElemSize(numLevels, 0, len)
    val resArr = new Array[Object](level0Size)
    var parent = resArr
    var arrPos = 0
    var level = 1
    while level < numLevels do
      var elemSize = levelElemSize(numLevels, level, len)
      while elemSize > 0 do
        val elemArrSize = if elemSize < 32 then elemSize else 32
        if level == numLevels - 1 then
          val elemArr = new Array[T](elemArrSize)
          System.arraycopy(arr, arrPos, elemArr, 0, elemArrSize)
          arrPos += elemArrSize
        else val elemArr = new Array[Object](elemArrSize)

        elemSize -= elemArrSize
      level += 1

  extension [A](vec: Vec[A])
    inline def show: String =
      inline erasedValue[A] match
        case _: Int => Arrays.toString(vec.asInstanceOf[Array[Int]])

    def foreach(fn: A => Any): Unit =
      val len = vec.length
      len >> 5 match
        case 0 =>
          val arr = vec.arr.asInstanceOf[Array[A]]
          var i = 0
          while i < len do
            fn(arr(i))
            i += 1
        case 1 =>
          val l0Arr = vec.arr.asInstanceOf[Array[Array[A]]]
          var i = 0
          while i < l0Arr.length do
            val l1Arr = l0Arr(i)
            var j = 0
            while j < l1Arr.length do
              fn(l1Arr(j))
              j += 1
            i += 1

    inline def map[B](fn: A => B): Vec[B] =
      val cTag: ClassTag[B] = summonFrom[B]:
        case ctag: ClassTag[B] => ctag
        case _ => summon[ClassTag[Object]].asInstanceOf[ClassTag[B]]
      mapImpl(vec, fn)(using cTag)

  private def mapImpl[A, B: ClassTag](vec: Vec[A], fn: A => B): Vec[B] =
    val len = vec.length
    len >> 5 match
      case 0 =>
        val oldL0Arr = vec.arr.asInstanceOf[Array[A]]
        val l0Arr = new Array[B](len.toInt)
        var i = 0
        while i < len do
          l0Arr(i) = fn(oldL0Arr(i))
          i += 1
        new Vec(l0Arr, len)
      case 1 =>
        val oldL0Arr = vec.arr.asInstanceOf[Array[A]]
        val l0Arr = new Array[Object](32)
        var i, j = 0
        while i < 32 do

          val l2Arr = new Array[B](32)
          i += 1

    inline infix def :+(a: A): Vec[A] = ???

    inline infix def updated(index: Int, elem: A): Vec[A] = ???
 */
