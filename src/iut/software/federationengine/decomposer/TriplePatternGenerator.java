package iut.software.federationengine.decomposer;

import java.net.URI;
import java.util.ArrayList;
import iut.software.federationegine.structures.OptimizerException;
import org.openrdf.query.algebra.StatementPattern;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;


public class TriplePatternGenerator extends QueryModelVisitorBase<OptimizerException>
{
	
	private ArrayList<TriplePattern> _Triples ;
	public TriplePatternGenerator()
	{
		super() ;
		_Triples =new ArrayList<TriplePattern>() ;
	}
	
	public ArrayList<TriplePattern> GenerateTriplePatternsAndTheirAsks (TupleExpr QueryTree)
	{
		QueryTree.visit(this) ;
		return _Triples;
	}
	
	@Override
	public void meet  (StatementPattern triple)
	{
		_Triples.add(new TriplePattern(triple, CreateAskQuery(triple))) ;
		super.meet(triple);
	}
	
	
//	private void SelectSource (StatementPattern triple)
//	{
//		String AskQueryString =CreateAskQuery(triple) ;
//		//AskExecuter askExec =new AskExecuter(AskQueryString, "http://dbpedia.org/sparql");
//		//Thread askExecThr =new Thread(askExec) ;
//		//askExecThr.start() ;
//	}
	
	private String CreateAskQuery (StatementPattern triple)
	{
		String queryString ="ASK { " ;
		if (triple.getSubjectVar().hasValue())
		{
			try
			{
				URI.create(triple.getSubjectVar().getValue().toString()) ;
				queryString+=" <"+triple.getSubjectVar().getValue()+">" ;
			}
			catch (IllegalArgumentException exp)
			{
				queryString+=" "+triple.getSubjectVar().getValue()+ " " ;
			}
		}
		else
		{
			queryString+=" ?"+triple.getSubjectVar().getName() ;
		}
		//
		if (triple.getPredicateVar().hasValue())
		{
			try
			{
				URI.create(triple.getPredicateVar().getValue().toString()) ;
				queryString+=" <"+triple.getPredicateVar().getValue()+">" ;
			}
			catch (IllegalArgumentException exp)
			{
				queryString+=" "+triple.getPredicateVar().getValue()+ " " ;
			}
		}
		else
		{
			queryString+=" ?"+triple.getPredicateVar().getName() ;
		}
		//
		if (triple.getObjectVar().hasValue())
		{
			try
			{
				URI.create(triple.getObjectVar().getValue().toString()) ;
				queryString+=" <"+triple.getObjectVar().getValue()+">" ;
			}
			catch (IllegalArgumentException exp)
			{
				queryString+=" "+triple.getObjectVar().getValue()+ " " ;
			}
		}
		else
		{
			queryString+=" ?"+triple.getObjectVar().getName() ;
		}
		//
		queryString+=" }";
		return queryString ;
	}
}
