
import java.io.*;

public class UnitTester{
	public static void main(String[] args) {
		boolean redirect = true;
		if(args.length > 0){
			redirect = false;
		}
		UnitTester ut = new UnitTester(redirect);
	}

	public UnitTester(boolean redirect){
		if(redirect){
		try{
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
		}catch(Exception e){}}
		System.out.println("Ethan Eldridge OS CPU Scheduling Simulation");
		System.out.println("Unit Testing OS Structures and CPU");
		System.out.println("---------------------NEW TEST----------------------");
		Queue.main(new String[]{""});
		System.out.println("---------------------NEW TEST----------------------");
		PriorityQueue.main(new String[]{""});	
		System.out.println("---------------------NEW TEST----------------------");
		SJBQueue.main(new String[]{""});	
		System.out.println("---------------------NEW TEST----------------------");
		RoundRobinQueue.main(new String[]{""});
		System.out.println("---------------------NEW TEST----------------------");
		Scheduler.main(new String[]{""});
		System.out.println("---------------------        ----------------------");
		System.out.println("No Unit tests on ProcessControlBlock or Process classes");
		System.out.println("---------------------        ----------------------");
		System.out.println("Running CPU w/ default Queue");
		CPU.main(new String[]{""});
		System.out.println("Unit Tests Complete...");

	}
}