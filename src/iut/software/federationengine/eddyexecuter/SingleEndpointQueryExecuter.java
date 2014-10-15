package iut.software.federationengine.eddyexecuter;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import iut.software.federationegine.structures.OptimizerException;
import iut.software.federationegine.structures.Tuple;
import iut.software.federationengine.threadmanagement.PoisonPill;
import iut.software.federationengine.threadmanagement.SyncPoisonPill;
import iut.software.federationengine.threadmanagement.ThreadStatus;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.algebra.Service;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.evaluation.QueryBindingSet;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;

public class SingleEndpointQueryExecuter extends QueryModelVisitorBase<OptimizerException> implements Runnable
{
	
	private BlockingQueue<Tuple> _ResultSet ;
	private TupleExpr _QueryToExecute ;
	
	public SingleEndpointQueryExecuter(TupleExpr QueryToExecute,BlockingQueue<Tuple> ResultTupleSet)
	{
		super() ;
		_ResultSet=ResultTupleSet ;
		_QueryToExecute =QueryToExecute;
	}
	
	@Override
	public void run() 
	{
		_QueryToExecute.visit(this) ;
	}
	
	@Override
	public void meet(Service node) throws OptimizerException
	{
		Set<String> serviceVars = node.getServiceVars() ;
		String QueryString =node.getQueryString(serviceVars) ;
		Repository repo =new SPARQLRepository(node.getServiceRef().getValue().stringValue()) ;
		try 
		{
			repo.initialize() ;
			RepositoryConnection con;
			con = repo.getConnection();
			TupleQuery remoteQuery;
			remoteQuery = con.prepareTupleQuery(QueryLanguage.SPARQL,QueryString);
			TupleQueryResult queryResultSet =remoteQuery.evaluate() ;
			while (queryResultSet.hasNext())
			{
				BindingSet BindSet =queryResultSet.next() ;
				QueryBindingSet queryBindSet =new QueryBindingSet(BindSet);
				Tuple receivedTuple =new Tuple() ;
				receivedTuple.setBindingSet(queryBindSet) ;
				_ResultSet.put(receivedTuple) ;
			}
			queryResultSet.close() ;
			 
			SyncPoisonPill eddyReadyPoison =new SyncPoisonPill(ThreadStatus.ReadyToTerminate, PoisonPill.eddyPillType,Thread.currentThread().getName(),0) ;
			_ResultSet.put(eddyReadyPoison);
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
