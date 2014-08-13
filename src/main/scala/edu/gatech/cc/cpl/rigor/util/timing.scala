package edu.gatech.cc.cpl.rigor.util

import scalaz._

/**
 * Created by arya on 7/30/14.
 */
object timing {

  /** milliseconds */
  type Time = Long

  def measureTime[B](body: => B): (Time,B) = {
    val startTime = System.currentTimeMillis()
    val result = body
    val stopTime = System.currentTimeMillis()

    (stopTime - startTime, result)
  }

  def setTime[A,B](l: A @> Time)(body: => B): State[A,B] = {
    val (elapsedTime, result) = measureTime(body)
    l.assign(elapsedTime).map(_ => result)
  }

  def addTime[A,B](l: A @> List[Time])(body: => B): State[A,B] = {
    val (elapsedTime, result) = measureTime(body)
    l.mods(_ :+ elapsedTime).map(_ => result)
  }
}
