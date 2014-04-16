package iut.software.federationengine.optimizer;

import iut.software.federationegine.structures.OptimizerException;

import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.evaluation.impl.QueryJoinOptimizer;
import org.openrdf.query.algebra.evaluation.impl.QueryModelNormalizer;

public class Optimizer {
	
	
	public static TupleExpr Optimize (TupleExpr queryTree,boolean optimizejoins) throws OptimizerException
	{
		if (optimizejoins)
		{
			QueryJoinOptimizer joinOptimizer = new  QueryJoinOptimizer();
			joinOptimizer.optimize(queryTree, null, null) ;
		}
		else
		{
			LeftRightJoinReplacer lrReplacer =new LeftRightJoinReplacer() ;
			lrReplacer.ReplaceJoinsWithNewJoins(queryTree);
		}
		JoinReplacer joinRep =new JoinReplacer() ;
		joinRep.ReplaceJoinsWithNewJoins(queryTree) ;
		return queryTree;
	}
	
	

}
