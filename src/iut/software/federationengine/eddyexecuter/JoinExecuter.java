package iut.software.federationengine.eddyexecuter;

import iut.software.federationegine.structures.JoinStatus;
import iut.software.federationegine.structures.NewJoin;
import iut.software.federationegine.structures.OptimizerException;
import iut.software.federationegine.structures.Tuple;
import iut.software.federationengine.logging.myLogger;
import iut.software.federationengine.threadmanagement.PoisonPill;
import iut.software.federationengine.threadmanagement.SyncPoisonPill;
import iut.software.federationengine.threadmanagement.ThreadStatus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.FileUtils;
import org.openrdf.query.algebra.evaluation.QueryBindingSet;

public class JoinExecuter implements Runnable {

	private final BlockingQueue<Tuple> tupleHeap ;
	private final BlockingQueue<Tuple> workQueue ;
	private byte joinId ;
	private String leftSubQueryName ;
	private String joinVariable ;
	private HashMap<String,ArrayList<Tuple>> leftHashTable ;
	private HashMap<String,ArrayList<Tuple>> rightHashTable ;
	
	//For tuple priority
	private int randomCeiling ;
	
	//For Detecting a final Result Tuple
	private int numberOfJoinOperators;
	public void setNumberOfJoinOperators (int numberOfJoinOperators)
	{
		this.numberOfJoinOperators=numberOfJoinOperators;
	}
	
	/**
	 * For Returning Query Results
	 */
	private BlockingQueue<Tuple> resultTuples ;
	//For Scheduling Purposes
	private int countIncomeTuples ;
	private int countOutcomeTuples ;
	private int countIntermeidateTuplesGenerated ;
	//For thread management
	private ThreadStatus eddyThreadState ;
	private int countPoisonRequestId ;
	private boolean stateReportedToEddy ;
	//For Testing
	private int coutResultTuples ;
	public JoinExecuter(BlockingQueue<Tuple> tupleHeap,NewJoin joinOperator,int randomCeiling,BlockingQueue<Tuple> resultTuplesList) 
	{
		this.tupleHeap =tupleHeap;
		this.joinId=joinOperator.getJoinId() ;
		this.joinVariable=joinOperator.getJoinVariable();
		this.leftSubQueryName=joinOperator.getLeftSubQueryName() ;
		this.workQueue =new LinkedBlockingQueue<Tuple>();
		this.leftHashTable=new HashMap<String, ArrayList<Tuple>>();
		this.rightHashTable =new HashMap<String, ArrayList<Tuple>>();
		this.stateReportedToEddy=false;
		this.eddyThreadState=ThreadStatus.Normal;
		this.countPoisonRequestId=-1 ;
		this.coutResultTuples=0;
		this.countIncomeTuples= 0;
		this.countOutcomeTuples= 0;
		this.numberOfJoinOperators=0;
		this.randomCeiling= randomCeiling;
		this.resultTuples =resultTuplesList ;
		this.countIntermeidateTuplesGenerated=0 ;
	}
	
//	/**
//	 * Increase Tickets
//	 */
//	public void IncreaseTickets()
//	{
//		synchronized (this.tickets) 
//		{
//			this.tickets++ ;
//		}
//	}
//	
//	/**
//	 * Decrease Tickets
//	 */
//	public void DecreaseTickets()
//	{
//		synchronized (this.tickets) 
//		{
//			this.tickets-- ;
//			//System.out.println("Called Decrease!");
//		}
//	}
//	
	
	
	
	

	
	//Debug pakesh kon 
	private int countReport ;
	
	//============================Getters and Setters======================================
	public byte getJoinId() 
	{
		return joinId;
	}
	
	

	
	
	public int getCountIncomeTuples() 
	{
		return countIncomeTuples;
	}

	
	public int getCountOutcomeTuples() 
	{
		return countOutcomeTuples;
	}

/**
 * result Tuple ha dar nazar gerefte nemishavand. Tededad tuplehaye miani ke in join generate karde 
 * @return
 */
	public int getcountIntermeidateTuplesGenerated() 
	{
		return countIntermeidateTuplesGenerated;
	}


