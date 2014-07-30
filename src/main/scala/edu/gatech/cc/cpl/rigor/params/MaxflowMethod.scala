package edu.gatech.cc.cpl.rigor.params

/**
 * Created by arya on 7/1/14.
 */
sealed trait MaxflowMethod

package object maxflowMethod {
  /** pseudo-flow */
  case object hochbaum extends MaxflowMethod
  /** vanilla Boykov-Kolmogorov */
  case object nodynamic extends MaxflowMethod
  /** Kohli-Torr dynamic graph method */
  case object kohli extends MaxflowMethod
  /** our method */
  case object multiseed extends MaxflowMethod

  /*
  For additional options with 'nodynamic', 'kohli', and 'multiseed',
  see comments in GraphProcessor.multiseed_param_min_st_cut().
  */
}

