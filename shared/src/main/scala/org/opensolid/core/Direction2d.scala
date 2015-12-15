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

import scala.annotation.tailrec
import scala.beans.BeanProperty
import scala.math
import scala.util.Random

final case class Direction2d(vector: Vector2d) extends VectorTransformable2d[Direction2d] {
  def x: Double = vector.x

  def y: Double = vector.y

  def component(index: Int): Double = vector.component(index)

  def unary_- : Direction2d = Direction2d(-vector)

  def *(sign: Sign): Direction2d = sign match {
    case Sign.Positive => this
    case Sign.Negative => -this
    case _ => Direction2d.None
  }

  def *(value: Double): Vector2d = vector * value

  def *(interval: Interval): VectorBoundingBox2d = vector * interval

  def /(value: Double): Vector2d = vector / value

  def /(interval: Interval): VectorBoundingBox2d = vector / interval

  def transformedBy(transformation: Transformation2d): Direction2d = transformation(this)

  def projectedOnto(axis: Axis2d): Vector2d = vector.projectedOnto(axis)

  def placedOnto(plane: Plane3d): Direction3d = Direction3d(vector.placedOnto(plane))

  def dot(vector: Vector2d): Double = this.vector.dot(vector)

  def dot(that: Direction2d): Double = this.vector.dot(that.vector)

  def dot(vectorBoundingBox: VectorBoundingBox2d): Interval = vector.dot(vectorBoundingBox)

  def dot(directionBoundingBox: DirectionBoundingBox2d): Interval =
    vector.dot(directionBoundingBox.vectorBoundingBox)

  def normalDirection: Direction2d = Direction2d(-y, x)

  def angleTo(that: Direction2d): Double =
    math.atan2(this.x * that.y - this.y * that.x, this.x * that.x + this.y * that.y)
}

object Direction2d {
  def apply(x: Double, y: Double): Direction2d = Direction2d(Vector2d(x, y))

  def fromAngle(angle: Double): Direction2d = Direction2d(math.cos(angle), math.sin(angle))

  def random: Direction2d = random(Random)

  def random(generator: Random): Direction2d = {
    @tailrec
    def generate: Direction2d = {
      val x = -1.0 + 2.0 * generator.nextDouble
      val y = -1.0 + 2.0 * generator.nextDouble
      val squaredNorm = x * x + y * y
      if (squaredNorm >= 0.25 && squaredNorm <= 1.0) {
        val norm = math.sqrt(squaredNorm)
        Direction2d(x / norm, y / norm)
      } else {
        generate
      }
    }
    generate
  }

  @BeanProperty
  val None: Direction2d = Direction2d(0.0, 0.0)

  @BeanProperty
  val X: Direction2d = Direction2d(1.0, 0.0)

  @BeanProperty
  val Y: Direction2d = Direction2d(0.0, 1.0)
}
