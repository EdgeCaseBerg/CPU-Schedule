public class UnitTester{
	public static void main(String[] args) {
		UnitTester ut = new UnitTester();
	}

	public UnitTester(){
		System.out.println("Ethan Eldridge OS CPU Scheduling Simulation");
		System.out.println("Unit Testing OS Structures and CPU");
		//Testing Queue
		try{
			Queue.main(new String[]{""});
		}catch(Exception e){
			System.out.println("Unit Test on Queue Failed");
			for(StackTraceElement element : e.getStackTrace()){
				System.out.println("Trace: " + element.toString());
			}		
		}
		//Testing Priority Queue
		try{
			PriorityQueue.main(new String[]{""});	
		}catch(Exception e){
			System.out.println("Unit Test on Queue Failed");
			for(StackTraceElement element : e.getStackTrace()){
				System.out.println("Trace: " + element.toString());
			}		
		}
	}
}