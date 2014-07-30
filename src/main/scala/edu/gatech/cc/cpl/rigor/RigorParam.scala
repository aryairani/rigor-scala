package edu.gatech.cc.cpl.rigor

import java.io.File

import edu.gatech.cc.cpl.rigor.boundaryfuncs.{StructEdges, Gb, BoundaryFunc}
import edu.gatech.cc.cpl.rigor.graphmethod._
import edu.gatech.cc.cpl.rigor.params.MaxflowMethod
import edu.gatech.cc.cpl.rigor.params.maxflowMethod.{hochbaum, multiseed}
import edu.gatech.cc.cpl.rigor.superpixelmethods.{sp_seeds_caller, felzenszwalb_seeds_caller}

import scalaz.@>

/**
 * Created by arya on 6/30/14.
 */
abstract case class RigorParam[+A](default: A) {
  def apply: A
}




/**
 * Parameters for FastSeg's technical internals
 * @param pmc_num_lambdas Number of lambda parameters to enumerate for parametric min-cut
 *                        (see GraphProcessor.generate_param_lambdas() on how lambda as generated).
 * @param pmc_maxflow_method Type of max-flow method to use. Invoked at GraphProcessor.generate_mincut_segments().
 *                           Current options are 'hochbaum' (Pseudo-flow), 'nodynamic' (vanilla Boykov-Kolmogorov),
 *                           'kohli' (Kohli-Torr dynamic graph method), and finally 'multiseed' (our method).
 * @param boundaries_method Type of boundary detector to use. Invoked at Segmenter.compute_boundaries().
 * @param filter_min_seg_pixels Used in Segmenter.filter_segments to decide the minimum size a segment should have for
 *                              it to be retained.
 * @param filter_max_rand If more segments are produced than this no., they are randomly selected and reduced to this
 *                        number. Invoked by Segmenter.filter_segments().
 * @param graph_methods A list of unary graph types to use for min-cut segmentation, and their parameters.
 *                      These are iterated over in Segmenter.compute_segments.
 *
 */
case class SegmParams(
                       pmc_num_lambdas: Int,
                       pmc_maxflow_method: MaxflowMethod,
                       boundaries_method: BoundaryFunc,
                       filter_min_seg_pixels: Int,
                       filter_max_rand: Int,
                       graph_methods: Seq[GraphMethod_]//,
//                       graph_sub_methods_seeds_idx: ???,
//                       graph_seed_gen_method: Seq[GraphSeedGenMethod],
                       )

/**
 * 
 * @param method A unary graph types to use for min-cut segmentation. These are iterated over in Segmenter.compute_segments.
 * @param submethods
 * @param seeds
 */
case class GraphMethodConfig(method: GraphMethod_, submethods: ???, seeds: (Int,Int))

object SegmParams {


  val default = {
    val seedgen1 = sp_img_grid((5,5),(40,40))
    val seedgen2 = sp_clr_seeds((5,5),(15,15), felzenszwalb_seeds_caller())

    SegmParams(
      pmc_num_lambdas = 20,
      pmc_maxflow_method = hochbaum,
      boundaries_method = Gb,
      filter_min_seg_pixels = 100,
      filter_max_rand = 5000,
      graph_methods = Seq(
        new UniformGraph(
          submethods = Seq(UniformGraph.Internal(seedgen1), UniformGraph.External(seedgen1)),
          graphSeedFrameWeight = 1000,
          pairwise = PairwiseWeights(1, 1, 1e-3)
        ),
        new ColorGraph(
          submethods = Seq(ColorGraph.Internal(seedgen2), ColorGraph.External(seedgen2)),
          graphSeedFrameWeight = 1000,
          graph_unary_exp_scale = 0.07,
          pairwise = PairwiseWeights(1.5, 1, 4e-3)
        )
      )
    )
  }

