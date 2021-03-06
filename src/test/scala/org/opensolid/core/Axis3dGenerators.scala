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

import org.opensolid.core.Direction3dGenerators._
import org.opensolid.core.Point3dGenerators._
import org.scalacheck._

trait Axis3dGenerators {
  val anyAxis3d: Gen[Axis3d] =
    for {
      originPoint <- anyPoint3d
      direction <- anyDirection3d
    } yield Axis3d(originPoint, direction)

  implicit val arbitraryAxis3d: Arbitrary[Axis3d] = Arbitrary(anyAxis3d)
}

object Axis3dGenerators extends Axis3dGenerators
