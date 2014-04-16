package iut.software.federationegine.structures;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openrdf.query.algebra.evaluation.QueryBindingSet;
/**
 * 
 * @author Amin
 *
 * This is class for storing query results, each tuple has a priority this field is used for tuple routing
 */

public class Tuple implements Comparable<Tuple>{
	
	
	
	private QueryBindingSet bindingSet ;
	private int readyBits ;
	private int doneBits; 
	private ArrayList<String> subQueryList ;
	private int priority ;
	
	/**
	 * Constructs a tuple with Default PRIORITY and prepared  BindingSet,Bitmap,SubQueryNames. 
	 * @param bindingSet
	 * @param bitMap
	 * @param subQueryList
	 */
	public Tuple(QueryBindingSet bindingSet,int readyBits,int doneBits,ArrayList<String> subQueryList) 
	{
		this.bindingSet =bindingSet ;
		this.readyBits=readyBits ;
		this.doneBits=doneBits;
		this.subQueryList =subQueryList ;
		this.priority =1 ;
	}
	
	/**
	 * Construct a tuple with default priority. 
	 * 
	 */
	
	public Tuple() 
	{
		bindingSet =new QueryBindingSet() ;
		readyBits = 0; 
		doneBits= 0 ;
		subQueryList =new ArrayList<String>();
		this.priority =1 ;
	}
     
	/**
	 * Constructs a tuple with  prepared Priority,BindingSet,Bitmap,SubQueryNames. 
	 * @param bindingSet
	 * @param bitMap
	 * @param subQueryList
	 * @param priority
	 */
	public Tuple(QueryBindingSet bindingSet,int readyBits,int doneBits,ArrayList<String> subQueryList,int priority) 
	{
		this.bindingSet =bindingSet ;
		this.readyBits=readyBits ;
		this.doneBits=doneBits;
		this.subQueryList =subQueryList ;
		this.priority =priority ;
	}
	
	//========================Getters and Setters=============================

	public QueryBindingSet getBindingSet() {
		return bindingSet;
	}

	public void setBindingSet(QueryBindingSet bindingSet) {
		this.bindingSet = bindingSet;
	}

	public int getReadyBits() {
		return readyBits;
	}

	public void setReadyBits(int readyBits) {
		this.readyBits =readyBits ;
	}

	public int getDoneBits() {
		return doneBits;
	}

	public void setDoneBits(int doneBits) {
		this.doneBits =doneBits;
	}
	
	public ArrayList<String> getSubQueryList() {
		return subQueryList;
	}

	public void setSubQueryList(ArrayList<String> subQueryList) {
		this.subQueryList = subQueryList;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
//=====================================================================================
	
	/**
	 * This method gets a subQueryName and returns true if the tuple contains that name in it's subquery list .
	 * @param subQueryName
	 * @return
	 */
	public boolean HasSubQuery(String subQueryName)
	{
		return subQueryList.contains(subQueryName);
	}

	/**
	 * This method increases tuple priority by default value of 1 
	 */
	public void IncreasePriority ()
	{
		this.priority++ ;
	}
	
	/**
	 * This method increases tuple priority with this formula : priority+=value
	 * @param value
	 */
	public void IncreasePriority (int value)
	{
		this.priority+=value;
	}
	
	//Man ino jaye 1 , -1 ro taghir dadam ta tuple haye ba priority bozorgtar bala bioftan ghablan harchi priority kamtar (ro be manfi) bood balatar mioftad
	@Override
	public int compareTo(Tuple o) {
		if (this.priority>o.priority)
			return -1;
		else if (this.priority==o.priority)
			return 0 ;
		else
			return 1; 
	}
	
	/**
	 * if there is no need for work on this tuple and this tuple is a result tuple, this method returns true otherwise returns false
	 * @return
	 */
//	public boolean isaResultTuple ()
//	{
//		 Set<Map.Entry<Byte,JoinStatus>> bitmapEntrySet= bitMap.entrySet();
//		 for (Map.Entry<Byte, JoinStatus> bitmapEntry:bitmapEntrySet)
//		 {
//			 JoinStatus js = bitmapEntry.getValue() ;
//			 if ((js==JoinStatus.NotReady)||(js==JoinStatus.Ready))
//				 return false ;
//		 }
//		 //there is no ready or not ready in bitmap .
//		 return true ;
//	}
}