	public String getLeftSubQueryName()
	{
		return this.leftSubQueryName ;
	}
	
	public String getJoinVariable() 
	{
		return joinVariable;
	}	
	//=====================================================================================
	



	/**
	 * Adds a tuple to its work queue
	 * @param tupleToJoin
	 */
	public void JoinTuple (Tuple tupleToJoin)
	{
		try 
		{
			this.workQueue.put(tupleToJoin) ;
		}
		catch (InterruptedException e) 
		{
		   throw new OptimizerException(e) ;
		}
	}

	
	


	@Override
	public void run() 
	{
		StartWorking() ;
	}
	
	
	
	private void StartWorking()
	{
//		try {
//			Thread.sleep((this.joinId+1)*5000);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		while (true)
		{
			try 
			{
				
				//DEBUG
				//WriteWorkingTupleToFile (tupleToProcess);
				//System.out.println("There is "+workQueue.size()+" tuples in working quee of join "+joinId);
				
				//Eddy is terminated
				if (this.eddyThreadState==ThreadStatus.Termintaed)
					break;
				
				//Queue is empty report to eddy that you are ready to terminate if you have not reported yet. 
				if ((this.workQueue.isEmpty()) && !(this.stateReportedToEddy) && (this.eddyThreadState==ThreadStatus.ReadyToTerminate))
					ReportReadyToTerminate() ;
				
				Tuple tupleToProcess =workQueue.take() ;
				if (tupleToProcess instanceof SyncPoisonPill)
					ProcessPoison((SyncPoisonPill)tupleToProcess);
				else
					ProcessTuple(tupleToProcess);
				
			} 
			catch (InterruptedException e) 
			{
				throw new OptimizerException(e) ;
			}
			
		}
		myLogger.printInfo("Join" +Thread.currentThread().getName()+ " Finished.");
		//System.out.println("Join" +Thread.currentThread().getName()+ " Finished.");
		myLogger.printInfo("Count Results Tuples= "+this.coutResultTuples);
		//System.out.println("Count Results Tuples= "+this.coutResultTuples);
		myLogger.printInfo("Join" +Thread.currentThread().getName()+" "+ this.countReport+" times report to eddy");
		//System.out.println("Join" +Thread.currentThread().getName()+" "+ this.countReport+" times report to eddy" );
	}
	
	/**
	 * Report eddy i'm ready to terminate
	 */
	private void ReportReadyToTerminate()
	{
		SyncPoisonPill reportPoison =new SyncPoisonPill(ThreadStatus.ReadyToTerminate,PoisonPill.joinExecuterPillType,Thread.currentThread().getName(),this.countPoisonRequestId);
		try
		{
			this.tupleHeap.put(reportPoison) ;
		}
		catch (InterruptedException e)
		{
			OptimizerException optm =new OptimizerException(e) ;
			throw optm;
		}
		this.stateReportedToEddy=true ;
		//pakesh kon
		myLogger.printInfo("Join" +Thread.currentThread().getName()+" reports with request id " +this.countPoisonRequestId);
		//System.out.println("Join" +Thread.currentThread().getName()+" reports with request id " +this.countPoisonRequestId);
		this.countReport++ ;
	}
	
	/**
	 * Drinking poison!
	 * @param poison
	 */
	private void ProcessPoison (SyncPoisonPill poison)
	{
		if (poison.getSenderThreadStatus()==ThreadStatus.Termintaed)
		{
			this.eddyThreadState=ThreadStatus.Termintaed;
			return ;
		}
		if ((poison.getSenderThreadStatus()==ThreadStatus.ReadyToTerminate) && (poison.getRequestId()>this.countPoisonRequestId))
		{
			this.countPoisonRequestId=poison.getRequestId() ;
			this.stateReportedToEddy=false ;
			this.eddyThreadState=ThreadStatus.ReadyToTerminate;
		}
		
	}
	
