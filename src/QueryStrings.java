
public class QueryStrings 
{

	
	public static String cd1Service= 
			"PREFIX owl:      <http://www.w3.org/2002/07/owl#> "+
			"PREFIX dbpedia:  <http://dbpedia.org/resource/> "+
				"SELECT ?predicate ?object WHERE {\n"+
				  "{\n"+
			  		"SERVICE <http://172.16.3.171:8890/sparql>\n"+
				  		"{\n"+		
				  			"dbpedia:Barack_Obama ?predicate ?object "+ 
				  		"}\n"+
				  "}\n"+
				  "UNION "+
				   "{\n"+
					   	"SERVICE <http://172.16.3.178:8890/sparql>\n"+
				   		"{\n"+
					   		"?subject owl:sameAs dbpedia:Barack_Obama. "+
					   		"?subject ?predicate ?object\n"+ 
		   				"}\n" +
   				  "}\n" +
				"}\n" ;
	
	public static String cd2Service =
			
			"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>PREFIX owl:<http://www.w3.org/2002/07/owl#>prefix nytimes: <http://data.nytimes.com/elements/>prefix dc:<http://purl.org/dc/terms/>prefix lmdb: <http://data.linkedmdb.org/resource/movie/>prefix dbpedia: <http://dbpedia.org/resource/>" +
			" SELECT * WHERE\n"+ 	
			"{\n"+ 		
				"SERVICE <http://172.16.3.171:8890/sparql>\n"+			
				"{\n"+			
					"?obama dbpedia-owl:party ?party .\n" +
					"Filter (?obama=<http://dbpedia.org/resource/Barack_Obama>)	"+
				"}\n"+
				"SERVICE <http://172.16.3.178:8890/sparql>\n"+	
				"{\n"+	
					"?x nytimes:topicPage ?page .\n"		+
					"?x owl:sameAs ?obama .\n"+
					"Filter (?obama=<http://dbpedia.org/resource/Barack_Obama>)	"+
				"}\n"+
			"}"		;

	
	public static String cd3Service =
			"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>PREFIX owl:<http://www.w3.org/2002/07/owl#>prefix nytimes: <http://data.nytimes.com/elements/>prefix dc:<http://purl.org/dc/terms/>prefix lmdb: <http://data.linkedmdb.org/resource/movie/>prefix dbpedia: <http://dbpedia.org/resource/>prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
			"SELECT * WHERE \n"+ 	
			 "{\n" 	+
				 "SERVICE <http://172.16.3.171:8890/sparql>\n" +
				 "{\n"+				
					"?pres rdf:type dbpedia-owl:President . \n"+		
					"?pres dbpedia-owl:nationality dbpedia:United_States . \n"+		
					"?pres dbpedia-owl:party ?party . \n"		+
				"}\n"+
				"SERVICE <http://172.16.3.178:8890/sparql>\n" +
				"{\n"+			
					"?x nytimes:topicPage ?page . \n"		+
					"?x owl:sameAs ?pres. \n" 		+
				"}" +
			"}"		;
	
	public static String cd4Service =
			
