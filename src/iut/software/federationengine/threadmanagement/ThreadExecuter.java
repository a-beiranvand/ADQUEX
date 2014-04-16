package iut.software.federationengine.threadmanagement;

import iut.software.federationegine.structures.OptimizerException;
import iut.software.federationengine.eddyexecuter.JoinExecuter;
import iut.software.federationengine.eddyexecuter.SubQueryExecuter;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class ThreadExecuter implements Executor
{
	
	private List<Thread> joinExecuterThreads ;
	private List<Thread> subQueryExecuterThreads ;
	private List<Thread> otherThreads ;
	private int countTerminatedSubQueryExecuters ;
	private int countReadyToTerminateJoinExecuters ;
	private boolean allSQExecsAreTerminated ;
	private boolean allJExecsAreReadyToTerminate ;
	
	//Constructor
	public ThreadExecuter()
	{
		this.joinExecuterThreads=new ArrayList<Thread>();
		this.subQueryExecuterThreads=new ArrayList<Thread>() ;
		this.otherThreads =new ArrayList<Thread>() ;
		this.countReadyToTerminateJoinExecuters =0 ;
		this.countTerminatedSubQueryExecuters =0 ;
		this.allJExecsAreReadyToTerminate =false ;
		this.allSQExecsAreTerminated = false ;
	}
	
	
	//------------------------------Getters and Setters -------------------------------------
	
	public List<Thread> getJoinExecuterThreads() 
	{
		return joinExecuterThreads;
	}


	public List<Thread> getSubQueryExecuterThreads() 
	{
		return subQueryExecuterThreads;
	}


	public List<Thread> getOtherThreads() 
	{
		return otherThreads;
	}


	public boolean isAllSQExecsAreTerminated() 
	{
		return allSQExecsAreTerminated;
	}


	public boolean isAllJExecsAreReadyToTerminate() 
	{
		return allJExecsAreReadyToTerminate;
	}
	
	//-----------------------------------------------------------------------------------
	
	/**
	 * This method takes a runnable object and makes a thread for that object and eventually starts the thread .
	 */
	@Override
	public void execute(Runnable command) 
	{
		if (command instanceof JoinExecuter)
		{
			JoinExecuter jeCommand =(JoinExecuter)command ;
			Thread newJoinExecThread =new Thread(command, "joinID= "+jeCommand.getJoinId());
			this.joinExecuterThreads.add(newJoinExecThread) ;
			newJoinExecThread.start() ;
		}
		else if (command instanceof SubQueryExecuter)
		{
			SubQueryExecuter sqCommand =(SubQueryExecuter)command  ;
			Thread newSubQueryExecThread =new Thread(command,sqCommand.getSubQueryName());
			this.subQueryExecuterThreads.add(newSubQueryExecThread) ;
			newSubQueryExecThread.start();
		}
		else
		{
			//Simply create a thread
		}
	}
	
	/**
	 * This method gets a list of runnable commands and create threads for that commands and starts the threads. it can handle a mixed list of Join Executer Runnables and Sub Query Executers.
	 * @param commands
	 */
	public void executeList (List<? extends Runnable> commands)
	{
		for (Runnable command : commands)
		{
			execute(command) ;
		}
	}
 
	/**
	 * This method increments count terminated subquery executers by one. if count terminated subquery executers becomes more than number of subquery executer threads this method throws an exception.
	 */
	public void incCountTerminatedSubQueryExecuters()
	{
		
		int numberOfSQExecThreads =this.subQueryExecuterThreads.size() ;
		if ((this.countTerminatedSubQueryExecuters+1)>numberOfSQExecThreads)
		{
			OptimizerException optm =new  OptimizerException("Error! count terminated subquery executers can't be more than number of sub query executer threads. yek thread(sub query executer) vaziate taghir nakardash ro 2 bar gozaresh dade.");
			throw optm ;
		}
		//Increment
		this.countTerminatedSubQueryExecuters++ ;
		//Set all subquery executer terminated to true if all of threads has terminated
		if (this.countTerminatedSubQueryExecuters==numberOfSQExecThreads)
		{
			this.allSQExecsAreTerminated= true ;
		}
		
	}

	/**
	 * This method decrements count terminated subquery executers by one. if count terminated subquery executers becomes negative this method throws an exception.
	 */
	public void decCountTerminatedSubQueryExecuters()
	{
		if ((this.countTerminatedSubQueryExecuters-1)<0)
		{
			OptimizerException optm =new  OptimizerException("Error! count terminated subquery executers can't be negative. yek thread(sub query executer) vaziate taghir nakardash ro 2 bar gozaresh dade.");
			throw optm ;
		}
		//Decrement
		this.countTerminatedSubQueryExecuters-- ;
		this.allSQExecsAreTerminated=false ;
	}
	
	/**
	 * This method increments count ready to terminate join executers by one. if count ready to terminate join executers becomes more than number of join executer threads this method throws an exception.
	 */
	public void incCountReadyToTerminateJoinExecuters()
	{
		
		int numberOfJExecThreads =this.joinExecuterThreads.size() ;
		if ((this.countReadyToTerminateJoinExecuters+1)>numberOfJExecThreads)
		{
			OptimizerException optm =new  OptimizerException("Error! count terminated Join executers can't be more than number of Join executer threads. yek thread(join executer) vaziate taghir nakardash ro 2 bar gozaresh dade.");
			throw optm ;
		}
		//Increment
		this.countReadyToTerminateJoinExecuters++ ;
		//Set all subquery executer terminated to true if all of threads has terminated
		if (this.countReadyToTerminateJoinExecuters==numberOfJExecThreads)
		{
			this.allJExecsAreReadyToTerminate= true ;
		}
		
	}

	/**
	 * This method decrements count ready to terminate join executers by one. if count terminated subquery executers becomes negative this method throws an exception.
	 */
	public void decCountReadyToTerminateJoinExecuters()
	{
		if ((this.countReadyToTerminateJoinExecuters-1)<0)
		{
			OptimizerException optm =new  OptimizerException("Error! count Ready to terminate join executers can't be negative. yek thread(join executer) vaziate taghir nakardash ro 2 bar gozaresh dade.");
			throw optm ;
		}
		//Decrement
		this.countReadyToTerminateJoinExecuters-- ;
		this.allJExecsAreReadyToTerminate=false ;
	}
	
	/**
	 * This method sets count terminated joinexecuters to zero.
	 */
	public void resetCountReadyToTerminateJoinExecuters ()
	{
		this.countReadyToTerminateJoinExecuters= 0;
	}
}