  val structEdges = {
    val seedgen1 = sp_img_grid(
      graph_seed_nums = (8,8),
      graph_seed_region_size = (50,50)
    )
    val seedgen2 = sp_clr_seeds(
      graph_seed_nums = (8,8),
      graph_seed_region_size = (50,50),
      sp_seeds_caller
    )
    List(
      pmc_maxflow_method := multiseed,
      boundaries_method := StructEdges,
      graph_methods := Seq(
        new UniformGraphFuxin(
          submethods = Seq(
            UniformGraphFuxin.Internal(seedgen1),
            UniformGraphFuxin.External(seedgen1),
            UniformGraphFuxin.External2(seedgen1)
          ),
          graphSeedFrameWeight = 1000,
          pairwise = PairwiseWeights(3.5, 1, 1e-3)
        ),
        new ColorGraphFuxin(
          submethods = Seq(
            ColorGraphFuxin.Internal(seedgen1),
            ColorGraphFuxin.External(seedgen1),
            ColorGraphFuxin.External2(seedgen1)
          ),
          graphSeedFrameWeight = 1100,
          graph_unary_exp_scale = 0.5,
          pairwise = PairwiseWeights(3.5, 1, 1e-3)
        )
      )
    )

  }

  import scalaz.Lens.lensg
  val pmc_maxflow_method: SegmParams @> MaxflowMethod = lensg(a => b => a.copy(pmc_maxflow_method=b), _.pmc_maxflow_method)
  val boundaries_method: SegmParams @> BoundaryFunc = lensg(a => b => a.copy(boundaries_method=b), _.boundaries_method)
  val graph_methods: SegmParams @> Seq[GraphMethod_] = lensg(a => b => a.copy(graph_methods=b), _.graph_methods)

}


