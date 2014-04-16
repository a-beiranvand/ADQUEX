package iut.software.federationengine.eddyexecuter;

import iut.software.federationegine.structures.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DeterministicRoutingStrategyTupleByTuple implements RoutingStrategy 

{
	
	ArrayList<JoinExecuterState> joinExecutersStates ;
	private int replanningPriod ;
	private int routedTuples ;
	private int[] routingTable ;
	Double[] operatorCosts ;
	/**
	 * For Random Routing
	 */
	private Random rndGenerator ; 
	//=========================Constructor============================ 
	public DeterministicRoutingStrategyTupleByTuple (List<JoinExecuter> joinExecutesList,int replanningPriod)
	{
		this.joinExecutersStates =new ArrayList<DeterministicRoutingStrategyTupleByTuple.JoinExecuterState>();
		for (JoinExecuter joinExec : joinExecutesList)
		{
			this.joinExecutersStates.add(new JoinExecuterState(joinExec));
		}
		this.replanningPriod =replanningPriod ;
		this.routedTuples =0 ;
		int allOfReadyBitStates = (int)Math.pow(2,this.joinExecutersStates.size());		
		this.routingTable =new int[allOfReadyBitStates];
		this.rndGenerator =new Random() ;
		this.operatorCosts =new Double[this.joinExecutersStates.size()] ;
	}
	
	
	@Override
	public int GetOperatorIdForTuple(Tuple tupleToRoute) 
	{
		
		
		if (this.routedTuples <=this.replanningPriod)
		{
			return RandomOperator (tupleToRoute) ;
		}
		//Masalan 1001 omin tuple omade pas agar replanning priod 1000 hast jadval route ro dobare tashkil bede va bad route kon tuple ro
		else if ((this.routedTuples % this.replanningPriod) == 1 )
		{
			ReOptimizeRoutingTable() ;
			return DeterministicRoute(tupleToRoute);
		}
		else
		{
			return DeterministicRoute(tupleToRoute);
		}
	}
	
	
	//==================================RANDOM ROUTING FUNCTION====================================
	private int RandomOperator(Tuple tupleToRoute)
	{
		int selectedJoinId = 0;
		int tupleReadyBitMap =tupleToRoute.getReadyBits() ;
		ArrayList<Integer> applicableJoinIds =new ArrayList<Integer>();
		int countOperators =this.joinExecutersStates.size() ;
		int joinIdMasker =0 ;
		for (int iJoinId=0 ; iJoinId <countOperators ;iJoinId++)
		{
			joinIdMasker = 1 << iJoinId ;
			if ((tupleReadyBitMap & joinIdMasker)!= 0)
			{
				applicableJoinIds.add(iJoinId);
			}
		}
		
		//Select a random join id
		int numOfJoinIds =applicableJoinIds.size() ;
		if (numOfJoinIds==1)
		{
			selectedJoinId= applicableJoinIds.get(0) ;
			
		}
		else
		{
			//morethanonejoiner++ ;
			int random =this.rndGenerator.nextInt(numOfJoinIds) ;
			//System.out.println("Selected Joiner ="+selectedJoiner);
			selectedJoinId=  applicableJoinIds.get(random);
		}
		//Update Operator statistics that we can update them from here
		this.joinExecutersStates.get(selectedJoinId).updateState(tupleToRoute);
		this.routedTuples++ ;
		return selectedJoinId;
	}
	
	
	//==========================Constructing routing table================================
	private void ReOptimizeRoutingTable()
	{
		System.out.println( "Replanning Stage = "+ routedTuples/replanningPriod);
		System.out.println( "¬¬¬¬¬¬¬ Previos Routing Table = "+Arrays.toString(routingTable));
		// productionconsumptionRatio = numberofoutputTuples / numberofinputTuples
		double pcRatio =0 ;
		// LeftRightRatio = numberofLeftEdgeTuples / numberofRightEdgeTuples or numberofRightEdgeTuples / numberofLeftEdgeTuples
		// The one that is greater will be chosen.
		double lrRatio =0;
		double opCost =0;
		//---------Calculating operator costs-------------
		for (int iJoinId=0;iJoinId<this.joinExecutersStates.size();iJoinId++)
		{
			System.out.println("******PC RATIO CALC********");
			
			if (this.joinExecutersStates.get(iJoinId).getJoinexecuter().getCountIncomeTuples()>0)
			{
				System.out.println("Join Id = "+iJoinId+ " Input = "+this.joinExecutersStates.get(iJoinId).getJoinexecuter().getCountIncomeTuples() +" Output= "+this.joinExecutersStates.get(iJoinId).getJoinexecuter().getCountOutcomeTuples());
				pcRatio =(double)this.joinExecutersStates.get(iJoinId).getJoinexecuter().getCountOutcomeTuples()/this.joinExecutersStates.get(iJoinId).getJoinexecuter().getCountIncomeTuples() ;
				System.out.println("PC RATIO= "+pcRatio);
			}
			else
			{
				pcRatio=0;
			}
			//
			if (this.joinExecutersStates.get(iJoinId).getCountLeftEdgeIncome()>this.joinExecutersStates.get(iJoinId).getCountRightEdgeIncome())
			{
				if (this.joinExecutersStates.get(iJoinId).getCountRightEdgeIncome()>0)
					lrRatio =(double)this.joinExecutersStates.get(iJoinId).getCountLeftEdgeIncome()/this.joinExecutersStates.get(iJoinId).getCountRightEdgeIncome() ;
				else
					lrRatio=this.joinExecutersStates.get(iJoinId).getCountLeftEdgeIncome();
			}
			else
			{
				if (this.joinExecutersStates.get(iJoinId).getCountLeftEdgeIncome()>0) 
					lrRatio =(double)this.joinExecutersStates.get(iJoinId).getCountRightEdgeIncome()/this.joinExecutersStates.get(iJoinId).getCountLeftEdgeIncome() ;
				else
					lrRatio =this.joinExecutersStates.get(iJoinId).getCountRightEdgeIncome();
			}
			opCost = pcRatio * lrRatio ;
			operatorCosts[iJoinId] = opCost;
		}
		//---------------------Routing Table Construction----------------
		int allOfReadyBitStates = (int)Math.pow(2,this.joinExecutersStates.size());
		//i as 1 shoro mishe chon yek ready bit hichvaght 0 nemishe
		for (int iReadyBit=1 ; iReadyBit < allOfReadyBitStates ;iReadyBit++)
		{
			double minOpCost = Double.MAX_VALUE ;
			int bestJoinId = 0 ;
			int masker = 0 ;
			for (int jJoinId=0 ; jJoinId < this.joinExecutersStates.size();jJoinId++)
			{
				masker= 1 << jJoinId ;
				if ((masker & iReadyBit) != 0)
				{
//					System.out.println("++++++++++Masker+++++++++++");
//					System.out.println("Masker = "+ masker+"Ireadybit= "+iReadyBit+ " And = "+ (masker&iReadyBit));
					if (operatorCosts[jJoinId]< minOpCost)
					{
						minOpCost = operatorCosts[jJoinId] ;
						bestJoinId = jJoinId;
					}
				}
			}
			//Fill a routing table ROW
			routingTable[iReadyBit] = bestJoinId ;
		}
		
		
		System.out.println("¬¬¬¬¬¬¬ Operator Costs = "+Arrays.toString(operatorCosts));
		System.out.println("¬¬¬¬¬¬¬ Routing Table Changed To = "+Arrays.toString(routingTable));
	}
	
	//==================================DETERMINISTIC ROUTING ======================================
	private int DeterministicRoute (Tuple tupleToRoute)
	{
		this.routedTuples++ ;
		this.joinExecutersStates.get(routingTable[tupleToRoute.getReadyBits()]).updateState(tupleToRoute);
		return routingTable[tupleToRoute.getReadyBits()] ;
	}
	
	
	//=================================PRINT ROUTING STATISTICS===================================
	@Override
	public void printRoutingStatistics() 
	{
		int countIntermediateResults = 0; 
		
		System.out.println("&&&&&&&&&&  OPERATOR COSTS= "+Arrays.toString(operatorCosts));
		
		for (JoinExecuterState joinExecStateItem : this.joinExecutersStates)
		{
			System.out.println("$$$$$$ join id "+ joinExecStateItem.getJoinexecuter().getJoinId()+" Input = "+joinExecStateItem.getJoinexecuter().getCountIncomeTuples()+" Output= " +joinExecStateItem.getJoinexecuter().getCountOutcomeTuples());
			System.out.println("%%%%%% join id "+ joinExecStateItem.getJoinexecuter().getJoinId()+"Left Edge Input = "+joinExecStateItem.getCountLeftEdgeIncome()+" Right Edge Input= " +joinExecStateItem.getCountRightEdgeIncome());
			/**
			 * Out put shamel result tuple ha va tuple haye intermediate ast
			 */
			countIntermediateResults+= joinExecStateItem.getJoinexecuter().getCountOutcomeTuples();
			
		}
		System.out.println("£££££££ Count Of Intermediate Results= "+countIntermediateResults);
		System.out.println("^^^^^^^ All Of Routed Tuples= "+ this.routedTuples);
	}
	
	
	/**
	 * In class ro baraye joda kardan kar routing az join executer ha sakhtam va hamchenin bara dastersi synchronized be ticketha
	 * @author Amin
	 *
	 */
	private class JoinExecuterState 
	{
		/**
		 * Farz Income va Outcome dar join Executer mojood ast
		 */
		private JoinExecuter joinexecuter ;
		private int countLeftEdgeIncome ;
		private int countRightEdgeIncome ;
		
		//Constructor
		public JoinExecuterState (JoinExecuter unticketedjoinexecuter)
		{
			this.joinexecuter=unticketedjoinexecuter ;
			this.countLeftEdgeIncome=0 ;
			this.countRightEdgeIncome= 0; 
			
		}

		//==============================Getters and Setters ====================================//
		public JoinExecuter getJoinexecuter() 
		{
			return joinexecuter;
		}

		public int getCountLeftEdgeIncome() 
		{
			return countLeftEdgeIncome;
		}

		public int getCountRightEdgeIncome() 
		{
			return countRightEdgeIncome;
		}

		//==============================End Getters and Setters=================================//
		
//		public void IncLeftEdgeIncome()
//		{
//			synchronized (this) 
//			{
//				this.countLeftEdgeIncome++ ;
//			}
//		}
//		
//		public void IncRightEdgeIncome()
//		{
//			synchronized (this) 
//			{
//				this.countRightEdgeIncome++ ;
//			}
//		}
		
		
		/**
		 * Vaziat amari ye operator ra update mikonda (tuple haye vorodi,.....)
		 * @param routedTuple
		 */
		public void updateState (Tuple routedTuple)
		{
			boolean isLeftEdgeTuple =false;
			for (String subQueryNameItem: routedTuple.getSubQueryList())
			{
				if (subQueryNameItem.equalsIgnoreCase(this.joinexecuter.getLeftSubQueryName()))
				{
					isLeftEdgeTuple=true ;
				}
			}
			if (isLeftEdgeTuple)
			{
				this.countLeftEdgeIncome++ ;
			}
			else
			{
				this.countRightEdgeIncome++ ;
			}
		}
	}

}
