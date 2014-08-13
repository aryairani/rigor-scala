package edu.gatech.cc.cpl.rigor.maxflow

import edu.gatech.cc.cpl.rigor.???

/**
 * Created by arya on 7/1/14.
 */
sealed abstract trait MaxflowMethod {
  def apply(): ???
}

/** pseudo-flow */
case object hochbaum extends MaxflowMethod { def apply() = ??? }

/** vanilla Boykov-Kolmogorov */
case object nodynamic extends MaxflowMethod { def apply() = ??? }

/** Kohli-Torr dynamic graph method */
case object kohli extends MaxflowMethod { def apply() = ??? }

/** our method */
case object multiseed extends MaxflowMethod { def apply() = ??? }

/*
For additional options with 'nodynamic', 'kohli', and 'multiseed',
see comments in GraphProcessor.multiseed_param_min_st_cut().
*/