import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.evaluation.QueryBindingSet;

import iut.software.federationegine.structures.OptimizerException;
import iut.software.federationegine.structures.QueryResultList;
import iut.software.federationengine.eddyexecuter.FederatedQueryExecuter;
import iut.software.federationengine.eddyexecuter.JoinTableGenerator;
import iut.software.federationengine.logging.myLogger;
import iut.software.federationengine.optimizer.Optimizer;
import iut.software.federationengine.parser.Parser;


public class hook {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		
//		String queryString ="SELECT ?actor ?news WHERE {\n"+
//			   
//				   "SERVICE <http://linkedmdb.org/sparql> {" +
//				   	"?film <http://purl.org/dc/terms/title> 'Tarzan' .}\n"+
//				   	"SERVICE <http://linkedmdb.org/sparql> {" +
//				   	"?film <http://data.linkedmdb.org/resource/movie/actor> ?actor .}\n"+
//				   	"SERVICE <http://linkedmdb.org/sparql> {" +
//				   	"?actor <http://www.w3.org/2002/07/owl#sameAs> ?x.\n" +
//				   	"}"+
//				   "SERVICE <http://nytimes.com/sparql> {" +
//				   "?y <http://www.w3.org/2002/07/owl#sameAs> ?x .}\n"+
//				   "SERVICE <http://nytimes.com/sparql> {" +
//				   "?y <http://data.nytimes.com/elements/topicPage> ?news\n" +
//				   "}" +
//			   
//			"}" ;
		
//		String queryString ="SELECT ?actor ?news WHERE {\n"+
//				   
//				 
//				   	"?film <http://purl.org/dc/terms/title> 'Tarzan' .\n"+			
//				   	"?film <http://data.linkedmdb.org/resource/movie/actor> ?actor .\n"+
//				   	"?actor <http://www.w3.org/2002/07/owl#sameAs> ?x.\n" +
//				 
//				 
//				   "?y <http://www.w3.org/2002/07/owl#sameAs> ?x .\n"+
//				   "?y <http://data.nytimes.com/elements/topicPage> ?news\n" +
//				 
//			   
//			"}" ;
//		String queryString ="SELECT ?president ?party ?page WHERE {\n"+
//				   "SERVICE <http://dbpedia.org/sparql> {" +
//				   "?president <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>\n"+ 
//				   "<http://dbpedia.org/ontology/President> .\n" +
//				   
//				   "?president <http://dbpedia.org/ontology/nationality>\n"+  
//				   "<http://dbpedia.org/resource/United_States> .\n" +
//				   
//					      "?president <http://dbpedia.org/ontology/party> ?party .}\n" +
		
