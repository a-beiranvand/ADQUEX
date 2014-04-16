package iut.software.federationengine.parser;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.parser.ParsedQuery;
import org.openrdf.query.parser.QueryParser;
import org.openrdf.query.parser.sparql.SPARQLParser;

public class Parser {
	
	//Returns query tree (TupleExpr) if query is syntactically valid otherwise throws an exception.
	public static TupleExpr parseQueryString (String queryString) throws MalformedQueryException
	{
		QueryParser sparqlParser =new SPARQLParser() ;
		try {
			ParsedQuery parsed =sparqlParser.parseQuery(queryString, null) ;
			return parsed.getTupleExpr() ;
		} catch (MalformedQueryException e) {
			throw e ;
		}
		
	}

}
