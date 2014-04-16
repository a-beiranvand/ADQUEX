package iut.software.federationengine.threadmanagement;


/**
 * Az in class baraye rad o badal payam bein eddy va operatorha (threadhashoon) estefade mishe. farghesh ine ke yek request id dare
 * @author Amin
 *
 */
public class SyncPoisonPill extends PoisonPill 
{
	
	private int requestId ;
	
	public SyncPoisonPill (ThreadStatus senderThreadState,int poisonType,String senderThreadName,int requestId)
	{
		super(senderThreadState,poisonType,senderThreadName);
		this.requestId=requestId;
	}

	public int getRequestId() 
	{
		return requestId;
	}
	
	

}
