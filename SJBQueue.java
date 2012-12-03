import java.util.Random;

public class SJBQueue extends PriorityQueue{
	
	public SJBQueue(){
	}
	//Amazingly Enough, Because of my set up with the CPU, the Shortest job first queue is 
	//not neccesary as a new class. The priority queue uses the scheduling information in the
	//PCB, which is set to the burst time if we are using SJB. So its the CPU Schedulers job to
	//apply the neccesary information for the SJB Queue to work.

	/**
	*Prints the Queue.
	*/
	@Override
	public void printQueue(){
		for(Link node = head; node != null; node = node.next){
			ProcessControlBlock pcb = node.getPCB();
			System.out.println("PID: " + pcb.getPID() + " State: " + pcb.getState() + " Actual CPU Time (ms): " + (System.currentTimeMillis() -pcb.getStartTime())  +  " Desired CPU Time (ms): " + pcb.getProcess().getBurst());
		}
	}

	public static void main(String[] args) {
		try{


			System.out.println("Unit Test for SJB Queue");

			SJBQueue sjb = new SJBQueue();

			Random gen = new Random();

			//Create some randomly timed Process's
			Process [] p = new Process[]{new Process(0).setBurst(gen.nextInt(1000)),
										 new Process(1).setBurst(gen.nextInt(1000)),
										 new Process(2).setBurst(gen.nextInt(1000))
										};

			//Create the PCBs
			ProcessControlBlock [] pcbs = new ProcessControlBlock[3];
			for(int i=0; i < 3; i++){
				pcbs[i] = new ProcessControlBlock(p[i]).setSchedule(p[i].getBurst());
				sjb.enQueue(pcbs[i]);
			}

			//Show that they are ordered correctly:
			sjb.printQueue();

			System.out.println("Unit Test Successful");

		}catch(Exception e){
			System.out.println("Unit Test on Queue Failed");
			for(StackTraceElement element : e.getStackTrace()){
				System.out.println("Trace: " + element.toString());
			}		
		}
	}

}