		//====================================FIRST TEST QUERY=============================
//		String queryString ="select ?x ?page where {"+
//			      "SERVICE <http://192.168.145.128:8890/sparql> {"+
//			      "?x <http://data.nytimes.com/elements/topicPage> ?page .}\n" +
//			      "SERVICE <http://192.168.145.128:8890/sparql> {"+
//			      "?x <http://www.w3.org/2002/07/owl#sameAs> ?president .}\n"+
//			      "SERVICE <http://192.168.145.128:8890/sparql>{"+
//			      "?x a <http://www.w3.org/2004/02/skos/core#Concept>.}\n"+
//					  "}"  ;
		//==================================================================================
//		String queryString ="SELECT ?president  ?page WHERE {\n"+
//				   "SERVICE <http://dbpedia.org/sparql> {" +
//				   "?president <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>\n"+ 
//				   "<http://dbpedia.org/ontology/President> .}\n" +
//				   "SERVICE <http://dbpedia.org/sparql> {"+
//				   "?president <http://dbpedia.org/ontology/nationality>\n"+  
//				   "<http://dbpedia.org/resource/United_States> .}\n" +
//				   "SERVICE <http://dbpedia.org/sparql> {" +
//					      "?president <http://dbpedia.org/ontology/party> ?party .}\n" +
//				   "SERVICE <http://192.168.145.128:8890/sparql>" +
//				   "{?x <http://data.nytimes.com/elements/topicPage> ?page .}" +
//				   //"SERVICE <http://192.168.145.128:8890/sparql>{"+
//				   "SERVICE <http://192.168.145.128:8890/sparql>" +
//				   "{?x <http://www.w3.org/2002/07/owl#sameAs> ?president .}"+
//					  "}"  ;
//		String queryString="SELECT ?film ?director ?genre WHERE {\n"+
//							"SERVICE <http://dbpedia.org/sparql> {\n"+
//							"?film <http://dbpedia.org/ontology/director>  ?director .\n"+
//							"?director <http://dbpedia.org/ontology/nationality> <http://dbpedia.org/resource/Italy> .\n"+
//							"}\n"+
//							"SERVICE <http://linkedmdb.org/sparql>\n"+
//							"{\n"+
//							"?x <http://www.w3.org/2002/07/owl#sameAs> ?film .\n"+
//							"?x <http://data.linkedmdb.org/resource/movie/genre> ?genre .\n"+
//							"}\n" +
//					"}" ;
		
		
		//==============================LATEST TEST QUERY ==================================
//		String queryString ="SELECT ?president  ?page WHERE {\n"+
//					"SERVICE <http://dbpedia.org/sparql> {" +
//					"?president <http://dbpedia.org/ontology/party> ?party ." +
//					//"}\n" +
//					
//				   //"SERVICE <http://dbpedia.org/sparql> {" +
//					   "?president <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>\n"+ 
//					   "<http://dbpedia.org/class/yago/PresidentsOfTheUnitedStates> .\n" +
//				   "}"+				   
//					"SERVICE <http://192.168.145.128:8890/sparql>{" +
//					"?x <http://data.nytimes.com/elements/topicPage> ?page. " +
//					"}" +				  
//				   //"SERVICE <http://192.168.145.128:8890/sparql>{"+
//				   "SERVICE <http://192.168.145.128:8890/sparql>{" +
//				   		"?x <http://www.w3.org/2002/07/owl#sameAs> ?president ." +
//				   	"}"+
//				   		
// "SERVICE <http://192.168.145.128:8890/sparql>{" +
//	"?x <http://www.w3.org/2002/07/owl#sameAs> ?sameas ." +
//"}"+
		
		
				   
//				"SERVICE <http://192.168.145.128:8890/sparql>{" +
//					"?x <http://data.nytimes.com/elements/first_use> ?firstuse ." +
//				"}"+
//				
//				"SERVICE <http://192.168.145.128:8890/sparql>{" +
//				"?x <http://data.nytimes.com/elements/latest_use> ?lastuse ." +
//				"}"+
//				
//				"SERVICE <http://192.168.145.128:8890/sparql>{" +
//				"?x <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?lat ." +
//				"}"+
		//	"}"  ;
		
		
		//First real test
//		String queryString ="PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>\n" +
//				"PREFIX dbpedia:<http://dbpedia.org/resource/>\n" +
//				"PREFIX linkedMDB:<http://data.linkedmdb.org/resource/movie/>\n" +
//		
//				"SELECT ?film ?director ?genre WHERE {\n "+
//				"SERVICE <http://192.168.1.4:8890/sparql>{\n" +
//				"?film dbpedia-owl:director ?director."+
//				"?director dbpedia-owl:nationality ?n ."+
//				" }\n "+
//				"SERVICE <http://192.168.1.2:8890/sparql>{\n " +
//					"?x owl:sameAs ?film ."+
//					" ?x linkedMDB:genre ?genre "+
//				" }\n" +
//		"}" ;
		
