import iut.software.federationegine.structures.NewJoin;
import iut.software.federationegine.structures.OptimizerException;

import org.openrdf.query.algebra.Join;
import org.openrdf.query.algebra.Service;
import org.openrdf.query.algebra.StatementPattern;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;


public class Joinprinter extends QueryModelVisitorBase<OptimizerException>{
	
	
	public Joinprinter() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void meet(Join node) throws OptimizerException {
		
	//	System.out.println("Left: "+node.getLeftArg());
	//	System.out.println("Right: "+node.getRightArg());
//		NewJoin nNode =(NewJoin)node ;
//		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//		System.out.println("Join Id = "+ nNode.getJoinId() + " Join Var= "+ nNode.getJoinVariable());
		super.meet(node);
	}
	@Override
	public void meet(Service node) throws OptimizerException {
		System.out.println("SERVICE: "+node.getServiceVars());
		System.out.println("Sub Query String: "+ node.getQueryString(node.getServiceVars()));
		Join parentJoin =(Join)node.getParentNode() ;
		if (parentJoin.getRightArg() == node )
		{
			System.out.println("Right Child");
		}
		else
		{
			System.out.println("Left Child");
		}
		super.meet(node);
	}
	
	@Override
	public void meet(StatementPattern node) throws OptimizerException {
		
		
		
		super.meet(node);
	}
	
	
}