			"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>PREFIX owl:<http://www.w3.org/2002/07/owl#>prefix nytimes: <http://data.nytimes.com/elements/>prefix dc:<http://purl.org/dc/terms/>prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>prefix dbpedia: <http://dbpedia.org/resource/>prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
			"SELECT * WHERE\n"+ 	
			 "{\n"+ 
				 "SERVICE <http://172.16.3.164:8890/sparql>\n" +
				 "{\n"+			
					"?film dc:title 'Tarzan' .\n" +
					"?film linkedMDB:actor ?actor .\n" +
					"?actor owl:sameAs ?x.\n" +
				"}\n"+
				"SERVICE <http://172.16.3.178:8890/sparql>\n" +
				"{\n" +
					"?y owl:sameAs ?x .\n" +
					"?y nytimes:topicPage ?news.\n" +
				"}\n" +
			"}"		;
	public static String cd5Service =
			"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>PREFIX owl:<http://www.w3.org/2002/07/owl#>prefix nytimes: <http://data.nytimes.com/elements/>prefix dc:<http://purl.org/dc/terms/>prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>prefix dbpedia: <http://dbpedia.org/resource/>prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
			"SELECT * WHERE\n"+ 	
			 "{ \n" +
			 	"SERVICE <http://172.16.3.171:8890/sparql>\n" +
			 	"{\n"+				
				 	"?film dbpedia-owl:director ?director.\n" +
				 	"?director dbpedia-owl:nationality dbpedia:Italy .\n" +
			 	"}\n" +
				 "SERVICE <http://172.16.3.164:8890/sparql>\n" +
				 "{\n"+
				 	"?x owl:sameAs ?film .\n" +
				 	"?x linkedMDB:genre ?genre.\n" +
				 "}\n"+
			 "}"	;
	public static String cd6Service =
			"PREFIX geonames:    <http://www.geonames.org/ontology#>\n"+
			"PREFIX foaf:        <http://xmlns.com/foaf/0.1/>\n"+
			"SELECT ?name ?location  WHERE \n" +
			"{\n"+
				"SERVICE <http://172.16.3.178:8890/sparql>\n" +
				"{\n"+				
					"?artist foaf:name ?name .\n"+
					"?artist foaf:based_near ?location .\n"+
				"}\n"+
				"SERVICE <http://172.16.3.175:8890/sparql>\n" +
				"{\n"+
					"?location geonames:parentFeature ?germany .\n"+
					"?germany geonames:name 'Federal Republic of Germany'\n"+
				"}\n"+
			"}";
	
	public static String cd7Service =
			"PREFIX owl:         <http://www.w3.org/2002/07/owl#>\n"+
			"PREFIX nytimes:     <http://data.nytimes.com/elements/>\n"+
			"PREFIX geonames:    <http://www.geonames.org/ontology#>\n"+
			"SELECT ?location ?news WHERE\n"+ 
			"{\n"+
				"SERVICE <http://172.16.3.175:8890/sparql>\n" +
				"{\n"+				
					"?location geonames:parentFeature ?parent .\n"+
					"?parent geonames:name 'California'  .\n"+
				"}\n"+
				"SERVICE <http://172.16.3.178:8890/sparql>\n" +
				"{\n"+				
					"?y owl:sameAs ?location.\n"+
					"?y nytimes:topicPage ?news\n"+
				"}\n"+
			"}";

	
	public static String myquery1Service =
			"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>"+
					"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
					"prefix nytimes: <http://data.nytimes.com/elements/>" +
					"prefix dc:<http://purl.org/dc/terms/>" +
					"prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>"+
					"prefix dbpedia: <http://dbpedia.org/resource/>" +
					"prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
					"SELECT * WHERE \n " +
					"{ \n" +
					"SERVICE <http://172.16.3.171:8890/sparql>" +
					"{\n" +
						"?dbfilm a dbpedia-owl:Film.\n"+
						"?dbfilm dbpedia-owl:director ?director.\n" +
						
					"}\n" +
					"SERVICE <http://172.16.3.164:8890/sparql>\n" +
					"{\n"+
						"?imdbfilm owl:sameAs ?dbfilm.\n" +
						"?imdbfilm dc:title ?title.\n"+
					"}\n" +
				  "SERVICE <http://172.16.3.178:8890/sparql>\n" +
				  "{\n" +
				  		"?concept owl:sameAs ?director.\n" +
				  		"?concept nytimes:topicPage ?directorpage.\n"+
				  "}\n" +
			  "}\n" ;


	public static String myquery2Service =
			"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>"+
					"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
					"prefix nytimes: <http://data.nytimes.com/elements/>" +
					"prefix dc:<http://purl.org/dc/terms/>" +
					"prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>"+
					"prefix dbpedia: <http://dbpedia.org/resource/>" +
					"prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
					"SELECT * WHERE \n " +
					"{ \n" +
					"SERVICE <http://172.16.3.171:8890/sparql>" +
					"{\n" +
						"?dbfilm a dbpedia-owl:Film.\n"+
						"?dbfilm dbpedia-owl:budget ?budget .\n" +
						"?dbfilm dbpedia-owl:director ?director.\n" +
						
