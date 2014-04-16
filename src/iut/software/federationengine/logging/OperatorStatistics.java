package iut.software.federationengine.logging;

public class OperatorStatistics
{
	private Byte operatorId ;
	private String joinVar ;
	private double percentOfTuplesRoutedToThisOp ;
	
	public OperatorStatistics(Byte opId, String joinVar , double percentOfTuplesRoutedTo) 
	{
		this.operatorId =opId ;
		this.joinVar =joinVar ;
		this.percentOfTuplesRoutedToThisOp =percentOfTuplesRoutedTo ;
	}
	public Byte getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Byte operatorId) {
		this.operatorId = operatorId;
	}
	public String getJoinVar() {
		return joinVar;
	}
	public void setJoinVar(String joinVar) {
		this.joinVar = joinVar;
	}
	public double getPercentOfTuplesRoutedToThisOp() {
		return percentOfTuplesRoutedToThisOp;
	}
	public void setPercentOfTuplesRoutedToThisOp(
			double percentOfTuplesRoutedToThisOp) {
		this.percentOfTuplesRoutedToThisOp = percentOfTuplesRoutedToThisOp;
	}
	
	@Override
	public String toString() 
	{
		return "[ join" +operatorId+" ("+joinVar+")" + "Percent of First Routed Tuples= "+percentOfTuplesRoutedToThisOp+" ]";		
	}
	
}
