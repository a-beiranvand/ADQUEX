package iut.software.federationengine.eddyexecuter;

import iut.software.federationegine.structures.NewJoin;
import iut.software.federationegine.structures.OptimizerException;

import java.util.ArrayList;
import java.util.HashMap;

import org.openrdf.query.algebra.Join;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;

public class JoinTableGenerator  extends QueryModelVisitorBase<OptimizerException> {
	
	
	private HashMap<String, ArrayList<Byte>> joinTable ;
	
	public JoinTableGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	
	public HashMap<String, ArrayList<Byte>> GenerateJoinTable (TupleExpr queryExpr) throws OptimizerException
	{
		
		this.joinTable =new HashMap<String, ArrayList<Byte>>();
		queryExpr.visit(this) ;
		return joinTable;
		
	}
	
	
	
	
	@Override
	public void meet(Join node) throws OptimizerException
	{
		
		if (node instanceof NewJoin)
		{
			NewJoin fedJoin =(NewJoin)node;
			ArrayList<Byte> joinIds = null ;
			//Assumption : there is only one join variable 
			joinIds =joinTable.get(fedJoin.getJoinVariable()) ;
			//System.out.println("jointable generator: join Ids: "+joinIds );
			if (joinIds==null)
			{
				joinIds =new ArrayList<Byte>() ;
				joinIds.add(fedJoin.getJoinId()) ;
				joinTable.put(fedJoin.getJoinVariable(), joinIds) ;
			}
			else
			{
				joinIds.add(fedJoin.getJoinId()) ;
				joinTable.put(fedJoin.getJoinVariable(), joinIds) ;
			}
		}
		super.meet(node);
	}

}