					"}\n" +
					"SERVICE <http://172.16.3.164:8890/sparql>\n" +
					"{\n"+
						"?film a linkedMDB:film .\n" +
						"?film owl:sameAs ?dbfilm." +
						"?film linkedMDB:actor ?actor.\n" +
					"}\n" +
				  "SERVICE <http://172.16.3.178:8890/sparql>\n" +
				  "{\n" +
						"?ydirector owl:sameAs ?director."+
						"?ydirector nytimes:topicPage ?newspage."+
				  "}\n" +
			  "}\n" ;

	
	
	public static String myquery3Service =
			"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>"+
					"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
					"prefix nytimes: <http://data.nytimes.com/elements/>" +
					"prefix dc:<http://purl.org/dc/terms/>" +
					"prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>"+
					"prefix dbpedia: <http://dbpedia.org/resource/>" +
					"prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
					"SELECT * WHERE \n " +
					"{ \n" +
					"SERVICE <http://172.16.3.171:8890/sparql>" +
					"{\n" +
						"?dbfilm a dbpedia-owl:Film.\n"+
						"?dbfilm dbpedia-owl:budget ?budget .\n" +
						"?dbfilm dbpedia-owl:director ?director.\n" +
						
					"}\n" +
					"SERVICE <http://172.16.3.164:8890/sparql>\n" +
					"{\n"+
						"?film a linkedMDB:film .\n" +
						"?film owl:sameAs ?dbfilm." +
						"?film linkedMDB:genre <http://data.linkedmdb.org/resource/film_genre/4>." +
						"?film linkedMDB:actor ?actor.\n" +
					"}\n" +
				  "SERVICE <http://172.16.3.178:8890/sparql>\n" +
				  "{\n" +
						"?ydirector owl:sameAs ?director."+
						"?ydirector nytimes:topicPage ?newspage."+
				  "}\n" +
			  "}\n" ;
	
	public static String ls1Service =
			"PREFIX drugbank: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/> "+
			"PREFIX dbpo-drug:  <http://dbpedia.org/ontology/drug/> "+
			"SELECT ?drug ?melt WHERE {\n"+
			"{ SERVICE <http://172.16.3.175:8890/sparql>\n "+
			   "{ ?drug drugbank:meltingPoint ?melt . }\n"+
			"}\n"+
			  "UNION \n"+
			"{ SERVICE <http://172.16.3.171:8890/sparql>"+
			   "{ ?drug dbpo-drug:meltingPoint ?melt . }"+
			"}\n"+
			"}" ;
	
	public static String ls2Service =
			"PREFIX owl:  <http://www.w3.org/2002/07/owl#> "+
			"PREFIX drug: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/> "+
				"SELECT ?predicate ?object WHERE { \n" +
				"{ SERVICE <http://172.16.3.175:8890/sparql>\n "+
				   "{ drug:DB00201 ?predicate ?object . }\n"+
				"}\n"+
				  "UNION\n"+
				"{ SERVICE <http://172.16.3.175:8890/sparql>\n "+
				   "{ drug:DB00201 owl:sameAs ?caff . "+
				     "?caff ?predicate ?object . } \n }\n"+
			     
				"}" ;	
	
public static String ls3Service =
			
			"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"+
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
			"PREFIX drugbank: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>\n"+
			"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>\n"+
			"SELECT ?Drug ?IntDrug ?IntEffect WHERE \n" +
			"{\n"+
					"SERVICE <http://172.16.3.171:8890/sparql>\n" +
					"{\n"+
					   "?Drug rdf:type dbpedia-owl:Drug .\n"+
					"}\n"+   
					"SERVICE <http://172.16.3.175:8890/sparql>\n" +
					"{\n"+
					   "?y owl:sameAs ?Drug .\n"+
					   "?Int drugbank:interactionDrug1 ?y .\n"+
					   "?Int drugbank:interactionDrug2 ?IntDrug .\n"+
					   "?Int drugbank:text ?IntEffect .\n" +
				   "}\n"+  
			"}";
	
