package iut.software.federationengine.decomposer;

import iut.software.federationegine.structures.OptimizerException;

import org.openrdf.query.BooleanQuery;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;

public class AskExecuter implements Runnable 
{
	String _AskQueryString ;
	String _EndpointAddress ;
	Integer _TriplePatternIndex ;
	public AskExecuter (String AskQueryString,Integer TriplePatternIndex,String EndpointAddress)
	{
		_AskQueryString =AskQueryString ;
		_EndpointAddress=EndpointAddress ;
		_TriplePatternIndex =TriplePatternIndex ;
	}
	
	@Override
	public void run() 
	{
		Repository repo =new SPARQLRepository(_EndpointAddress) ;
		try 
		{
			repo.initialize() ;
			RepositoryConnection con;
			con = repo.getConnection();
			BooleanQuery remoteAskQuery;
			remoteAskQuery = con.prepareBooleanQuery(QueryLanguage.SPARQL,_AskQueryString);
			Boolean AskResult = remoteAskQuery.evaluate() ;
			System.out.println("\n Query: "+_AskQueryString);
			System.out.println("\n Ask Result: "+AskResult);
			if (AskResult)
			{
				//Add this endpoint to list of endpoints that can answer to this triple pattern 
				Decomposer.getInstance().getTriplePatterns().get(_TriplePatternIndex).AddRelatedEnpoint(_EndpointAddress);
			}
			Decomposer.getInstance().finishAskExecuter() ;
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
		
	}
	
}