	/**
	 * Consuming a tuple
	 * @param tupleToProcess
	 */
	private void ProcessTuple (Tuple tupleToProcess)
	{
		//Tedad tuple haye vorodi ro afzayesh bede
		this.countIncomeTuples++ ;
		if (tupleToProcess.HasSubQuery(this.leftSubQueryName))
		{
			InsertInLeftHash(tupleToProcess) ;
		}
		else
		{
			InsertInRightHash(tupleToProcess);
		}
	}
	//movaghat
	private void WriteWorkingTupleToFile (Tuple t)
	{
		File execHtml =new File("workingquee"+this.joinId+".html") ;
		try {
			FileUtils.writeStringToFile(execHtml,"<br /><br/><hr/><br/>",true) ;
			FileUtils.writeStringToFile(execHtml, "<font color='blue'>Binding Set="+t.getBindingSet()+"</font><br />",true) ;
			FileUtils.writeStringToFile(execHtml,"<br /><b>SubQuery Name List = "+ t.getSubQueryList()+"</b>",true ) ;
			FileUtils.writeStringToFile(execHtml, "<br/>SubQuery Bitmap= "+t.getReadyBits(),true);
			FileUtils.writeStringToFile(execHtml, "<br/>SubQuery Priorit= "+t.getPriority(),true);
			FileUtils.writeStringToFile(execHtml,"<br/><hr />" ,true);	
		}
		catch (IOException e) 
		{
			throw new OptimizerException(e) ;
		}
	}
	//************************************************************/
	
	//============================================Insert left hash table Probe Right Hash Table ============================//
	private void InsertInLeftHash(Tuple t)
	{
		String joinVarBinding =t.getBindingSet().getBinding(this.joinVariable).getValue().stringValue() ;
		if (joinVarBinding == null)
		{
			OptimizerException optm =new OptimizerException("There is no binding for "+this.joinVariable+" in tuple "+t.getSubQueryList()+" with bitmap: "+t.getReadyBits()) ;
			throw optm;
		}
		//Left Multimap insert
		ArrayList<Tuple> sameKeyTuples  ;
		sameKeyTuples = this.leftHashTable.get(joinVarBinding) ;
		if (sameKeyTuples == null)
		{
			//this is the first tuple with this key create a new list
			ArrayList<Tuple> newTupleList =new ArrayList<Tuple>() ;
			newTupleList.add(t);
			this.leftHashTable.put(joinVarBinding, newTupleList) ;
		}
		else
		{
			sameKeyTuples.add(t) ;
			this.leftHashTable.put(joinVarBinding, sameKeyTuples) ;
		}
		//End Left multimap insert
		
		//Right Multimap probe 
		ArrayList<Tuple> matchedTuples = rightHashTable.get(joinVarBinding) ;
		if (matchedTuples!=null)
		{
			for (Tuple matchedTupleItem : matchedTuples)
			{
				JoinTuples (matchedTupleItem,t) ;
			}
		}
		
	}
	
	//============================================Insert Right hash table Probe Left Hash Table ============================//
	private void InsertInRightHash(Tuple t)
	{
		String joinVarBinding =t.getBindingSet().getBinding(this.joinVariable).getValue().stringValue() ;
		if (joinVarBinding == null)
		{
			OptimizerException optm =new OptimizerException("There is no binding for "+this.joinVariable+" in tuple "+t.getSubQueryList()+" with bitmap: "+t.getReadyBits()) ;
			throw optm;
		}
		
		//Right Multimap insert
		ArrayList<Tuple> sameKeyTuples;
		sameKeyTuples = this.rightHashTable.get(joinVarBinding) ;
		if (sameKeyTuples == null)
		{
			//this is the first tuple with this key create a new list
			ArrayList<Tuple> newTupleList =new ArrayList<Tuple>() ;
			newTupleList.add(t);
			this.rightHashTable.put(joinVarBinding, newTupleList) ;
		}
		else
		{
			sameKeyTuples.add(t) ;
			this.rightHashTable.put(joinVarBinding, sameKeyTuples) ;
		}
		//End Left multimap insert
		//Left Multimap probe 
		ArrayList<Tuple> matchedTuples = this.leftHashTable.get(joinVarBinding) ;
		if (matchedTuples != null)
		{
			for (Tuple matchedTupleItem : matchedTuples)
			{
				JoinTuples (matchedTupleItem,t) ;
			}
		}
	}

	
	
