package iut.software.federationengine.decomposer;

import java.util.ArrayList;


import iut.software.federationegine.structures.OptimizerException;
import iut.software.federationengine.parser.Parser;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.algebra.TupleExpr;



public class Decomposer {
	
	
	private static Decomposer instance ;
	private Integer numOfRunningAskExc ;
	private ArrayList<TriplePattern> generatedTriplePtrns ; 
	private Decomposer ()
	{
		numOfRunningAskExc = -1 ;
	}
	
	public ArrayList<TriplePattern> getTriplePatterns ()
	{
		return generatedTriplePtrns ;
	}
	
	public static synchronized void Initialize () throws OptimizerException 
	{
		if (instance !=null)
		{
			OptimizerException exp =new OptimizerException("Decomposer is initialized! Do not initailize it again.") ;
			throw exp ;
		}
		instance =new Decomposer() ;
	}
	
	public static synchronized Decomposer getInstance ()
	{
		if (instance != null)
		{
			return instance ;
		}
		else
		{
			OptimizerException e =new  OptimizerException("Exception: Decomposer is not initialized") ;
			throw e ;
		}
	}
	
	
	public  void finishAskExecuter ()
	{
		synchronized (this)
		{
			numOfRunningAskExc -- ;
			this.notify();	
		}
	}
	
	
	private boolean isAllAskExecsFinished ()
	{
		if (numOfRunningAskExc ==-1)
		{
			OptimizerException e =new OptimizerException("Exception: You have not started any Ask Executer") ;
			throw e ;
		}
		else if (numOfRunningAskExc == 0)
		{
			return true ;
		}
		else
			return false ;
	}
	/**
	 * @param args
	 */
	
	
	private void DecomposeTest ()
	{
//		String queryString=	"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>PREFIX owl:<http://www.w3.org/2002/07/owl#>prefix nytimes: <http://data.nytimes.com/elements/>prefix dc:<http://purl.org/dc/terms/>prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>prefix dbpedia: <http://dbpedia.org/resource/>prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
//				"SELECT * WHERE\n"+ 	
//				 "{ \n" +
//					 	"?film dbpedia-owl:director ?director.\n" +				 	
//					 	"?x owl:sameAs ?film .\n" +
//					 	"?x linkedMDB:genre ?genre.\n" +
//				 "}"	;
		
		String queryString = QueryStringsStandard.myquery3 ;
		try
		{
			TupleExpr tExpr = Parser.parseQueryString(queryString) ;
			System.out.println(tExpr);
			TriplePatternGenerator g= new TriplePatternGenerator() ;
			generatedTriplePtrns= g.GenerateTriplePatternsAndTheirAsks(tExpr);
			ArrayList<String> EndpointURIs =new ArrayList<String>() ;
			EndpointURIs.add("http://192.168.1.6:8890/sparql") ;
			EndpointURIs.add("http://192.168.1.7:8890/sparql") ;
			EndpointURIs.add("http://192.168.1.8:8890/sparql") ;
			numOfRunningAskExc =generatedTriplePtrns.size() * EndpointURIs.size() ;
			int triplePtrnIndex = 0 ;
			for (  TriplePattern triplePtrn : generatedTriplePtrns) 
			{
				System.out.println("\n"+triplePtrn.getStatementPattern());
				System.out.println("\n"+triplePtrn.getAskQueryString());
				for (String EndpointAddress : EndpointURIs  )
				{
					Thread askExecThr =new Thread(new AskExecuter(triplePtrn.getAskQueryString(),triplePtrnIndex,EndpointAddress)) ;
					askExecThr.start() ;
				}
				triplePtrnIndex++;
			}
			synchronized (this) 
			{
				while (!isAllAskExecsFinished())
				{
					this.wait() ;
				}
			}
			System.out.println("************ DECOMPOSITION RESULTS***************");
			for ( TriplePattern triplePtrn : generatedTriplePtrns)
			{
				System.out.println("\nPattern: "+triplePtrn.getStatementPattern().toString());
				System.out.println("Related Endpoints: "+triplePtrn.getRelatedEndpoints().toString());
			}
		}
		catch (MalformedQueryException exp)
		{
			System.out.println("Exception:");
			System.out.println(exp.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Synchronizing Error");
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) 
	{
		long startTime =System.currentTimeMillis();
		Decomposer.Initialize() ;
		Decomposer.getInstance().DecomposeTest() ;
		long executionTime =System.currentTimeMillis()-startTime;
		System.out.println("\nDecomposition Time:"+executionTime);
	}
}
