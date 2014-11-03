package iut.software.federationengine.eddyexecuter;

import iut.software.federationegine.structures.OptimizerException;
import iut.software.federationegine.structures.SubQuery;
import iut.software.federationegine.structures.Tuple;
import iut.software.federationengine.logging.myLogger;
import iut.software.federationengine.threadmanagement.PoisonPill;
import iut.software.federationengine.threadmanagement.ThreadStatus;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.algebra.evaluation.QueryBindingSet;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;

public class SubQueryExecuter implements Runnable {

	
	private final BlockingQueue<Tuple> TupleHeap ;
	private SubQuery serviceSubQuery ;
	private String subQueryName ;
	private int randomCeiling ;
	private Random rndGenerator ;
	private int tupleCounter ;
	private long lastTupleArriveTime ;
	private boolean isFinished ;
	private boolean useRandomPriority ;
	public SubQueryExecuter(SubQuery subQueryToExecute,BlockingQueue<Tuple> TupleHeap,String subQueryId,int randomCeiling,boolean useRandomPriority) 
	{
		this.TupleHeap=TupleHeap;
		this.serviceSubQuery=subQueryToExecute ;
		this.subQueryName=subQueryId;
		this.randomCeiling = randomCeiling ;
		this.rndGenerator=new Random() ;
		this.tupleCounter =0;
		lastTupleArriveTime=0;
		isFinished =false;
		this.useRandomPriority=useRandomPriority;
	}
	
	//--------------------------Getters and Setters
	
	public String getSubQueryName() 
	{
		return subQueryName;
	}
	
	
	public long getLastTupleArriveTime() 
	{
		return lastTupleArriveTime;
	}
	
	public long getDelay ()
	{
		if (isFinished)
			return 0;
		else
			return System.currentTimeMillis()- this.lastTupleArriveTime;
	}
	//------------------------------------------


	@Override
	public void run() {
		
		ExecuteSubQuery() ;

	}
	
	

	private void ExecuteSubQuery() throws OptimizerException
	{
		Repository repo =new SPARQLRepository(serviceSubQuery.getEndpointAdress().toString()) ;
		try 
		{
			repo.initialize() ;
			RepositoryConnection con;
			con = repo.getConnection();
			TupleQuery remoteQuery;
			remoteQuery = con.prepareTupleQuery(QueryLanguage.SPARQL,serviceSubQuery.getQueryString());
			TupleQueryResult queryResultSet =remoteQuery.evaluate() ;
			int randomPriority=0 ;
			int rnd =0 ;
			while (queryResultSet.hasNext())
			{
				tupleCounter++ ;
				this.lastTupleArriveTime=System.currentTimeMillis() ;
				BindingSet BindSet =queryResultSet.next() ;
				QueryBindingSet queryBindSet =new QueryBindingSet(BindSet);
				Tuple receivedTuple =new Tuple() ;
				receivedTuple.getSubQueryList().add(serviceSubQuery.getSubQueryName()) ;
				receivedTuple.setReadyBits(serviceSubQuery.getReadyBitPattern()) ;
				receivedTuple.setDoneBits(0);
				receivedTuple.setBindingSet(queryBindSet) ;
				if (useRandomPriority)
				{
					rnd=rndGenerator.nextInt(this.randomCeiling) ;
					randomPriority = rnd - this.tupleCounter ;
					receivedTuple.setPriority(randomPriority);
				}
				else
				{
					receivedTuple.setPriority(0);
				}
				//receivedTuple.setPriority(1);
				TupleHeap.put(receivedTuple);
				//System.out.println(this.subQueryName+" "+tupleCounter);
			}
			//Following jeens recommendation i close the TupleQueryResult
			queryResultSet.close() ;
			//Say eddy that this thread has finished it's work .
			isFinished=true;
			PoisonPill poisonSubQueryFinished=new PoisonPill(ThreadStatus.Termintaed, PoisonPill.subQueryExecuterPillType, Thread.currentThread().getName());
			poisonSubQueryFinished.setPriority(0);
			TupleHeap.add(poisonSubQueryFinished) ;
			//========================TEST=================
			myLogger.printInfo("Sub Query "+this.subQueryName+"Finished with"+tupleCounter+" Tuples.");
			//System.out.println("Sub Query "+this.subQueryName+"Finished with"+tupleCounter+" Tuples.");
			Eddy.getInstance().incFinishedSqExecuters(false) ;
		}
		catch (RepositoryException e) 
		{
			OptimizerException optm =new OptimizerException(e);
			throw optm;
		}
		catch (MalformedQueryException e) 
		{
			OptimizerException optm =new OptimizerException(e);
			throw optm;
		}
		catch (QueryEvaluationException e) 
		{
			OptimizerException optm =new OptimizerException(e);
			throw optm;
		} 
		catch (InterruptedException e) 
		{
			OptimizerException optm =new OptimizerException(e);
			throw optm;
		}
	}

}