	public static String ls4Service =
			
			"PREFIX rdf:      <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
			"PREFIX drugbank: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>\n"+
			"PREFIX drug-cat: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugcategory/>\n"+
			"PREFIX kegg:     <http://bio2rdf.org/ns/kegg#>\n"+
			"SELECT ?drugDesc ?cpd ?equation WHERE \n" +
			"{\n"+
				"SERVICE <http://172.16.3.175:8890/sparql>\n" +
				"{\n"+
				   "?drug drugbank:drugCategory drug-cat:cathartics .\n"+
				   "?drug drugbank:keggCompoundId ?cpd .\n"+
				   "?drug drugbank:description ?drugDesc .\n" +
			   "}\n"+
			   "SERVICE <http://172.16.3.164:8890/sparql>\n" +
			   "{\n"+
				   "?enzyme kegg:xSubstrate ?cpd .\n"+
				   "?enzyme rdf:type kegg:Enzyme .\n"+
				   "?reaction kegg:xEnzyme ?enzyme .\n"+
				   "?reaction kegg:equation ?equation .\n" +
			   "}\n"+ 
			"}";

	
	public static String ls5Service =
			
			"PREFIX rdf:      <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
			"PREFIX drugbank: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>\n"+
			"PREFIX bio2rdf:  <http://bio2rdf.org/ns/bio2rdf#>\n"+
			"PREFIX dc:       <http://purl.org/dc/elements/1.1/>\n"+
			"SELECT ?drug ?keggUrl ?chebiImage WHERE \n" +
			"{\n" +
				"SERVICE <http://172.16.3.175:8890/sparql>\n" +
				"{\n"+
				   "?drug rdf:type drugbank:drugs .\n"+
				   "?drug drugbank:keggCompoundId ?keggDrug .\n"+
				   "?drug drugbank:genericName ?drugBankName .\n" +
			   "}"+
			   "SERVICE <http://172.16.3.164:8890/sparql>\n" +
			   "{\n"+
				   "?keggDrug bio2rdf:url ?keggUrl .\n" +
			   "}\n"+
			   "SERVICE <http://172.16.3.178:8890/sparql>\n" +
			   "{\n"+
				   "?chebiDrug dc:title ?drugBankName .\n"+
				   "?chebiDrug bio2rdf:image ?chebiImage .\n" +
			   "}\n"+ 
			"}\n";
	
	public static String ls6Service =
			"PREFIX rdf:      <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
			"PREFIX drugbank: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>\n"+
			"PREFIX kegg:     <http://bio2rdf.org/ns/kegg#>\n"+
			"PREFIX bio2rdf:  <http://bio2rdf.org/ns/bio2rdf#>\n"+
			"PREFIX dc:       <http://purl.org/dc/elements/1.1/>\n"+
			"PREFIX drug-cat: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugcategory/>\n"+
			"SELECT ?drug ?title WHERE " +
			"{\n"+
				"SERVICE <http://172.16.3.175:8890/sparql>\n" +
				"{\n"+
					"?drug drugbank:drugCategory drug-cat:micronutrient .\n"+
					"?drug drugbank:casRegistryNumber ?id .\n" +
				"}\n"+
				"SERVICE <http://172.16.3.164:8890/sparql>\n" +
			   "{\n"+
					"?keggDrug rdf:type kegg:Drug .\n"+
					"?keggDrug bio2rdf:xRef ?id .\n"+
					"?keggDrug dc:title ?title .\n" +
			   "}\n"+ 
			"}";
	
	
	public static String ls7Service =
			"PREFIX owl:      <http://www.w3.org/2002/07/owl#>\n"+
			"PREFIX rdf:      <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
			"PREFIX drugbank: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>\n"+
			"PREFIX kegg:     <http://bio2rdf.org/ns/kegg#>\n"+
			"PREFIX bio2rdf:  <http://bio2rdf.org/ns/bio2rdf#>\n"+
			"PREFIX chebi:    <http://bio2rdf.org/ns/chebi#>\n"+
			"PREFIX purl:     <http://purl.org/dc/terms/>\n"+
			"SELECT ?drug ?transform ?mass WHERE \n" +
			"{\n"+
					"SERVICE <http://172.16.3.175:8890/sparql>\n" +
					"{\n"+
				   		"{ " +
				   			"?drug drugbank:affectedOrganism  'Humans and other mammals'.\n"+
				   			"?drug drugbank:casRegistryNumber ?cas .\n"+
					    "}\n" +
					    "OPTIONAL { ?drug drugbank:biotransformation ?transform . }\n"+
			      "}\n"+
			      "SERVICE <http://172.16.3.164:8890/sparql>\n" +
				   "{\n"+
				      "?keggDrug bio2rdf:xRef ?cas .\n"+
				      "?keggDrug bio2rdf:mass ?mass\n"+
				      "FILTER ( ?mass > '5.0' )\n" +
			      "}\n"+  
			"}" ;
	
public static String myQuery4Service =
			
