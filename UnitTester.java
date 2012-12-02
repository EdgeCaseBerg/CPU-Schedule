public class UnitTester{
	public static void main(String[] args) {
		UnitTester ut = new UnitTester();
	}

	public UnitTester(){
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
		System.out.println("---------------------        ----------------------");
	}
}