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

final case class Triangle2d(firstVertex: Point2d, secondVertex: Point2d, thirdVertex: Point2d)
  extends Scalable2d[Triangle2d]
  with Bounded[Bounds2d]
  with GeometricallyComparable[Triangle2d] {

  def vertices: (Point2d, Point2d, Point2d) =
    (firstVertex, secondVertex, thirdVertex)

  def vertex(index: Int): Point2d = index match {
    case 0 => firstVertex
    case 1 => secondVertex
    case 2 => thirdVertex
    case _ =>
      throw new IndexOutOfBoundsException(s"Index $index is out of bounds for a triangle vertex")
  }

  def edges: (LineSegment2d, LineSegment2d, LineSegment2d) = {
    val oppositeFirst = LineSegment2d(thirdVertex, secondVertex)
    val oppositeSecond = LineSegment2d(firstVertex, thirdVertex)
    val oppositeThird = LineSegment2d(secondVertex, firstVertex)
    (oppositeFirst, oppositeSecond, oppositeThird)
  }

  def edge(index: Int): LineSegment2d = index match {
    case 0 => LineSegment2d(thirdVertex, secondVertex)
    case 1 => LineSegment2d(firstVertex, thirdVertex)
    case 2 => LineSegment2d(secondVertex, firstVertex)
    case _ =>
      throw new IndexOutOfBoundsException(s"Index $index is out of bounds for a triangle edge")
  }

  override def transformedBy(transformation: Transformation2d): Triangle2d =
    Triangle2d(
      firstVertex.transformedBy(transformation),
      secondVertex.transformedBy(transformation),
      thirdVertex.transformedBy(transformation)
    )

  override def scaledAbout(point: Point2d, scale: Double): Triangle2d =
    Triangle2d(
      firstVertex.scaledAbout(point, scale),
      secondVertex.scaledAbout(point, scale),
      thirdVertex.scaledAbout(point, scale)
    )

  override def bounds: Bounds2d =
    firstVertex.hull(secondVertex).hull(thirdVertex)

  override def isEqualTo(that: Triangle2d, tolerance: Double): Boolean =
    this.firstVertex.isEqualTo(that.firstVertex, tolerance) &&
    this.secondVertex.isEqualTo(that.secondVertex, tolerance) &&
    this.thirdVertex.isEqualTo(that.thirdVertex, tolerance)

  def area: Double =
    0.5 * firstVertex.vectorTo(secondVertex).cross(firstVertex.vectorTo(thirdVertex))

  def centroid: Point2d =
    firstVertex + (firstVertex.vectorTo(secondVertex) + firstVertex.vectorTo(thirdVertex)) / 3.0

  def contains(point: Point2d): Boolean = {
    val firstProduct = firstVertex.vectorTo(secondVertex).cross(firstVertex.vectorTo(point))
    val secondProduct = secondVertex.vectorTo(thirdVertex).cross(secondVertex.vectorTo(point))
    val thirdProduct = thirdVertex.vectorTo(firstVertex).cross(thirdVertex.vectorTo(point))

    (firstProduct >= 0 && secondProduct >= 0 && thirdProduct >= 0) ||
    (firstProduct <= 0 && secondProduct <= 0 && thirdProduct <= 0)
  }

  def placedOnto(plane: Plane3d): Triangle3d =
    Triangle3d(
      firstVertex.placedOnto(plane),
      secondVertex.placedOnto(plane),
      thirdVertex.placedOnto(plane)
    )
}

object Triangle2d {
  val Unit: Triangle2d = Triangle2d(Point2d.Origin, Point2d(1, 0), Point2d(0, 1))
}
