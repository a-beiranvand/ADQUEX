package iut.software.federationengine.decomposer;

public class QueryStringsStandard 
{

	public static String cd2 =
			
			"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>PREFIX owl:<http://www.w3.org/2002/07/owl#>prefix nytimes: <http://data.nytimes.com/elements/>prefix dc:<http://purl.org/dc/terms/>prefix lmdb: <http://data.linkedmdb.org/resource/movie/>prefix dbpedia: <http://dbpedia.org/resource/>" +
			" SELECT * WHERE\n"+ 	
			"{\n"+ 		
					"dbpedia:Barack_Obama dbpedia-owl:party ?party .\n"+
					"?x nytimes:topicPage ?page .\n"		+
					"?x owl:sameAs dbpedia:Barack_Obama .\n"+
			"}"		;


public static String cd3 =
		"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>PREFIX owl:<http://www.w3.org/2002/07/owl#>prefix nytimes: <http://data.nytimes.com/elements/>prefix dc:<http://purl.org/dc/terms/>prefix lmdb: <http://data.linkedmdb.org/resource/movie/>prefix dbpedia: <http://dbpedia.org/resource/>prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
		"SELECT * WHERE \n"+ 	
		 "{\n" 	+
				"?pres rdf:type dbpedia-owl:President . \n"+		
				"?pres dbpedia-owl:nationality dbpedia:United_States . \n"+		
				"?pres dbpedia-owl:party ?party . \n"		+
				"?x nytimes:topicPage ?page . \n"		+
				"?x owl:sameAs ?pres. \n" 		+
		"}"		;

public static String cd4 =
		
		"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>PREFIX owl:<http://www.w3.org/2002/07/owl#>prefix nytimes: <http://data.nytimes.com/elements/>prefix dc:<http://purl.org/dc/terms/>prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>prefix dbpedia: <http://dbpedia.org/resource/>prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
		"SELECT * WHERE\n"+ 	
		 "{\n"+ 
				"?film dc:title 'Tarzan' .\n" +
				"?film linkedMDB:actor ?actor .\n" +
				"?actor owl:sameAs ?x.\n" +
				"?y owl:sameAs ?x .\n" +
				"?y nytimes:topicPage ?news.\n"+ 
		"}"		;
public static String cd5 =
		"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>PREFIX owl:<http://www.w3.org/2002/07/owl#>prefix nytimes: <http://data.nytimes.com/elements/>prefix dc:<http://purl.org/dc/terms/>prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>prefix dbpedia: <http://dbpedia.org/resource/>prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
		"SELECT * WHERE\n"+ 	
		 "{ \n" +
			 	"?film dbpedia-owl:director ?director.\n" +
			 	"?director dbpedia-owl:nationality dbpedia:Italy .\n" +
			 	"?x owl:sameAs ?film .\n" +
			 	"?x linkedMDB:genre ?genre.\n" +
		 "}"	;
public static String cd6 =
		"PREFIX geonames:    <http://www.geonames.org/ontology#>\n"+
		"PREFIX foaf:        <http://xmlns.com/foaf/0.1/>\n"+
		"SELECT ?name ?location  WHERE \n" +
		"{\n"+
				"?artist foaf:name ?name .\n"+
				"?artist foaf:based_near ?location .\n"+
				"?location geonames:parentFeature ?germany .\n"+
				"?germany geonames:name 'Federal Republic of Germany'\n"+
		"}";

public static String cd7Service =
		"PREFIX owl:         <http://www.w3.org/2002/07/owl#>\n"+
		"PREFIX nytimes:     <http://data.nytimes.com/elements/>\n"+
		"PREFIX geonames:    <http://www.geonames.org/ontology#>\n"+
		"SELECT ?location ?news WHERE\n"+ 
		"{\n"+ 
				"?location geonames:parentFeature ?parent .\n"+
				"?parent geonames:name 'California'  .\n"+
				"?y owl:sameAs ?location.\n"+
				"?y nytimes:topicPage ?news\n"+
		"}";


public static String myquery1 =
		"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>"+
				"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
				"prefix nytimes: <http://data.nytimes.com/elements/>" +
				"prefix dc:<http://purl.org/dc/terms/>" +
				"prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>"+
				"prefix dbpedia: <http://dbpedia.org/resource/>" +
				"prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"SELECT * WHERE \n " +
				"{ \n" +
					"?dbfilm a dbpedia-owl:Film.\n"+
					"?dbfilm dbpedia-owl:director ?director.\n" +
					"?imdbfilm owl:sameAs ?dbfilm.\n" +
					"?imdbfilm dc:title ?title.\n"+
			  		"?concept owl:sameAs ?director.\n" +
			  		"?concept nytimes:topicPage ?directorpage.\n"+
		  "}\n" ;


public static String myquery2 =
		"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>"+
				"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
				"prefix nytimes: <http://data.nytimes.com/elements/>" +
				"prefix dc:<http://purl.org/dc/terms/>" +
				"prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>"+
				"prefix dbpedia: <http://dbpedia.org/resource/>" +
				"prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"SELECT * WHERE \n " +
				"{ \n" +
					"?dbfilm a dbpedia-owl:Film.\n"+
					"?dbfilm dbpedia-owl:budget ?budget .\n" +
					"?dbfilm dbpedia-owl:director ?director.\n" +
					"?film a linkedMDB:film .\n" +
					"?film owl:sameAs ?dbfilm." +
					"?film linkedMDB:actor ?actor.\n" +
					"?ydirector owl:sameAs ?director."+
					"?ydirector nytimes:topicPage ?newspage."+
		  "}\n" ;



public static String myquery3 =
		"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>"+
				"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
				"prefix nytimes: <http://data.nytimes.com/elements/>" +
				"prefix dc:<http://purl.org/dc/terms/>" +
				"prefix linkedMDB: <http://data.linkedmdb.org/resource/movie/>"+
				"prefix dbpedia: <http://dbpedia.org/resource/>" +
				"prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"SELECT * WHERE \n " +
				"{ \n" +				
					"?dbfilm a dbpedia-owl:Film.\n"+
					"?dbfilm dbpedia-owl:budget ?budget .\n" +
					"?dbfilm dbpedia-owl:director ?director.\n" +
					"?film a linkedMDB:film .\n" +
					"?film owl:sameAs ?dbfilm." +
					"?film linkedMDB:genre <http://data.linkedmdb.org/resource/film_genre/4>." +
					"?film linkedMDB:actor ?actor.\n" +
					"?ydirector owl:sameAs ?director."+
					"?ydirector nytimes:topicPage ?newspage."+
		  "}\n" ;


public static String ls3Service =
		
