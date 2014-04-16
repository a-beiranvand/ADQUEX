package iut.software.federationegine.structures;

import iut.software.federationengine.threadmanagement.PoisonPill;
import java.util.concurrent.BlockingQueue;
import org.openrdf.query.BindingSet;

public class QueryResultList 
{

	private BlockingQueue<Tuple> resultTuples ;
	private boolean hasNext ; 
	public QueryResultList(BlockingQueue<Tuple> resultTuples) 
	{
		this.resultTuples = resultTuples;
		this.hasNext =true;
	}
	
	
	
	
	/*
	 * Returns next tuple and removes it, or returns null if there is no more results.
	 */
	public BindingSet next()  
	{
		if (!hasNext)
			return null ;
		try 
		{
			Tuple nextResult =resultTuples.take() ;
			if (nextResult instanceof PoisonPill)
			{
				this.hasNext=false ;
				return null ;
			}
			return nextResult.getBindingSet() ;
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}

		

}