/*
    % set the Segmenter parameters
    sp = get_param_val(sp, ep, 'graph_methods', ...
            {     'UniformGraph'     ,      'ColorGraph'       });          % A list of unary graph types to use for min-cut segmentation. These are iterated over in Segmenter.compute_segments. They are names of classes of AbstractGraph type.
    sp = get_param_val(sp, ep, 'graph_sub_methods', ...
            {{'internal', 'external'}, {'internal', 'external'}});          % Each unary graph type can have slightly different sub-methods for setting unaries. Each cell lists the types of methods for each graph
                                                                            % given in graph_methods. They are iterated over and computed from AbstractGraph.prepare_graphs(). These methods are usually listed in a
                                                                            % switch case statement in the create_unaries() method, for instance, you can see them in ColorGraph.create_unaries().
    sp = get_param_val(sp, ep, 'graph_seed_frame_weight', ...
            {         1000           ,          1000           });          % Generally used as a scaling value for graph unaries. Each graph method has its own value. For instance, it is used by ColorGraph as a
                                                                            % scaling constant when computing color distance metric for each superpixel (see compute_sp_*unary_values). Also used in UniformGraph*. For
                                                                            % instance a higher value in ColorGraph is likely to give smaller no. of segments with a more smaller change between the parameteric
                                                                            % min-cuts, and the largest cut being more likely smaller in size with the same parametric lambda.
    sp = get_param_val(sp, ep, 'graph_unary_exp_scale', ...
            {          NaN           ,          0.07           });          % Only used by ColorGraph* in compute_sp_*unary_values to decide an expontial scaling for the color distance metric. This controls how
                                                                            % similar a color should be to the seed to get a high unary value (hence more chances of lying in the fg). The quality of results from
                                                                            % changing this value highly depends on the image itself. Each graph method has its own value.
    sp = get_param_val(sp, ep, 'graph_pairwise_contr_weight', ...
            {          1             ,            1            });          % Is the pairwise distance multiplier. It helps converting boundary value distances into a capacity value that can be used for min-cut. This
                                                                            % value is used in AbstractGraph.get_pairwise_capacities() - see comments in the file for more explanation. The effect of increasing this
                                                                            % value is similar to <graph_pairwise_multiplier>: you are more likely to get larger segments for the same parametric lambda - hence, using
                                                                            % the same range of lambda values will also likely decrease the total number of segments when using a larger <graph_pairwise_contr_weight>.
    sp = get_param_val(sp, ep, 'graph_pairwise_potts_weight', ...
            {         1e-3           ,           4e-3          });          % This is used in conjunction with <graph_pairwise_contr_weight> to set the pairwise capacities in the graph. It acts as an offset. See
                                                                            % AbstractGraph.get_pairwise_capacities() to find how it is exactly used. The trend in the size of segments produced when increasing its
                                                                            % value, is similar to when you increase <graph_pairwise_contr_weight>, although the change is more gradual with the same amount of increase.
    sp = get_param_val(sp, ep, 'graph_pairwise_sigma', ...
            {          1           ,             1.5           });          % This is used in conjunction with <graph_pairwise_contr_weight> and <graph_pairwise_potts_weight> in setting the pairwise capacities in the
                                                                            % graph. Its used in AbstractGraph.get_pairwise_capacities(). Increasing this value would most likely increase the number of segments
                                                                            % produced. As with other settings, each cell gives a value used for each graph method.
    sp = get_param_val(sp, ep, 'graph_sub_methods_seeds_idx', ...
            {[     1    ,     1     ], [    2     ,     2     ]});          % Each cell in <graph_seed_gen_method> decides the seed generation method to use. This parameter gives the index to the seed generation
                                                                            % method in <graph_seed_gen_method> to use for a particular graph method (the size of this parameter needs to be exactly the same as
                                                                            % <graph_sub_methods>). Invoked at Segmenter.precompute_unary_data() in the precompute_seeds() function.
    sp = get_param_val(sp, ep, 'graph_seed_gen_method', ...
            {'sp_img_grid',             'sp_clr_seeds'             });      % Possible seed genereation methods used for different graph methods. The seed generation method used for a particular graph is decided by
                                                                            % <graph_sub_methods_seeds_idx>. Invoked at Segmenter.precompute_unary_data() in the precompute_seeds() function - which generates seeds
                                                                            % using AbstractGraph.generate_graph_seeds(). Note that if some seed method given here is not referred to in <graph_sub_methods_seeds_idx>,
                                                                            % it will never be called to generate any seeds.
    sp = get_param_val(sp, ep, 'graph_seed_nums', ...
            {    [5, 5]   ,                 [5, 5]                 });      % Indicates for each seed generation method that how many seeds should be generated. Each array indicates what's the density of seed grid on
                                                                            % the image (a [5 5] would produce a total of 25 seeds). The first number specifies the # of seeds in the vertical direction and the second in
                                                                            % the horizontal direction.
    sp = get_param_val(sp, ep, 'graph_seed_params', ...
            {  {[40, 40]} , {[15, 15], 'felzenszwalb_seeds_caller'}});      % The parameter settings for each seed generation method in <graph_seed_gen_method>. Each cell is passed as the <graph_seed_params> argument
                                                                            % in AbstractGraph.generate_graph_seeds(). For both 'sp_img_grid' and 'sp_clr_seeds', the first array is the height and width of a seed region
                                                                            % in pixels (which is then converted to a set of superpixel by generate_seeds_funcs::pixel_set_to_sp_set()). Just see
                                                                            % AbstractGraph.generate_graph_seeds() on how different parameters are used.
 */
case class FilepathParams(
                           // Directory where data is saved (like computed boundaries, debug info, etc.)
                           data_save_dirpath: File
                       )
case class OtherParams(

                       )
case class ExtraParams(

                       )

/*
    fp = filepath_params;% Parameters related to directory locations
    sp = segm_params;    % Parameters for FastSeg's technical internals
    op = other_params;   % Misc. parameters
    ep = extra_params;
 */

object Params {

  def defaultFilePath(code_root_dir: File) = FilepathParams(
    data_save_dirpath = new File(code_root_dir, "data")
  )

}





