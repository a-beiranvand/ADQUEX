package iut.software.federationengine.optimizer;

import iut.software.federationegine.structures.NewJoin;
import iut.software.federationegine.structures.OptimizerException;

import java.util.Set;

import org.openrdf.query.algebra.Join;
import org.openrdf.query.algebra.Service;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;

public class JoinReplacer extends QueryModelVisitorBase<OptimizerException>{
	
	private byte joinIdCounter ;
	private char leftSubQueryName ;
	public JoinReplacer() {
		super() ;
		joinIdCounter =0 ;
		leftSubQueryName='A' ;
	}
	
	public TupleExpr ReplaceJoinsWithNewJoins (TupleExpr queryTree) throws OptimizerException 
	{
		queryTree.visit(this) ;
		return queryTree;
	}
	
	@Override
	public void meet(Join node) throws OptimizerException 
	{
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("Left: "+node.getLeftArg().getBindingNames());
		System.out.println("Right: "+node.getRightArg().getBindingNames());
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		String joinVar =getJoinVar(node) ;
		char[] leftSubQueryChars =new char[1] ;
		leftSubQueryChars[0]=leftSubQueryName ;
		NewJoin joinToReplace =new NewJoin(node.getLeftArg(), node.getRightArg(), joinVar,joinIdCounter,new String(leftSubQueryChars) ) ;
		leftSubQueryName++;
		joinIdCounter++ ;
		node.replaceWith(joinToReplace) ;
		super.meet(node);
	}
	
	@Override
	public void meet(Service node) throws OptimizerException {
		// Do not go to deep when you meet a SERVICE because we don't want to REPLACE joins that are in SERVICE 
	
	}
	private String getJoinVar (Join node) throws OptimizerException
	{
		Set<String> leftBindings = node.getLeftArg().getBindingNames()  ;
		Set <String> rightBindings =node.getRightArg().getBindingNames() ;
		String joinVar=null ;
		//Finding shared variable between right and left args of a join
		for (String leftItem :leftBindings)
		{
			for (String rightItem :rightBindings)
			{
				if (leftItem.equals(rightItem))
				{
					if (joinVar!=null)
					{
						//if there is more than 1 shared variable throw an exception
						
						OptimizerException e =new OptimizerException("Join Replacer Erorr! More Than 1 Join Variable Currently Is Not Supported. ") ;
						throw e ;
					}
					joinVar =leftItem;
				}
				
			}
			
		}
		
		//if there isn't any shared variable throw an exception
		if (joinVar ==null) 
		{
			OptimizerException e =new OptimizerException("Join Replacer Error! There is'nt any share variable between, \n Left:\n "+node.getLeftArg().toString()+"\n and right:\n"+node.getRightArg().toString() );
			throw e ;
		}
		//Every thing is ok return join variable
		
		return joinVar;
	}
	
	


}