		"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"+
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
		"PREFIX drugbank: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>\n"+
		"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>\n"+
		"SELECT ?Drug ?IntDrug ?IntEffect WHERE \n" +
		"{\n"+
				"SERVICE <http://192.168.1.2:8890/sparql>\n" +
				"{\n"+
				   "?Drug rdf:type dbpedia-owl:Drug .\n"+
				"}\n"+   
				"SERVICE <http://192.168.1.3:8890/sparql>\n" +
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
			"SERVICE <http://192.168.1.3:8890/sparql>\n" +
			"{\n"+
			   "?drug drugbank:drugCategory drug-cat:cathartics .\n"+
			   "?drug drugbank:keggCompoundId ?cpd .\n"+
			   "?drug drugbank:description ?drugDesc .\n" +
		   "}\n"+
		   "SERVICE <http://192.168.1.4:8890/sparql>\n" +
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
	"SERVICE <http://192.168.1.3:8890/sparql>\n" +
	"{\n"+
	   "?drug rdf:type drugbank:drugs .\n"+
	   "?drug drugbank:keggCompoundId ?keggDrug .\n"+
	   "?drug drugbank:genericName ?drugBankName .\n" +
   "}"+
   "SERVICE <http://192.168.1.4:8890/sparql>\n" +
   "{\n"+
	   "?keggDrug bio2rdf:url ?keggUrl .\n" +
   "}\n"+
   "SERVICE <http://192.168.1.2:8890/sparql>\n" +
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
		"SERVICE <http://192.168.1.3:8890/sparql>\n" +
		"{\n"+
			"?drug drugbank:drugCategory drug-cat:micronutrient .\n"+
			"?drug drugbank:casRegistryNumber ?id .\n" +
		"}\n"+
		"SERVICE <http://192.168.1.4:8890/sparql>\n" +
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
		"SERVICE <http://192.168.1.3:8890/sparql>\n" +
		"{\n"+
	   		"{ " +
	   			"?drug drugbank:affectedOrganism  'Humans and other mammals'.\n"+
	   			"?drug drugbank:casRegistryNumber ?cas .\n"+
		    "}\n" +
		    "OPTIONAL { ?drug drugbank:biotransformation ?transform . }\n"+
      "}\n"+
      "SERVICE <http://192.168.1.4:8890/sparql>\n" +
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
				"SERVICE <http://192.168.1.2:8890/sparql>\n" +
				"{\n"+
				   "?Drug rdf:type dbpedia-owl:Drug .\n"+
				   "?Drug owl:sameAs  ?drugbankdrug.\n"+
				"}\n"+   
				"SERVICE <http://192.168.1.3:8890/sparql>\n" +
				"{\n"+
				   "?drugbankdrug rdf:type drugbank:drugs .\n"+
//				   "?Int drugbank:interactionDrug1 ?drugbankdrug .\n"+
//				   "?Int drugbank:interactionDrug2 ?IntDrug .\n"+
//				   "?Int drugbank:text ?IntEffect .\n" +
				   "?drugbankdrug drugbank:keggCompoundId ?cpd .\n"+
			   "}\n"+
			   "SERVICE <http://192.168.1.4:8890/sparql>\n" +
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

"SERVICE <http://192.168.1.2:8890/sparql>\n" +
"{\n"+		
	   "?chebiDrug bio2rdf:image ?chebiImage .\n"+
	   "?chebiDrug dc:title ?drugBankName .\n"+
"}\n"+
"SERVICE <http://192.168.1.3:8890/sparql>\n" +
"{\n"+
"?drugbankdrug rdf:type drugbank:drugs .\n"+
"?Int drugbank:interactionDrug1 ?drugbankdrug .\n"+
"?Int drugbank:interactionDrug2 ?IntDrug .\n"+
"?Int drugbank:text ?IntEffect .\n" +
"?drugbankdrug drugbank:genericName ?drugBankName .\n"+
"?drugbankdrug drugbank:keggCompoundId ?cpd .\n"+
"}\n"+
"SERVICE <http://192.168.1.4:8890/sparql>\n" +
"{\n"+
   "?enzyme kegg:xSubstrate ?cpd .\n"+
   "?enzyme rdf:type kegg:Enzyme .\n"+
   "?reaction kegg:xEnzyme ?enzyme .\n"+
   "?reaction kegg:equation ?equation .\n" +
"}\n"+

	
   
"}";

}
