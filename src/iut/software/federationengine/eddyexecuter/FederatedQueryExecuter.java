package iut.software.federationengine.eddyexecuter;

import iut.software.federationegine.structures.OptimizerException;
import iut.software.federationegine.structures.QueryResultList;
import iut.software.federationegine.structures.SubQuery;
import iut.software.federationegine.structures.Tuple;
import iut.software.federationengine.optimizer.FedJoinCounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.openrdf.query.algebra.TupleExpr;

public class FederatedQueryExecuter {
	
	
	public FederatedQueryExecuter() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static QueryResultList ExecuteTupleExpr(TupleExpr tupleExprToExecute) throws OptimizerException
	{
		//you should change the return type // Solved in 8-8-92
		int countFedJoins =(new FedJoinCounter().countFedJoins(tupleExprToExecute)) ;
		if (countFedJoins==0)
		{
			return SingleEndpointExecute(tupleExprToExecute);
		}
		else
		{
			LinkedBlockingQueue<Tuple> queryResults =new LinkedBlockingQueue<Tuple>() ;
			Eddy.Initialize(queryResults) ;
			Eddy eddyinstance =Eddy.getInstance() ;
			JoinTableGenerator jtGenerator = new JoinTableGenerator();
			HashMap<String,ArrayList<Byte>> joinTable = jtGenerator.GenerateJoinTable(tupleExprToExecute) ;
			eddyinstance.setJoinVarTojoinMapper(joinTable) ;
			ArrayList<SubQuery> subQs =new SubQueryGenerator().GenerateSubQueries(tupleExprToExecute) ;
			eddyinstance.setSubQueries(subQs);
			eddyinstance.setQueryTupleExpr(tupleExprToExecute);
			System.out.println("---------------------------------------");
			int i= 0 ;
			for (SubQuery sq :subQs)
			{
				System.out.println("///////////////////////////////////////////////////////\n");
				System.out.println("Sub Query Name: "+sq.getSubQueryName());
				System.out.println("sub query string "+i+":"+sq.getQueryString());
				System.out.println("sub query "+i+" join vars:"+sq.getJoinVars());
				System.out.println("sub query "+i+" endpoint :"+sq.getEndpointAdress());
				System.out.println("\nsub query join bit pattern: "+sq.getReadyBitPattern());
				System.out.println("\n///////////////////////////////////////////////////////\n");
				i++;
			}
			//Eddy is a runnable object create a new thread for eddy.
			Thread eddyThread =new Thread(eddyinstance) ;
			eddyThread.start() ;
			
			//Creating a new queryResultList and returning it. 
			QueryResultList finalResults =new QueryResultList(queryResults);
			return finalResults;
		}
	}
	
	private static QueryResultList SingleEndpointExecute (TupleExpr tupleExprToExecute)
	{
		LinkedBlockingQueue<Tuple> queryResults =new LinkedBlockingQueue<Tuple>() ;
		SingleEndpointQueryExecuter seqe = new SingleEndpointQueryExecuter(tupleExprToExecute, queryResults) ;
		//seqe implements runnable
		Thread seqeThread =new Thread (seqe) ;
		seqeThread.start() ;
		QueryResultList finalResults =new QueryResultList(queryResults);
		return finalResults;
	}
	

}
