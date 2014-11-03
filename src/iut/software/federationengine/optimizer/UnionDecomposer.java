package iut.software.federationengine.optimizer;


import java.util.ArrayList;
import java.util.List; 
import iut.software.federationegine.structures.OptimizerException;

import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.Union;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;

public class UnionDecomposer extends QueryModelVisitorBase<OptimizerException> 
{

	public UnionDecomposer()
	{
		super() ;
		_UnionSubQueries= new ArrayList<TupleExpr>() ;
	}
	
	private ArrayList <TupleExpr> _UnionSubQueries ;
	
	public List<TupleExpr> getListOfUnionSubQueries (TupleExpr query)
	{
		query.visit(this) ;
		return _UnionSubQueries ;
	}
	
	@Override
	public void meet(Union node) throws OptimizerException
	{
		_UnionSubQueries.add(node.getLeftArg()) ;
		_UnionSubQueries.add(node.getRightArg()) ;
	}
	
}
