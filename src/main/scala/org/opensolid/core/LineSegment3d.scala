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

final case class LineSegment3d(startPoint: Point3d, endPoint: Point3d)
  extends Scalable3d[LineSegment3d]
  with Bounded[Bounds3d]
  with GeometricallyComparable[LineSegment3d] {

  def endpoints: (Point3d, Point3d) =
    (startPoint, endPoint)

  def vector: Vector3d =
    startPoint.vectorTo(endPoint)

  def direction: Option[Direction3d] =
    vector.direction

  def normalDirection: Option[Direction3d] =
    direction.map(_.normalDirection)

  def axis: Option[Axis3d] =
    direction.map(Axis3d(startPoint, _))

  def length: Double =
    vector.length

  def squaredLength: Double =
    vector.squaredLength

  def midpoint: Point3d =
    Point3d.midpoint(startPoint, endPoint)

  override def bounds: Bounds3d =
    startPoint.hull(endPoint)

  override def isEqualTo(that: LineSegment3d, tolerance: Double): Boolean =
    this.startPoint.isEqualTo(that.startPoint, tolerance) &&
    this.endPoint.isEqualTo(that.endPoint, tolerance)

  override def scaledAbout(point: Point3d, scale: Double): LineSegment3d =
    LineSegment3d(
      startPoint.scaledAbout(point, scale),
      endPoint.scaledAbout(point, scale)
    )

  override def transformedBy(transformation: Transformation3d): LineSegment3d =
    LineSegment3d(
      startPoint.transformedBy(transformation),
      endPoint.transformedBy(transformation)
    )

  def projectedOnto(axis: Axis3d): LineSegment3d =
    LineSegment3d(startPoint.projectedOnto(axis), endPoint.projectedOnto(axis))

  def projectedOnto(plane: Plane3d): LineSegment3d =
    LineSegment3d(startPoint.projectedOnto(plane), endPoint.projectedOnto(plane))

  def projectedInto(plane: Plane3d): LineSegment2d =
    LineSegment2d(startPoint.projectedInto(plane), endPoint.projectedInto(plane))
}

object LineSegment3d {
  def fromEndpoints(endpoints: (Point3d, Point3d)): LineSegment3d = endpoints match {
    case (startPoint, endPoint) => LineSegment3d(startPoint, endPoint)
  }
}
