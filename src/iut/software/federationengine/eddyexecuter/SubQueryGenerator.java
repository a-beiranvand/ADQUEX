package iut.software.federationengine.eddyexecuter;


import iut.software.federationegine.structures.NewJoin;
import iut.software.federationegine.structures.OptimizerException;
import iut.software.federationegine.structures.SubQuery;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.openrdf.query.algebra.Service;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;

public class SubQueryGenerator extends QueryModelVisitorBase<OptimizerException> {
	
	private ArrayList<SubQuery> subQueries ;
	private char subQueryNameCounter ;
	public SubQueryGenerator() {
		// TODO Auto-generated constructor stub
		subQueryNameCounter='A';
	}
	
	
	public ArrayList<SubQuery> GenerateSubQueries (TupleExpr queryExpr) throws  OptimizerException
	{
		subQueries =new  ArrayList<SubQuery>();
		queryExpr.visit(this) ;
		return subQueries;
	}
	
	@Override
	public void meet(Service node) throws OptimizerException {
		
		Set<String> serviceVars = node.getServiceVars() ;
		ArrayList<String> serviceJoinVars=GenerateJoinVars(serviceVars); 
		SubQuery subService =new SubQuery() ;
		subService.setQueryString(node.getQueryString(serviceVars)) ;
		subService.setJoinVars(serviceJoinVars) ;
		Eddy eddyInstance =Eddy.getInstance();
		subService.setReadyBitPattern(GenerateReadyBitPattern(eddyInstance.getJoinVarTojoinMapper(), serviceJoinVars, node));
		try 
		{
			subService.setEndpointAdress(node.getServiceRef().getValue().stringValue());
		}
		catch (URISyntaxException e) 
		{ 
			throw new OptimizerException(e) ;
		}
		char[] sqname =new char[1] ;
		sqname[0] =subQueryNameCounter;
		subService.setSubQueryName(new String(sqname)) ;
		subQueries.add(subService);
		subQueryNameCounter++;
		super.meet(node);
	}
	
	
	//Generate service join vars based on join table
	private ArrayList<String> GenerateJoinVars (Set<String> serviceVars) throws OptimizerException
	{
		Eddy eddyInstance =Eddy.getInstance();
		ArrayList<String> joinVars= new  ArrayList<String>();
		for (String var:serviceVars)
		{
			if (eddyInstance.getJoinVarTojoinMapper().get(var) !=null)
			{
				joinVars.add(var) ;
			}
		}
		return joinVars;
	}

	
	private int GenerateReadyBitPattern (HashMap<String, ArrayList<Byte>> joinTable,ArrayList<String> serviceJoinVars,Service node)
	{
		int readyBitPattern =0 ;
		boolean isLeftChild =false ;
		NewJoin parentJoin =(NewJoin)node.getParentNode() ;
		if (node == parentJoin.getLeftArg())
		{
			isLeftChild=true ;
		}
		String exclusiveJoinVar =null ;
		if ((joinTable.get(parentJoin.getJoinVariable()).size()>1) && (isLeftChild))
		{
			exclusiveJoinVar=parentJoin.getJoinVariable() ;
		}
		Set <String> allOfJoinVars =joinTable.keySet() ;
		ArrayList<Byte> joinIdsForAjoinVar  ;
		
		for (String joinVarItem:allOfJoinVars)
		{
			joinIdsForAjoinVar =joinTable.get(joinVarItem) ;
			for (Byte joinIdItem :joinIdsForAjoinVar)
			{
				if ((exclusiveJoinVar!=null)&&(joinVarItem.equals(exclusiveJoinVar)))
				{
					if (joinIdItem == parentJoin.getJoinId())
					{
						int bitSetter = 1 << joinIdItem;
						readyBitPattern = readyBitPattern | bitSetter;
						//joinBitPattern.put(joinIdItem, JoinStatus.Ready);
					}
//					else
//					{
//						joinBitPattern.put(joinIdItem, JoinStatus.NotReady);
//					}
				}
				else
				{
					if (serviceJoinVars.contains(joinVarItem))
					{
						int bitSetter = 1 << joinIdItem;
						readyBitPattern = readyBitPattern | bitSetter;
//						joinBitPattern.put(joinIdItem, JoinStatus.Ready) ;
					}
//					else
//					{
//						joinBitPattern.put(joinIdItem, JoinStatus.NotReady);
//					}
				}
			}
		}
		return readyBitPattern;
	}
}
