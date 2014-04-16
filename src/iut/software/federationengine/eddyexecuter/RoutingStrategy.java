package iut.software.federationengine.eddyexecuter;

import iut.software.federationegine.structures.Tuple;

public interface RoutingStrategy {
	
	/**
	 * This method return operator id for a tuple based on a routing strategy
	 * @param tupleToRoute
	 */
	public int GetOperatorIdForTuple(Tuple tupleToRoute) ;
	
	public void printRoutingStatistics();
	

}
