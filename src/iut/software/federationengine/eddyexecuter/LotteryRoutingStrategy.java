package iut.software.federationengine.eddyexecuter;
import iut.software.federationegine.structures.Tuple;
import iut.software.federationengine.logging.OperatorStatistics;
import iut.software.federationengine.logging.myLogger;

import java.util.ArrayList;
import java.util.List;


public class LotteryRoutingStrategy implements RoutingStrategy 

{
	
	ArrayList<TicketedJoinExecuters> joinExecutersWithTickets ;
	private int routedTuples ;
	
	//Constructor 
	public LotteryRoutingStrategy (List<JoinExecuter> joinExecutesList)
	{
		this.joinExecutersWithTickets =new ArrayList<LotteryRoutingStrategy.TicketedJoinExecuters>() ;
		for (JoinExecuter joinExec : joinExecutesList)
		{
			this.joinExecutersWithTickets.add(new TicketedJoinExecuters(joinExec)) ;
		}
	}
	
	
	@Override
	public int GetOperatorIdForTuple(Tuple tupleToRoute) 
	{
		this.routedTuples++;
		int readyBitMap =tupleToRoute.getReadyBits();
		int leastSelectivejoinId =0; 
		int maxTicket =Integer.MIN_VALUE ;
		int ticketTemp ;
		int masker = 0;
		for (int iJoinId =0 ; iJoinId < this.joinExecutersWithTickets.size() ; iJoinId++)
		{
			masker = 1 << iJoinId;
			if ((masker & readyBitMap) != 0)
			{
				ticketTemp = this.joinExecutersWithTickets.get(iJoinId).getTickets() ;
				if (ticketTemp>maxTicket)
				{
					maxTicket=ticketTemp ;
					//System.out.println("Max Ticket = "+ maxTicket+" Join id = "+joinId);
					leastSelectivejoinId=iJoinId;
				}
			}
		}
		
		//For Logging
		if (tupleToRoute.getDoneBits()==0)
			this.joinExecutersWithTickets.get(leastSelectivejoinId).numberOfTuplesFirstRoutedToMe++ ;
		//Return
		return leastSelectivejoinId;
	}
	
	
	
	@Override
	public void printRoutingStatistics() 
	{
		int countIntermediateResults = 0;
		//For Logging
		myLogger.opStatisticList =new ArrayList<OperatorStatistics>() ;
		for (TicketedJoinExecuters ticketedJEitem : joinExecutersWithTickets)
		{
			System.out.println("$$$$$$ join id "+ ticketedJEitem.getJoinexecuter().getJoinId()+" Input = "+ticketedJEitem.getCountInputTuples()+" Output= " +ticketedJEitem.getcountOutPutTuples());
			/**
			 * faghat tuple haye gheir final ro jozv in amar hesab kardam
			 */
			countIntermediateResults+= ticketedJEitem.getJoinexecuter().getcountIntermeidateTuplesGenerated();
			//For logging
			double percentFirstRoutedTuplesToSomeOp ;
			percentFirstRoutedTuplesToSomeOp = (double)ticketedJEitem.numberOfTuplesFirstRoutedToMe/(double)routedTuples ;
			percentFirstRoutedTuplesToSomeOp =percentFirstRoutedTuplesToSomeOp *100 ;
			OperatorStatistics opStats =new OperatorStatistics(ticketedJEitem.getJoinexecuter().getJoinId(), ticketedJEitem.getJoinexecuter().getJoinVariable(),percentFirstRoutedTuplesToSomeOp);
			myLogger.opStatisticList.add(opStats);
		}
		System.out.println("£££££££ Count Of Intermediate Results= "+countIntermediateResults);
		//For Logging
		myLogger.numberOfIntermediateResults=countIntermediateResults ;
		myLogger.allOfRoutedTuples =routedTuples ;
	}
	
	
	/**
	 * In class ro baraye joda kardan kar routing az join executer ha sakhtam va hamchenin bara dastersi synchronized be ticketha
	 * @author Amin
	 *
	 */
	private class TicketedJoinExecuters 
	{
		private JoinExecuter joinexecuter ;
		private int numberOfTuplesFirstRoutedToMe ;
		//Constructor
		public TicketedJoinExecuters (JoinExecuter unticketedjoinexecuter)
		{
			this.joinexecuter=unticketedjoinexecuter ;
			this.numberOfTuplesFirstRoutedToMe=0;
		}

		//==============================Getters and Setters ====================================//
		public JoinExecuter getJoinexecuter() 
		{
			return joinexecuter;
		}
		
		public int getTickets() 
		{
			int tickets = this.joinexecuter.getCountIncomeTuples()- this.joinexecuter.getCountOutcomeTuples() ;
			return tickets ;
		}
		
		public int getCountInputTuples()
		{
			return this.joinexecuter.getCountIncomeTuples();
		}
		
		public int getcountOutPutTuples()
		{
			return this.joinexecuter.getCountOutcomeTuples();
		}
		//==============================End Getters and Setters=================================//

	}

}