		//My Query
//		String queryString ="PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>"+
//							"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
//							"PREFIX drugbank:<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>" +
//							"PREFIX foaf:<http://xmlns.com/foaf/0.1/>" +
//							"PREFIX sider:<http://wifo5-04.informatik.uni-mannheim.de/sider/resource/sider/>" +
//							"select ?dbpediadrug " +
//							"where" +
//							"{" +
//								"SERVICE <http://192.168.1.2:8890/sparql>" +
//								"{" +
//									"?dbpediadrug a dbpedia-owl:Drug. 	" +
//									"?dbpediadrug owl:sameAs ?drugbank." +
//								"}" +
//								"SERVICE <http://192.168.1.3:8890/sparql>" +
//								"{" +
//									"?drugbank a drugbank:drugs." +
//									
//									
//								"}" +
////								"SERVICE <http://wifo5-04.informatik.uni-mannheim.de/sider/sparql>" +
////								"{" +
////									"?drug a sider:drugs." +
////									"?drug owl:sameAs ?dbpediadrug." +
////									
////								"}" +
//								
//							"}";
		
//		String queryString ="PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>"+
//				"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
//				"prefix nytimes: <http://data.nytimes.com/elements/>" +
//				"prefix dc:<http://purl.org/dc/terms/>" +
//				"prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>"+
//				"prefix dbpedia: <http://dbpedia.org/resource/>" +
//				"prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
//				"SELECT * WHERE \n " +
//				"{ \n" +
//					"SERVICE <http://192.168.1.2:8890/sparql>\n" +
//					"{\n"+							
//						"?dbfilm a dbpedia-owl:Film.\n"+							
//						"?dbfilm dbpedia-owl:budget ?budget .\n"+							
//						"?dbfilm dbpedia-owl:director ?director.\n"+							
//					"}\n"+							
//					"SERVICE <http://192.168.1.3:8890/sparql>\n"+							
//					"{\n"+							
//						"?film a linkedMDB:film .\n"+							
//						"?film owl:sameAs ?dbfilm.\n" +
//						"?film linkedMDB:genre <http://data.linkedmdb.org/resource/film_genre/4>.\n" +
//						"?film linkedMDB:actor ?actor.\n"+							
//					"}\n"+							
//					"SERVICE <http://192.168.1.4:8890/sparql>\n"+							
//					"{\n"+							
//						"?ydirector owl:sameAs ?director.\n" +
//						"?ydirector nytimes:topicPage ?newspage.\n" +
//					"}\n"+							
//				
//			  "}\n" ;
//	
//		String queryString="PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>PREFIX owl:<http://www.w3.org/2002/07/owl#>prefix nytimes: <http://data.nytimes.com/elements/>prefix dc:<http://purl.org/dc/terms/>prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>prefix dbpedia: <http://dbpedia.org/resource/>prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
//				"SELECT * WHERE\n"+ 	
//				 "{\n"+ 
//					 "SERVICE <http://192.168.1.3:8890/sparql>\n" +
//					 "{\n"+			
//						"?film dc:title ?title .\n" +
//					 "}\n"+
//					 "SERVICE <http://192.168.1.3:8890/sparql>\n" +
//					 "{\n"+			
//						"?film linkedMDB:actor ?actor .\n" +
//					 "}\n"+
//					 "SERVICE <http://192.168.1.3:8890/sparql>\n" +
//					 "{\n"+			
//						"?actor owl:sameAs ?x.\n" +
//					 "}\n"+
//					"SERVICE <http://192.168.1.4:8890/sparql>\n" +
//					"{\n" +
//						"?y owl:sameAs ?x .\n" +
//					
//					
//						"?y nytimes:topicPage ?news.\n" +
//					"}\n" +
//				"}"		;
		String queryString=	"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>PREFIX owl:<http://www.w3.org/2002/07/owl#>prefix nytimes: <http://data.nytimes.com/elements/>prefix dc:<http://purl.org/dc/terms/>prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>prefix dbpedia: <http://dbpedia.org/resource/>prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
		"SELECT * WHERE\n"+ 	
		 "{ \n" +
		 	"SERVICE <http://dbpedia.org/sparql>\n" +
		 	"{\n"+				
			 	"?film dbpedia-owl:director ?director.\n" +				 	
		 	"}\n" +
			 "SERVICE <http://192.168.1.3:8890/sparql>\n" +
			 "{\n"+
			 	"?x owl:sameAs ?film .\n" +
			 	"?x linkedMDB:genre ?genre.\n" +
			 "}\n"+
		 "}"	;
		myLogger.queryId="TESTMOTIVATION"  ;
		myLogger.queryString=queryString ;
		myLogger.systemName="MySystem-Lottery" ;
		myLogger.folderToSave="H:\\Isfahan University Of Technology\\Term 3\\Project\\Final Evaluation Results\\Adaptivity Test\\Effect of random priority\\";
		myLogger.timeout=5;
		try
		{
			long startTime =System.currentTimeMillis();
			TupleExpr tExpr = Parser.parseQueryString(queryString) ;
//			System.out.println(tExpr.toString());
			Optimizer.Optimize(tExpr,true) ;
//			System.out.println("Optimized Plan:\n");
//			System.out.println(tExpr);
//			Joinprinter jp =new Joinprinter();			
//			tExpr.visit(jp);
//			JoinTableGenerator jt =new JoinTableGenerator() ;
//			System.out.println(jt.GenerateJoinTable(tExpr));
//			System.out.println("Amin!");
//			System.out.println("Amin!");
			
			QueryResultList results = FederatedQueryExecuter.ExecuteTupleExpr(tExpr);
			BindingSet bindings ;
			int countTuples = 0;
			boolean isFirstTuple =true;
			while (true)
			{
				bindings =results.next() ;
				System.out.println(bindings);
				if (bindings == null )
					break ;
				if (isFirstTuple)
				{
					long firstTuple =System.currentTimeMillis()-startTime ;
					myLogger.firstTuple =firstTuple ;
					//System.out.println("First Tuple = "+firstTuple);
					isFirstTuple=false;
				}
				//System.out.println(bindings.toString());
				countTuples++;
				//System.out.println("||||||Count Results ="+countTuples);
			
			}
			long executionTime =System.currentTimeMillis()-startTime;
			myLogger.executionTime=executionTime ;
			myLogger.numberOfResultTuples =countTuples ;
			
			//System.out.println("Execution Time = "+ executionTime);
			Thread.sleep(2000) ;
			myLogger.printStatistics() ;
			myLogger.saveStatistics();
			System.exit(0);
			
			
		}
		catch (MalformedQueryException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace() ;
		}
		catch (OptimizerException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