			"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"+
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
			"PREFIX drugbank: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>\n"+
			"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>\n"+
			"PREFIX kegg:     <http://bio2rdf.org/ns/kegg#>\n"+
			"SELECT ?Drug ?IntDrug ?IntEffect WHERE \n" +
			"{\n"+
					"SERVICE <http://172.16.3.171:8890/sparql>\n" +
					"{\n"+
					   "?Drug rdf:type dbpedia-owl:Drug .\n"+
					
					"}\n"+   
					"SERVICE <http://172.16.3.175:8890/sparql>\n" +
					"{\n"+
					   "?drugbankdrug rdf:type drugbank:drugs .\n"+
//					   "?Int drugbank:interactionDrug1 ?drugbankdrug .\n"+
//					   "?Int drugbank:interactionDrug2 ?IntDrug .\n"+
//					   "?Int drugbank:text ?IntEffect .\n" +
						"?drugbankdrug owl:sameAs  ?Drug.\n"+
					   "?drugbankdrug drugbank:keggCompoundId ?cpd .\n"+
				   "}\n"+
				   "SERVICE <http://172.16.3.164:8890/sparql>\n" +
				   "{\n"+
					   "?enzyme kegg:xSubstrate ?cpd .\n"+
					   "?enzyme rdf:type kegg:Enzyme .\n"+
					   "?reaction kegg:xEnzyme ?enzyme .\n"+
					   "?reaction kegg:equation ?equation .\n" +
				   "}\n"+ 
			"}";


public static String myQuery5Service =

"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"+
"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
"PREFIX drugbank: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>\n"+
"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>\n"+
"PREFIX kegg:     <http://bio2rdf.org/ns/kegg#>\n"+
"PREFIX dc:       <http://purl.org/dc/elements/1.1/>"+
"PREFIX bio2rdf:  <http://bio2rdf.org/ns/bio2rdf#> "+
"SELECT ?Drug ?IntDrug ?IntEffect WHERE \n" +
"{\n"+

"SERVICE <http://172.16.3.178:8890/sparql>\n" +
"{\n"+		
		   "?chebiDrug bio2rdf:image ?chebiImage .\n"+
		   "?chebiDrug dc:title ?drugBankName .\n"+
"}\n"+
"SERVICE <http://172.16.3.175:8890/sparql>\n" +
"{\n"+
   "?drugbankdrug rdf:type drugbank:drugs .\n"+
   "?Int drugbank:interactionDrug1 ?drugbankdrug .\n"+
   "?Int drugbank:interactionDrug2 ?IntDrug .\n"+
   "?Int drugbank:text ?IntEffect .\n" +
   "?drugbankdrug drugbank:genericName ?drugBankName .\n"+
   "?drugbankdrug drugbank:keggCompoundId ?cpd .\n"+
"}\n"+
 "SERVICE <http://172.16.3.164:8890/sparql>\n" +
 "{\n"+
	   "?enzyme kegg:xSubstrate ?cpd .\n"+
	   "?enzyme rdf:type kegg:Enzyme .\n"+
	   "?reaction kegg:xEnzyme ?enzyme .\n"+
	   "?reaction kegg:equation ?equation .\n" +
 "}\n"+
 
		
	   
"}";


}
