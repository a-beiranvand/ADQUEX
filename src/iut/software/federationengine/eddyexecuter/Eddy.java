package iut.software.federationengine.eddyexecuter;


import iut.software.federationegine.structures.NewJoin;
import iut.software.federationegine.structures.OptimizerException;
import iut.software.federationegine.structures.SubQuery;
import iut.software.federationegine.structures.Tuple;
import iut.software.federationengine.logging.myLogger;
import iut.software.federationengine.threadmanagement.PoisonPill;
import iut.software.federationengine.threadmanagement.SyncPoisonPill;
import iut.software.federationengine.threadmanagement.ThreadExecuter;
import iut.software.federationengine.threadmanagement.ThreadStatus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;


import org.apache.commons.io.FileUtils;
import org.openrdf.query.algebra.Join;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;

public class Eddy implements Runnable{
	
	private static Eddy instance ;
	private HashMap<String,ArrayList<Byte>> joinVarTojoinMapper ;
	private ArrayList<SubQuery> subQueries ;
	private BlockingQueue<Tuple> TupleHeap ;
	/**
	 * List of runnable objects not threads
	 */
	private List<JoinExecuter> joinExecuters ;
	/**
	 * List of runnable objects not threads
	 */
	private List<SubQueryExecuter> subQueryExecuters ;
	private TupleExpr queryTupleExpr ;
	private ThreadExecuter eddyThreadExecuter ;
	
	/**
	 * For sending poison and thread management
	 */
	private ThreadStatus eddyThreadState ;
	private int poisonRequestIdCounter ;

	/**
	 * For Returning Query Results
	 */
	private BlockingQueue<Tuple> resultTupleSet ;
	
	/**
	 * For routing Strategy
	 */
	private RoutingStrategy routingStrategy ;
	
	
	/**
	 * Changing Execution Strategies.
	 */
	private String StrRoutingStrategy ;
	private boolean waitForAllSubqueryExecs ;
	private Integer countFinishedSQexecuters=0 ;
	private int randomCeiling ;
	private double delayFine ; 
	private long delayThreshold ;
	private boolean useRandomPriority ;
	//=================Test==============
	public synchronized boolean incFinishedSqExecuters(boolean Eddy)
	{
		if (countFinishedSQexecuters==subQueryExecuters.size())
			return true ;
		else if (!Eddy)
		{
			countFinishedSQexecuters++ ;
			return false;
		}
		return false;
	}
	
	
	@Override
	public void run() 
	{
		Execute();
	}
	
	
	private Eddy (BlockingQueue<Tuple> resultTupleSet)
	{
		//Private Constructor 
		this.resultTupleSet=resultTupleSet;
		this.TupleHeap =new PriorityBlockingQueue<Tuple>(10000) ;
		this.eddyThreadExecuter =new ThreadExecuter();
		this.eddyThreadState=ThreadStatus.Normal;
		//this.waitForAllSubqueryExecs=true ;
		this.waitForAllSubqueryExecs=false; 
		this.StrRoutingStrategy="Deterministic";
		//this.StrRoutingStrategy="Lottery" ;
		this.randomCeiling =100;
		//this.randomCeiling =0;
		this.useRandomPriority=true;
		//this.useRandomPriority=false;
		//--------
		this.delayFine=0.1 ;
		this.delayThreshold=50;
//		this.delayFine=0 ;
//		this.delayThreshold=0;
		
		//For Logging
		myLogger.strRoutingStrategy=StrRoutingStrategy ;
		myLogger.randomCeiling =randomCeiling ;
		myLogger.delayFine =delayFine ;
		myLogger.delayThreshold=delayThreshold ;
		myLogger.waitForAllSubqueryExecs=waitForAllSubqueryExecs ;
		myLogger.useRandomPriority=useRandomPriority;
		
	}
	
	/**
	 * Synchronized
	 * @throws OptimizerException
	 */
	public static synchronized void Initialize(BlockingQueue<Tuple> resultTupleSet) throws OptimizerException
	{
		if (instance != null)
		{
			OptimizerException e =new OptimizerException("Error in Eddy.initialize(): eddy double iniatialization error!") ;
			throw e ;
		}
		instance = new Eddy(resultTupleSet) ;
		
	}
	
	/**
	 * Synchronized because it can be called from multiple threads.
	 * @return
	 * @throws OptimizerException
	 */
	public static synchronized Eddy getInstance () throws OptimizerException
	{
		if (instance !=null)
		{
			return instance ;
		}
		else
		{
			OptimizerException e =new OptimizerException("Error! Eddy is not initialized!") ;
			throw e ;
		}
	}

	public HashMap<String,ArrayList<Byte>> getJoinVarTojoinMapper() 
	{
		return joinVarTojoinMapper;
	}

	public void setJoinVarTojoinMapper(HashMap<String,ArrayList<Byte>> joinVarTojoinMapper) 
	{
		this.joinVarTojoinMapper = joinVarTojoinMapper;
	}

