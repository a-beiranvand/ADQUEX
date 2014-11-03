package iut.software.federationengine.eddyexecuter;

import iut.software.federationegine.structures.OptimizerException;
import iut.software.federationegine.structures.QueryResultList;
import iut.software.federationegine.structures.SubQuery;
import iut.software.federationegine.structures.Tuple;
import iut.software.federationengine.logging.myLogger;
import iut.software.federationengine.optimizer.FedJoinCounter;
import iut.software.federationengine.optimizer.UnionDecomposer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.openrdf.query.algebra.TupleExpr;

public class FederatedQueryExecuter {
	
	
	public FederatedQueryExecuter() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static QueryResultList ExecuteTupleExpr(TupleExpr tupleExprToExecute) throws OptimizerException
	{
		//you should change the return type // Solved in 8-8-92
		List<TupleExpr> UnionSubqueries =new UnionDecomposer().getListOfUnionSubQueries(tupleExprToExecute) ;
		if (UnionSubqueries.size()>0)
		{
//			System.out.println("Number of union subqueris:"+ UnionSubqueries.size());
//			System.out.println(UnionSubqueries);
			LinkedBlockingQueue<Tuple> finalResults =new LinkedBlockingQueue<Tuple>() ;
			UnionExecuter unionExec =new UnionExecuter(UnionSubqueries, finalResults) ;
			Thread ThreadUnionExec =new Thread(unionExec) ;
			ThreadUnionExec.start() ;
			return new QueryResultList(finalResults);
		}
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
			//System.out.println(joinTable);
			myLogger.printInfo(joinTable);
			eddyinstance.setJoinVarTojoinMapper(joinTable) ;
			ArrayList<SubQuery> subQs =new SubQueryGenerator().GenerateSubQueries(tupleExprToExecute) ;
			eddyinstance.setSubQueries(subQs);
			eddyinstance.setQueryTupleExpr(tupleExprToExecute);
			//System.out.println("---------------------------------------");
			myLogger.printInfo("---------------------------------------");
			int i= 0 ;
			for (SubQuery sq :subQs)
			{
				myLogger.printInfo("///////////////////////////////////////////////////////\n");
				//System.out.println("///////////////////////////////////////////////////////\n");
				myLogger.printInfo("Sub Query Name: "+sq.getSubQueryName());
				//System.out.println("Sub Query Name: "+sq.getSubQueryName());
				myLogger.printInfo("sub query string "+i+":"+sq.getQueryString());
				//System.out.println("sub query string "+i+":"+sq.getQueryString());
				myLogger.printInfo("sub query "+i+" join vars:"+sq.getJoinVars());
				//System.out.println("sub query "+i+" join vars:"+sq.getJoinVars());
				myLogger.printInfo("sub query "+i+" endpoint :"+sq.getEndpointAdress());
				//System.out.println("sub query "+i+" endpoint :"+sq.getEndpointAdress());
				myLogger.printInfo("\nsub query join bit pattern: "+sq.getReadyBitPattern());
				//System.out.println("\nsub query join bit pattern: "+sq.getReadyBitPattern());
				myLogger.printInfo("\n///////////////////////////////////////////////////////\n");
				//System.out.println("\n///////////////////////////////////////////////////////\n");
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
