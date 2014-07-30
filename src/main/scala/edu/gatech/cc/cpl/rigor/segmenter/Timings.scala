package edu.gatech.cc.cpl.rigor.segmenter

import scalaz.State

/**
 * Created by arya on 7/29/14.
 */
case class Timings(total_seg_time: Time,
                   total_init_time: Time,
                   extra_bndry_compute_time: Time,
                   superpixels_compute_time: Time,
                   precompute_pairwise_time: Time,
                   precompute_seed_time: Time,
                   im_pairwise_time: Time,
                   total_computing_segs_time: Time,
                   pairwise_set_time: Time,
                   unary_cost_set_time: Time,
                   pmc_time: List[Time],
                   pmc_parallel_cut_time: List[Time],
                   pmc_parallel_overhead_time: List[Time],
                   seg_filtering_time: List[Time],
                   init_filter_time: List[Time],
                   energy_filter_time: List[Time],
                   rand_filter_time: List[Time],
                   seg_similar_filter_time: List[Time])

object Timings {
  import scalaz.@>
  import scalaz.Lens.lensg
  val total_seg_time: Timings @> Time = lensg(a => b => a.copy(total_seg_time=b), _.total_seg_time)
  val total_init_time: Timings @> Time = lensg(a => b => a.copy(total_init_time=b), _.total_init_time)
  val extra_bndry_compute_time: Timings @> Time = lensg(a => b => a.copy(extra_bndry_compute_time=b), _.extra_bndry_compute_time)
  val superpixels_compute_time: Timings @> Time = lensg(a => b => a.copy(superpixels_compute_time=b), _.superpixels_compute_time)
  val precompute_pairwise_time: Timings @> Time = lensg(a => b => a.copy(precompute_pairwise_time=b), _.precompute_pairwise_time)
  val precompute_seed_time: Timings @> Time = lensg(a => b => a.copy(precompute_seed_time=b), _.precompute_seed_time)
  val im_pairwise_time: Timings @> Time = lensg(a => b => a.copy(im_pairwise_time=b), _.im_pairwise_time)
  val total_computing_segs_time: Timings @> Time = lensg(a => b => a.copy(total_computing_segs_time=b), _.total_computing_segs_time)
  val pairwise_set_time: Timings @> Time = lensg(a => b => a.copy(pairwise_set_time=b), _.pairwise_set_time)
  val unary_cost_set_time: Timings @> Time = lensg(a => b => a.copy(unary_cost_set_time=b), _.unary_cost_set_time)

  val pmc_time: Timings @> List[Time] = lensg(a => b => a.copy(pmc_time=b), _.pmc_time)
  val pmc_parallel_cut_time: Timings @> List[Time] = lensg(a => b => a.copy(pmc_parallel_cut_time=b), _.pmc_parallel_cut_time)
  val pmc_parallel_overhead_time: Timings @> List[Time] = lensg(a => b => a.copy(pmc_parallel_overhead_time=b), _.pmc_parallel_overhead_time)
  val seg_filtering_time: Timings @> List[Time] = lensg(a => b => a.copy(seg_filtering_time=b), _.seg_filtering_time)
  val init_filter_time: Timings @> List[Time] = lensg(a => b => a.copy(init_filter_time=b), _.init_filter_time)
  val energy_filter_time: Timings @> List[Time] = lensg(a => b => a.copy(energy_filter_time=b), _.energy_filter_time)
  val rand_filter_time: Timings @> List[Time] = lensg(a => b => a.copy(rand_filter_time=b), _.rand_filter_time)
  val seg_similar_filter_time: Timings @> List[Time] = lensg(a => b => a.copy(seg_similar_filter_time=b), _.seg_similar_filter_time)

//  def overwriteTime[A](t: Time, l: A @> Time): A => A = l.set(_, t)

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
