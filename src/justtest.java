import java.nio.Buffer;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;

import iut.software.federationegine.structures.Tuple;


public class justtest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
		Tuple t =new Tuple();
//		t.getSubQueryList().add('A') ;
//		t.getSubQueryList().add('B') ;
//		if (t.HasSubQuery('A'))
//			System.out.println("True");
		t.IncreasePriority(4);
		t.setPriority(3);
		System.out.println(t.getPriority());
		
		Tuple t1 =new Tuple() ;
		
		Tuple t2 =new Tuple() ;
		t2.IncreasePriority() ;
		
	}

}
