package iut.software.federationengine.logging;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;


public class myLogger 
{

	public static long executionTime ;
	public static long firstTuple ;
	public static String queryString ;
	public static String queryId ;
	public static String systemName ;
	public static int allOfRoutedTuples ;
	public static int numberOfResultTuples ;
	public static ArrayList<OperatorStatistics> opStatisticList ;
	public static boolean waitForAllSubqueryExecs ; 
	public static String strRoutingStrategy ;
	public static int randomCeiling ;
	public static double delayFine ;
	public static long delayThreshold;
	public static int numberOfIntermediateResults ;
	public static boolean useRandomPriority ;
	public static int timeout ;
	public static boolean logging ;
	public static String folderToSave ;
	public static void printStatistics ()
	{
		System.out.println("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");
		System.out.println("First Tuple = "+ firstTuple );
		System.out.println("Execution Time= "+executionTime);
		System.out.println("Number of Results= "+numberOfResultTuples);
		System.out.println("Number of Intermediate Results= "+ numberOfIntermediateResults);
		System.out.println("Operator Statistics= "+opStatisticList.toString());
	}
	
	public static void saveStatistics()
	{
		String filePath = folderToSave + queryId+"-"+systemName+".csv";
		File statFile ;
		statFile =new File(filePath) ;
		if (statFile.exists())
		{
			System.out.println("Not null");
			writeStatsToFile (statFile) ;
		}
		else
		{
			statFile =new File(filePath) ;
			try 
			{
				FileUtils.writeStringToFile(statFile,"Query ID,"+queryId.toString()+"\n", false) ;
				FileUtils.writeStringToFile(statFile,"Query String,"+queryString+"\n", true) ;
				FileUtils.writeStringToFile(statFile,"Tested With,"+systemName+"\n\n\n\n\n", true) ;
				FileUtils.writeStringToFile(statFile,"Notice: All of times are in miliseconds\n\n", true) ;
				FileUtils.writeStringToFile(statFile,"Timeout:"+timeout+"min\n\n", true) ;
				FileUtils.writeStringToFile(statFile,"First Tuple Time,Execution Time,Number Of Results,Number Of Intermediate Results,All Of Routed Tuples", true) ;
				for (OperatorStatistics opStat :opStatisticList)
				{
					FileUtils.writeStringToFile(statFile,",Percent of tuples first routed to join "+opStat.getOperatorId()+"("+opStat.getJoinVar()+")", true) ;
				}
				FileUtils.writeStringToFile(statFile,",Routing strategy,Wait for all tuples to be buffered,Random ceiling,Delay fine,Delay threshold,Use random priority for tuples\n", true) ;
				writeStatsToFile(statFile);
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private static void writeStatsToFile (File fileToWriteOnIt) 
	{
		try 
		{
			FileUtils.writeStringToFile(fileToWriteOnIt,firstTuple+","+executionTime+","+numberOfResultTuples+","+numberOfIntermediateResults+","+allOfRoutedTuples, true) ;
			for (OperatorStatistics opStat :opStatisticList)
			{
				FileUtils.writeStringToFile(fileToWriteOnIt,","+opStat.getPercentOfTuplesRoutedToThisOp(), true) ;
			}
			FileUtils.writeStringToFile(fileToWriteOnIt,","+strRoutingStrategy+","+waitForAllSubqueryExecs+","+randomCeiling+","+delayFine+","+delayThreshold+","+useRandomPriority+"\n", true) ;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println("Statistics Writed To File.");
	}
	
	
	//For Other Systems
	public static void saveStatisticsForOtherSystems()
	{
		String filePath = folderToSave + queryId+"-"+systemName+".csv";
		File statFile ;
		statFile =new File(filePath) ;
		if (statFile.exists())
		{
			System.out.println("Not null");
			writeStatsToFileForOtherSystems(statFile) ;
		}
		else
		{
			statFile =new File(filePath) ;
			try 
			{
				FileUtils.writeStringToFile(statFile,"Query ID,"+queryId.toString()+"\n", false) ;
				FileUtils.writeStringToFile(statFile,"Query String,"+queryString+"\n", true) ;
				FileUtils.writeStringToFile(statFile,"Tested With,"+systemName+"\n\n\n\n\n", true) ;
				FileUtils.writeStringToFile(statFile,"Notice: All of times are in miliseconds\n\n", true) ;
				FileUtils.writeStringToFile(statFile,"Timeout:"+timeout+"min\n\n", true) ;
				FileUtils.writeStringToFile(statFile,"First Tuple Time,Execution Time,Number Of Results\n", true) ;
				writeStatsToFileForOtherSystems(statFile);
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private static void writeStatsToFileForOtherSystems (File fileToWriteOnIt) 
	{
		try 
		{
			FileUtils.writeStringToFile(fileToWriteOnIt,firstTuple+","+executionTime+","+numberOfResultTuples+"\n", true) ;		
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println("Statistics Writed To File.");
	}
	
	public static void printInfo (Object Info)
	{
		if (logging)
		{
			System.out.println("[INFO] "+ Info);
		}
	}
}
