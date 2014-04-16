package iut.software.federationegine.structures;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class SubQuery {
	
	private String subQueryName ;
	private String queryString ;
	private URI endpointAdress ;
	private ArrayList<String> joinVars  ;
	
	//[join id =0:[0],join id =1:[1],join id =2: [2]]
	private int readyBitPattern ;

	
	public SubQuery() {
		// TODO Auto-generated constructor stub
	}


	public String getQueryString() {
		return queryString;
	}


	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}


	public URI getEndpointAdress() {
		return endpointAdress;
	}


	public void setEndpointAdress(String endpointAdress)  throws URISyntaxException{

			this.endpointAdress =new  URI(endpointAdress) ;
	}


	public ArrayList<String> getJoinVars() {
		return joinVars;
	}


	public void setJoinVars(ArrayList<String> joinVars) {
		this.joinVars = joinVars;
	}


	public int getReadyBitPattern() {
		return readyBitPattern;
	}


	public void setReadyBitPattern(int readyBitPattern) {
		this.readyBitPattern = readyBitPattern;
	}
	
	
	public String getSubQueryName() {
		return subQueryName;
	}

	public void setSubQueryName(String subQueryName) {
		this.subQueryName = subQueryName;
	}



	
	
}
