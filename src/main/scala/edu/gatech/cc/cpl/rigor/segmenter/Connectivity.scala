package edu.gatech.cc.cpl.rigor.segmenter

/**
 * Created by arya on 7/30/14.
 */

sealed trait Connectivity
object Connectivity {
  case object EightWay extends Connectivity
  case object FourWay extends Connectivity
}
