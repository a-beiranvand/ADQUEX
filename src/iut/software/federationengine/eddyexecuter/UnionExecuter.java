package iut.software.federationengine.eddyexecuter;



import iut.software.federationegine.structures.SubQuery;
import iut.software.federationegine.structures.Tuple;
import iut.software.federationengine.optimizer.FedJoinCounter;
import iut.software.federationengine.threadmanagement.PoisonPill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.openrdf.query.algebra.TupleExpr;

public class UnionExecuter implements Runnable 
{

	LinkedBlockingQueue<Tuple> _FinalQueryResults ;
	LinkedBlockingQueue<Tuple> _TempQueryResults ;
	List<TupleExpr> _SubQueriesToExecute ;
	
	public UnionExecuter(List<TupleExpr> SubQueriesToExecute,LinkedBlockingQueue<Tuple> QueryResultList)
	{
		_SubQueriesToExecute=SubQueriesToExecute ;
		_FinalQueryResults = QueryResultList ;
		_TempQueryResults= new LinkedBlockingQueue<Tuple>();
	}
	
	@Override
	public void run() 
	{
		boolean S1IsFed=false ;
		int countFedJoins;
		countFedJoins=(new FedJoinCounter().countFedJoins(_SubQueriesToExecute.get(0))) ;
		if (countFedJoins==0)
			ExeuteSingleEndpoint(_SubQueriesToExecute.get(0));
		else
		{
			EddyExecuter(_SubQueriesToExecute.get(0)) ;
			S1IsFed=true ;
		}
		countFedJoins=(new FedJoinCounter().countFedJoins(_SubQueriesToExecute.get(1))) ;
		if (countFedJoins==0)
			ExeuteSingleEndpoint(_SubQueriesToExecute.get(1));
		else
		{
			if (S1IsFed)
			{
				//in khob nist bayad exception bede 
					System.out.println("Can not execute a union with 2 feds.");
					return ;
			}
			EddyExecuter(_SubQueriesToExecute.get(1)) ;
		}
		byte numOfFinishedSubQueries=0 ;
		while (true)
		{
			try
			{
				Tuple NextResult =_TempQueryResults.take() ;
				if (NextResult instanceof PoisonPill)
				{
					numOfFinishedSubQueries++ ;
					if (numOfFinishedSubQueries==2)
					{
						_FinalQueryResults.put(NextResult);
						return ;
					}
				}
				else
				{
					_FinalQueryResults.put(NextResult);
				}
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void ExeuteSingleEndpoint(TupleExpr QueryToExecute)
	{
		SingleEndpointQueryExecuter seqe = new SingleEndpointQueryExecuter(QueryToExecute,_TempQueryResults) ;
		//seqe implements runnable
		Thread seqeThread =new Thread (seqe) ;
		seqeThread.start() ;
	}
	
	private void EddyExecuter(TupleExpr QueryToExecute)
	{
		Eddy.Initialize(_TempQueryResults) ;
		Eddy eddyinstance =Eddy.getInstance() ;
		JoinTableGenerator jtGenerator = new JoinTableGenerator();
		HashMap<String,ArrayList<Byte>> joinTable = jtGenerator.GenerateJoinTable(QueryToExecute) ;
		eddyinstance.setJoinVarTojoinMapper(joinTable) ;
		ArrayList<SubQuery> subQs =new SubQueryGenerator().GenerateSubQueries(QueryToExecute) ;
		eddyinstance.setSubQueries(subQs);
		eddyinstance.setQueryTupleExpr(QueryToExecute);
		//Eddy is a runnable object create a new thread for eddy.
		Thread eddyThread =new Thread(eddyinstance) ;
		eddyThread.start() ;
	}
	
}
