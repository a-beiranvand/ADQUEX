package iut.software.federationengine.optimizer;

import iut.software.federationegine.structures.OptimizerException;


import org.openrdf.query.algebra.Join;
import org.openrdf.query.algebra.Service;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;

public class FedJoinCounter extends QueryModelVisitorBase<OptimizerException>
{
	private int Counter ;
	public FedJoinCounter ()
	{
		super();
		Counter =0 ;
	}
	
	public int countFedJoins (TupleExpr QueryWithNewJoins)	
	{
		QueryWithNewJoins.visit(this) ;
		return Counter ;
	}

	@Override
	public void meet(Join node) throws OptimizerException {
		Counter++ ;
		super.meet(node);
	}
	//Do not go deep when you see a service node
	@Override
	public void meet(Service node ) throws OptimizerException{
		
	}
	
}
