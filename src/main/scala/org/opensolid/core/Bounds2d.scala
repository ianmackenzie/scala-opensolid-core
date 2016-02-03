////////////////////////////////////////////////////////////////////////////////
//                                                                            //
//  OpenSolid is a generic library for the representation and manipulation    //
//  of geometric objects such as points, curves, surfaces, and volumes.       //
//                                                                            //
//  Copyright 2007-2015 by Ian Mackenzie                                      //
//  ian.e.mackenzie@gmail.com                                                 //
//                                                                            //
//  This Source Code Form is subject to the terms of the Mozilla Public       //
//  License, v. 2.0. If a copy of the MPL was not distributed with this file, //
//  you can obtain one at http://mozilla.org/MPL/2.0/.                        //
//                                                                            //
////////////////////////////////////////////////////////////////////////////////

package org.opensolid.core

import scala.util.Random

final case class Bounds2d(x: Interval, y: Interval)
  extends Bounded[Bounds2d] with GeometricallyComparable[Bounds2d] {

  def this(components: (Interval, Interval)) = this(components.first, components.second)

  def components: (Interval, Interval) = (x, y)

  def component(index: Int): Interval = index match {
    case 0 => x
    case 1 => y
    case _ => throw new IndexOutOfBoundsException(s"Index $index is out of bounds for Bounds2d")
  }

  override def equals(that: Bounds2d, tolerance: Double): Boolean =
    this.minVertex.equals(that.minVertex, tolerance) &&
    this.maxVertex.equals(that.maxVertex, tolerance)

  override def bounds: Bounds2d = this

  def isEmpty: Boolean = x.isEmpty || y.isEmpty

  def isWhole: Boolean = x.isWhole && y.isWhole

  def isSingleton: Boolean = x.isSingleton && y.isSingleton

  def center: Point2d = Point2d(x.median, y.median)

  def minVertex: Point2d = Point2d(x.lowerBound, y.lowerBound)

  def maxVertex: Point2d = Point2d(x.upperBound, y.upperBound)

  def interpolated(u: Double, v: Double): Point2d = Point2d(x.interpolated(u), y.interpolated(v))

  def randomPoint: Point2d = randomPoint(Random)

  def randomPoint(generator: Random): Point2d =
    interpolated(generator.nextDouble, generator.nextDouble)

  def hull(point: Point2d): Bounds2d = Bounds2d(x.hull(point.x), y.hull(point.y))

  def hull(that: Bounds2d): Bounds2d = Bounds2d(this.x.hull(that.x), this.y.hull(that.y))

  def intersection(that: Bounds2d): Bounds2d = {
    val x = this.x.intersection(that.x)
    val y = this.y.intersection(that.y)
    if (x.isEmpty || y.isEmpty) Bounds2d.Empty else Bounds2d(x, y)
  }

  def overlaps(that: Bounds2d): Boolean = this.x.overlaps(that.x) && this.y.overlaps(that.y)

  def overlaps(that: Bounds2d, tolerance: Double): Boolean =
    this.x.overlaps(that.x, tolerance) && this.y.overlaps(that.y, tolerance)

  def contains(point: Point2d): Boolean = x.contains(point.x) && y.contains(point.y)

  def contains(point: Point2d, tolerance: Double): Boolean =
    x.contains(point.x, tolerance) && y.contains(point.y, tolerance)

  def contains(that: Bounds2d): Boolean = this.x.contains(that.x) && this.y.contains(that.y)

  def contains(that: Bounds2d, tolerance: Double): Boolean =
    this.x.contains(that.x, tolerance) && this.y.contains(that.y, tolerance)

  def bisected(index: Int): (Bounds2d, Bounds2d) = {
    if (index % 2 == 0) {
      val (xLower, xUpper) = x.bisected
      (Bounds2d(xLower, y), Bounds2d(xUpper, y))
    } else {
      val (yLower, yUpper) = y.bisected
      (Bounds2d(x, yLower), Bounds2d(x, yUpper))
    }
  }

  def +(vector: Vector2d): Bounds2d = Bounds2d(x + vector.x, y + vector.y)

  def +(vectorBounds: VectorBounds2d): Bounds2d = Bounds2d(x + vectorBounds.x, y + vectorBounds.y)

  def -(vector: Vector2d): Bounds2d = Bounds2d(x - vector.x, y - vector.y)

  def -(vectorBounds: VectorBounds2d): Bounds2d = Bounds2d(x - vectorBounds.x, y - vectorBounds.y)

  def -(point: Point2d): VectorBounds2d = VectorBounds2d(x - point.x, y - point.y)

  def -(that: Bounds2d): VectorBounds2d = VectorBounds2d(this.x - that.x, this.y - that.y)
}

object Bounds2d {
  def apply(components: (Interval, Interval)): Bounds2d = new Bounds2d(components)

  def singleton(point: Point2d): Bounds2d =
    Bounds2d(Interval.singleton(point.x), Interval.singleton(point.y))

  def hullOf(points: (Point2d, Point2d)): Bounds2d = points.first.hull(points.second)

  def hullOf(points: (Point2d, Point2d, Point2d)): Bounds2d =
    points.first.hull(points.second).hull(points.third)

  val Empty: Bounds2d = Bounds2d(Interval.Empty, Interval.Empty)

  val Whole: Bounds2d = Bounds2d(Interval.Whole, Interval.Whole)

  val Unit: Bounds2d = Bounds2d(Interval.Unit, Interval.Unit)

  implicit val Traits: Bounds[Bounds2d] = new Bounds[Bounds2d] {
    override def component(bounds: Bounds2d, index: Int): Interval = bounds.component(index)

    override def overlaps(
      firstBounds: Bounds2d,
      secondBounds: Bounds2d,
      tolerance: Double
    ): Boolean = firstBounds.overlaps(secondBounds, tolerance)

    override def contains(
      firstBounds: Bounds2d,
      secondBounds: Bounds2d,
      tolerance: Double
    ): Boolean = firstBounds.contains(secondBounds, tolerance)

    override def bisected(bounds: Bounds2d, index: Int): (Bounds2d, Bounds2d) =
      bounds.bisected(index)

    override def hull(firstBounds: Bounds2d, secondBounds: Bounds2d): Bounds2d =
      firstBounds.hull(secondBounds)

    override val NumDimensions: Int = 2

    override val Empty: Bounds2d = Bounds2d.Empty
  }
}