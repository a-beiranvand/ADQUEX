package iut.software.federationengine.decomposer;

import java.util.ArrayList;
import org.openrdf.query.algebra.StatementPattern;

public class TriplePattern 
{
	private StatementPattern _statementPattern ;
	private String _askQueryString;
	private ArrayList<String> _RelatedEndpoints ;
	
	public TriplePattern ()
	{
		_RelatedEndpoints=new ArrayList<String>() ;
	}
	public TriplePattern (StatementPattern statementPtrn ,String askQueryStr)
	{
		_statementPattern = statementPtrn  ;
		_askQueryString =askQueryStr ;
		_RelatedEndpoints =new ArrayList<String>() ;
	}
	
	public StatementPattern getStatementPattern() {
		return _statementPattern;
	}
	public void setStatementPattern(StatementPattern statementPattern) {
		this._statementPattern = statementPattern;
	}
	public String getAskQueryString() {
		return _askQueryString;
	}
	public void setAskQueryString(String askQueryString) {
		this._askQueryString = askQueryString;
	}
	public ArrayList<String> getRelatedEndpoints() {
		return _RelatedEndpoints;
	}
	
	public void AddRelatedEnpoint (String EndpointAddress)
	{
		_RelatedEndpoints.add(EndpointAddress) ;
	}
	
}