	public ArrayList<SubQuery> getSubQueries() 
	{
		return subQueries;
	}

	public void setSubQueries(ArrayList<SubQuery> subQueries) 
	{
		this.subQueries = subQueries;
	}
	
	public void setQueryTupleExpr(TupleExpr queryTupleExpr) 
	{
		this.queryTupleExpr = queryTupleExpr;
	}
	
	public void Execute ()
	{
		
		//Generate subquery executers and start them with a thread executer
		this.subQueryExecuters=new ArrayList<SubQueryExecuter>();
		for (SubQuery sq : this.subQueries)
		{
			SubQueryExecuter sqExec = new SubQueryExecuter(sq, TupleHeap,sq.getSubQueryName(),this.randomCeiling,this.useRandomPriority);
			this.subQueryExecuters.add(sqExec);
			eddyThreadExecuter.execute(sqExec);
		}
		
		//Generate join Executers and start them with a thread executer
		JoinExecuterGenerator joinExecGen =new JoinExecuterGenerator() ;
		this.joinExecuters = joinExecGen.GenerateJoinExecuters(this.queryTupleExpr,this.TupleHeap) ;
		//Let a join executer know the number of all join operators for detecting a final result tuple
		for (JoinExecuter joinExecItem :this.joinExecuters)
		{
			joinExecItem.setNumberOfJoinOperators(this.joinExecuters.size());
		}
		this.eddyThreadExecuter.executeList(joinExecuters) ;
		
		//===========================DIFFERENT EXECUTIONS================================
		//--------Construct Routing Strategy------------------
		if (this.StrRoutingStrategy.equalsIgnoreCase("Deterministic"))
			this.routingStrategy =new DeterministicRoutingStrategy(joinExecuters,subQueryExecuters,this.delayFine,this.delayThreshold ,1000) ;
		else if (this.StrRoutingStrategy.equalsIgnoreCase("Lottery"))
			this.routingStrategy=new LotteryRoutingStrategy(joinExecuters) ;
		else
		{
			throw new OptimizerException("This Routing Strategy is not supported!") ;
		}
		
		long startTime =0;
		if (this.waitForAllSubqueryExecs)
		{
			while (true)
			{
				if (incFinishedSqExecuters(true))
					break ;
			}
			startTime= System.currentTimeMillis() ;
			myLogger.printInfo("All Subqueries Finished!");
			//System.out.println("All Subqueries Finished!");	
		}
		
		//========================END DIFFERENT EXECUTIONS================================
		
		//Consume Tuples and route them to  joiners.
		int debugi =0  ; //niaz nist faghat bar ye test gozashtam ;
		while (true)
		{
			debugi++ ;
			try 
			{
				synchronized (this.TupleHeap)
				{
					if (this.TupleHeap.isEmpty() &&  this.eddyThreadExecuter.isAllSQExecsAreTerminated())
					//if (this.eddyThreadExecuter.isAllSQExecsAreTerminated())
						break ;
				}
				Tuple tupleToConsume =TupleHeap.take() ;
				//if tuple is a poison process it. else route it to a joinExecuter based on a routing strategy.
				if (tupleToConsume instanceof PoisonPill)
				{
					ProcessPoison((PoisonPill)tupleToConsume);
				}
				else
				{
					ProcessTuple(tupleToConsume);
				}
				//WriteTupleToFile(tupleToConsume) ;
			}
			catch (InterruptedException e) 
			{
				OptimizerException optm =new OptimizerException(e) ;
				throw optm;
			}
			//System.out.println("Routing" + debugi);
		}
		myLogger.printInfo("Eddy exits on iteration "+debugi);
		//System.out.println("Eddy exits on iteration "+debugi);
		
		while (true)
		{
			debugi++;
			try
			{
//				synchronized (this.TupleHeap)
//				{
					if (this.TupleHeap.isEmpty() &&  this.eddyThreadExecuter.isAllJExecsAreReadyToTerminate())
						break ;
					else if ((this.TupleHeap.isEmpty()) && (this.eddyThreadState==ThreadStatus.Normal))
						ReportReadyToTerminateState() ;
				//}
				Tuple tupleToConsume =TupleHeap.take() ;
				if (tupleToConsume instanceof SyncPoisonPill)
					ProcessPoison((SyncPoisonPill)tupleToConsume);
				else if (tupleToConsume instanceof PoisonPill)
					ProcessPoison((PoisonPill)tupleToConsume);
				else
				{
					if (this.eddyThreadState==ThreadStatus.ReadyToTerminate)
						ExpireReadyState () ;
					ProcessTuple(tupleToConsume);
				}
				
			}
			catch (InterruptedException e) 
			{
				OptimizerException optm =new OptimizerException(e) ;
				throw optm;
			}
			
		}
		
		ReportTerminateState() ;
		//==============================TEST ==================
		if (this.waitForAllSubqueryExecs)
		{
			long endTime =System.currentTimeMillis() ;
			myLogger.printInfo("Execution time without Endpoint Delays=" + (endTime-startTime));
			//System.out.println("Execution time without Endpoint Delays=" + (endTime-startTime));
			
		}
		myLogger.printInfo("Eddy exits on iteration(2) "+debugi);
		//System.out.println("Eddy exits on iteration(2) "+debugi);
		myLogger.printInfo("Eddy Fully Terminated!") ;
		//System.out.println("Eddy Fully Terminated!");
		this.routingStrategy.printRoutingStatistics();
		
		
	}
	
	
	/**
	 * Eddy can only process  subQuery poison and joinExecuterPoison if a poison of other type was in tuple heap throw an exception. i think this never happens. 
	 * @param t
	 */
	private void ProcessPoison (PoisonPill poisonFeed)
	{
		if (poisonFeed.getPoisonPillType()==PoisonPill.subQueryExecuterPillType)
		{
			this.eddyThreadExecuter.incCountTerminatedSubQueryExecuters() ;
		}
		else if (poisonFeed.getPoisonPillType()==PoisonPill.joinExecuterPillType)
		{
			SyncPoisonPill syncPoisonFeed= (SyncPoisonPill)poisonFeed;
			if ((syncPoisonFeed.getRequestId()==this.poisonRequestIdCounter)&& (poisonFeed.getSenderThreadStatus()==ThreadStatus.ReadyToTerminate))
				this.eddyThreadExecuter.incCountReadyToTerminateJoinExecuters() ;
		}
		else
		{
			OptimizerException optm =new OptimizerException("Error in processing poison. Eddy can't process a poison of type "+poisonFeed.getPoisonPillType()+ "Eddy can Eddy can only process subQuery poison and joinExecuterPoison") ;
			throw optm;
		}
	}
	