	private void JoinTuples (Tuple tuple1 ,Tuple tuple2)
	{
		//increase count outcome tuples because this join produces a tuple
		this.countOutcomeTuples++ ;
		//Constructing a new bitmap for new integrated tuple
		//==========================NEW READY BITS=======================================
		int newReadyBits  ;
		int xorT1T2ReadyBits = tuple1.getReadyBits() ^ tuple2.getReadyBits() ;
		int andT1T2ReadyBits =tuple1.getReadyBits() & tuple2.getReadyBits() ;
		//Integration of 2 tuple ready bits
		newReadyBits =xorT1T2ReadyBits | andT1T2ReadyBits;
		//This tuple maybe ready for new operators
		int newAvailableReadyBits =0 ;
		ArrayList<Byte> joinsOnSameVar = Eddy.getInstance().getJoinVarTojoinMapper().get(this.joinVariable) ;
		int joinReadyBitSetter = 0;
		for (Byte joinId: joinsOnSameVar)
		{
			joinReadyBitSetter = (1 << joinId) ;
			newAvailableReadyBits = newAvailableReadyBits | joinReadyBitSetter ;
		}
		int xorAvailableAndIntegration = newReadyBits ^ newAvailableReadyBits ;
		int andAvailableAndIntegration = newReadyBits & newAvailableReadyBits ;
		newReadyBits = xorAvailableAndIntegration | andAvailableAndIntegration ;
		//Don't Forget this again //Set ready bit for the current join to 0
		//newBitMap.put(this.joinId, JoinStatus.Done);
		int readyBitMasker = 1 << this.joinId ;
		readyBitMasker = ~readyBitMasker;
		//Final Operation for Ready Bits 
		newReadyBits = newReadyBits & readyBitMasker;
		//====================================END NEW READY BITS=========================================
		//====================================NEW DONE BITS============================================
		int newDoneBits  ;
		int xorT1T2DoneBits = tuple1.getDoneBits() ^ tuple2.getDoneBits() ;
		int andT1T2DoneBits =tuple1.getDoneBits() & tuple2.getDoneBits() ;
		//Integration of 2 tuple Done bits
		newDoneBits =xorT1T2DoneBits | andT1T2DoneBits;
		
		//Setting done bit 1 for this join id 
		int doneBitSetter = 0 ;
		doneBitSetter = 1<< this.joinId ;
		//Final operation for done bits
		newDoneBits =newDoneBits | doneBitSetter ;
		//==================================END NEW DONE BITS=========================================
//		for (Byte joinId:tuple1.getBitMap().keySet())
//		{
//			JoinStatus newBitMapStatus =newBitMap.get(joinId) ;
//			if (newBitMapStatus==null)
//				newBitMapStatus=JoinStatus.NotReady ;
//			JoinStatus MaximumStatus =Maximum (tuple1.getBitMap().get(joinId),tuple2.getBitMap().get(joinId),newBitMapStatus) ;
//			newBitMap.put(joinId, MaximumStatus);
//		}
		
		//Constructing a new subquerynamelist for new integrated tuple
		//badan check kon ke duplicate insert nashe dar subquery list yek tuple .
		ArrayList<String> newSubQueryNameList =new ArrayList<String>();
		for (String subQueryNameItem :tuple1.getSubQueryList())
		{
			newSubQueryNameList.add(subQueryNameItem);
		}
		for (String subQueryNameItem :tuple2.getSubQueryList())
		{
			newSubQueryNameList.add(subQueryNameItem);
		}
		
		//Constructing a new binding set for new integrated tuple 
		QueryBindingSet newqbSet =new QueryBindingSet();
		newqbSet.addAll(tuple1.getBindingSet()) ;
		newqbSet.addAll(tuple2.getBindingSet()) ;

		//new tuple priority= randomCeiling+1 or +2,+3,....
		int newPriority =1 ;
		if (tuple1.getPriority()>=tuple2.getPriority())
			newPriority=tuple1.getPriority() ;
		else
			newPriority=tuple2.getPriority() ;
		if (newPriority>this.randomCeiling)
			newPriority++ ;
		else
			newPriority=this.randomCeiling+1; 
		//and finaly create a new tuple from tuple1 , tuple2
		
		Tuple finalNewTuple =new Tuple(newqbSet,newReadyBits,newDoneBits,newSubQueryNameList,newPriority);
		//DEBUG
		//putTupleToFile2(finalNewTuple) ;
		//=====================HoooraY one Result Tuple ==========================//
		if (finalNewTuple.getDoneBits() == (Math.pow(2, numberOfJoinOperators)-1))
		{
			try 
			{
				this.resultTuples.put(finalNewTuple);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			this.coutResultTuples++ ;
//			System.out.println("Join "+joinId+" Count Results Tuples= "+coutResultTuples);
			//System.out.println(finalNewTuple.getBindingSet().toString());
			//System.out.println("There is "+workQueue.size()+" tuples in working quee of join "+joinId);
			//DEBUG
			//putTupleToFile(finalNewTuple) ;
		}

		else
		{
			try 
			{
				tupleHeap.put(finalNewTuple) ;
				//Because this is not a final tuple
				this.countIntermeidateTuplesGenerated++ ;
				
			} catch (InterruptedException e) 
			{
				throw new OptimizerException(e) ;
			}
			
		}
			
	}
	
	
	
	//Felean natayej ro mostaghim az joinexecuter dar yek file mirizam badan bayad avaz she
	void putTupleToFile (Tuple t)
	{
		File execHtml =new File("results.html") ;
		try {
			FileUtils.writeStringToFile(execHtml,"<br /><br/><hr/><br/>",true) ;
			FileUtils.writeStringToFile(execHtml, "<font color='blue'>Binding Set="+t.getBindingSet()+"</font><br />",true) ;
			FileUtils.writeStringToFile(execHtml,"<br /><b>SubQuery Name List = "+ t.getSubQueryList()+"</b>",true ) ;
			FileUtils.writeStringToFile(execHtml, "<br/>SubQuery Bitmap= "+t.getReadyBits(),true);
			FileUtils.writeStringToFile(execHtml, "<br/>SubQuery Priorit= "+t.getPriority(),true);
			FileUtils.writeStringToFile(execHtml,"<br/><hr />" ,true);	
		}
		catch (IOException e) 
		{
			throw new OptimizerException(e) ;
		}
			
	}
	
	//
	void putTupleToFile2 (Tuple t)
	{
		File execHtml =new File("Joins"+this.joinId+".html") ;
		try {
			FileUtils.writeStringToFile(execHtml,"<br /><br/><hr/><br/>",true) ;
			FileUtils.writeStringToFile(execHtml, "<font color='blue'>Binding Set="+t.getBindingSet()+"</font><br />",true) ;
			FileUtils.writeStringToFile(execHtml,"<br /><b>SubQuery Name List = "+ t.getSubQueryList()+"</b>",true ) ;
			FileUtils.writeStringToFile(execHtml, "<br/>SubQuery Bitmap= "+t.getReadyBits(),true);
			FileUtils.writeStringToFile(execHtml, "<br/>SubQuery Priorit= "+t.getPriority(),true);
			FileUtils.writeStringToFile(execHtml,"<br/><hr />" ,true);	
		}
		catch (IOException e) 
		{
			throw new OptimizerException(e) ;
		}
			
	}
	
}
