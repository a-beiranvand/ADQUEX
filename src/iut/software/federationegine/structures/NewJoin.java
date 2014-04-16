package iut.software.federationegine.structures;

import org.openrdf.query.algebra.Join;
import org.openrdf.query.algebra.QueryModelNode;
import org.openrdf.query.algebra.TupleExpr;

/***
 * 
 * @author Amin
 * This is a new join node that i defined it. i defined this because i want to have join variables for each join.
 */
public class NewJoin  extends Join implements QueryModelNode,TupleExpr{
	
	private String joinVariable  ;
	private byte joinId ;
	private String leftSubQueryName ;



	public NewJoin(TupleExpr leftArg,TupleExpr rightArg,String joinVar,byte joinID,String leftSubQueryName) {
		super(leftArg,rightArg) ;
		this.joinVariable=joinVar ;
		this.joinId=joinID;
		this.leftSubQueryName =leftSubQueryName;
	}
	
	public String getJoinVariable()
	{
		return this.joinVariable ;
	}
	
	public void setJoinVariable (String joinVar)
	{
		this.joinVariable =joinVar ;
	}
	
	public byte getJoinId() {
		return joinId;
	}

	public void setJoinId(byte joinId) {
		this.joinId = joinId;
	}
	
	public String getLeftSubQueryName() {
		return leftSubQueryName;
	}

	public void setLeftSubQueryName(String leftSubQueryName) {
		this.leftSubQueryName = leftSubQueryName;
	}
	
	//This is just for printing
	@Override
	public String getSignature() {
		String nodeString= super.getSignature() ;
		nodeString += "  Join Variable = "+this.getJoinVariable()+ " Join Id = "+ this.getJoinId()+" Left SubQuery = "+this.getLeftSubQueryName() ;
		return nodeString;
	}

}
