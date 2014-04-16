package iut.software.federationengine.threadmanagement;

import iut.software.federationegine.structures.OptimizerException;
import iut.software.federationegine.structures.Tuple;

public class PoisonPill extends Tuple{
	
	public static final int subQueryExecuterPillType =1 ;
	public static final int joinExecuterPillType =2 ;
	public static final int eddyPillType =3 ;
	
	private ThreadStatus senderThreadStatus ;
	private int poisonPillType ; //valid values are 1(subQueryExecuterPillType),2(joinExecuterPillType),3(eddyPillType)
	private String senderThreadId ;
	
	//Constructor
	public PoisonPill(ThreadStatus senderThreadState,int poisonType,String senderThreadName)
	{
		setSenderThreadStatus(senderThreadState) ;
		setPoisonPillType(poisonType) ;
		setSenderThreadId(senderThreadName);
	}
	
	
	//----------------------Getters and Setters-----------------------------//
	/**
	 * 
	 * @return
	 */
	public ThreadStatus getSenderThreadStatus() 
	{
		return senderThreadStatus;
	}
	private void setSenderThreadStatus(ThreadStatus senderThreadStatus) 
	{
		this.senderThreadStatus = senderThreadStatus;
	}
	public int getPoisonPillType() 
	{
		return poisonPillType;
	}
	/**
	 * Current poison pill types are PoisonPill.subQueryExecuterPillType , PoisonPill.joinExecuterPillType , PoisonPill.eddyPillType
	 * If you send a invalid poison pill type to this method it throws an Optimizer exception.
	 * @param poisonPillType
	 */
	private void setPoisonPillType(int poisonPillType) 
	{
		if (poisonPillType <1 || poisonPillType >3 )
		{
			OptimizerException optmExp =new OptimizerException("You tried to set poison pill type to invalid value of "+poisonPillType+" . valid values are PoisonPill.subQueryExecuterPillType , PoisonPill.joinExecuterPillType , PoisonPill.eddyPillType") ;
			throw optmExp;
		}
		this.poisonPillType = poisonPillType;
	}
	public String getSenderThreadId() 
	{
		return senderThreadId;
	}
	private void setSenderThreadId(String senderThreadId) 
	{
		this.senderThreadId = senderThreadId;
	}
	
	

}