	/**
	 * This method routes a tuple to a operator
	 * @param t
	 */
	private void ProcessTuple(Tuple tupleToProcess) 
	{
		int joinIdToRoute= this.routingStrategy.GetOperatorIdForTuple(tupleToProcess) ;
		this.joinExecuters.get(joinIdToRoute).JoinTuple(tupleToProcess);
	}
	
	/**
	 * Sends ready to terminate poison to all join executers.
	 */
	private void ReportReadyToTerminateState() 
	{
		this.eddyThreadState=ThreadStatus.ReadyToTerminate ;
		SyncPoisonPill eddyReadyPoison =new SyncPoisonPill(this.eddyThreadState, PoisonPill.eddyPillType,Thread.currentThread().getName(),this.poisonRequestIdCounter) ;
		for (JoinExecuter jExecuter : this.joinExecuters)
		{
			jExecuter.JoinTuple(eddyReadyPoison);
			
		}
		//Send End of Results to Result Set
		try
		{
			this.resultTupleSet.put(eddyReadyPoison);
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		//System.out.println("Eddy Poison sended!");
	}
	
	/**
	 * ba afzayesh dadan poisonRequestIdCounter mige ke man dige amade terminate nistam va kol poison haee ke ba request id ghabl mian dige motabar nistan
	 */
	private void ExpireReadyState()
	{
		this.eddyThreadState=ThreadStatus.Normal ;
		this.poisonRequestIdCounter++ ;
		this.eddyThreadExecuter.resetCountReadyToTerminateJoinExecuters();
		//System.out.println("Expire Ready State");
	}
	
	/**
	 * Sends terminate poison to all join executers.
	 */
	private void ReportTerminateState() 
	{
		this.eddyThreadState=ThreadStatus.Termintaed ;
		SyncPoisonPill eddyReadyPoison =new SyncPoisonPill(this.eddyThreadState, PoisonPill.eddyPillType,Thread.currentThread().getName(),this.poisonRequestIdCounter) ;
		for (JoinExecuter jExecuter : this.joinExecuters)
		{
			jExecuter.JoinTuple(eddyReadyPoison);
		}
	}


	
	
	/**
	 * A private class that maybe i make it a public class in future
	 * @author Amin
	 *
	 */
	private class JoinExecuterGenerator extends QueryModelVisitorBase<OptimizerException>
	{
		
		private ArrayList<JoinExecuter> generatedJoinExecuters ;
		public JoinExecuterGenerator() {
			generatedJoinExecuters=new ArrayList<JoinExecuter>();
		}
		public ArrayList<JoinExecuter> GenerateJoinExecuters (TupleExpr queryTupleExpr,BlockingQueue<Tuple> tupleHeap)
		{
			queryTupleExpr.visit(this) ;
			return generatedJoinExecuters ;
		}
		
		@Override
		public void meet(Join node) throws OptimizerException {
			if (node instanceof NewJoin)
			{
				NewJoin newjoinItem =(NewJoin)node ;
				JoinExecuter joinExec=new JoinExecuter(TupleHeap, newjoinItem,randomCeiling,resultTupleSet) ;
				generatedJoinExecuters.add(joinExec) ;
			}
			super.meet(node);
		}
		
	}
	
	
	//
	private void WriteTupleToFile(Tuple t)
	{
		File execHtml =new File("EddyTuples.html") ;
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
