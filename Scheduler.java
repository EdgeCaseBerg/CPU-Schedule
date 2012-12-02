

public class Scheduler{
	//The scheduling algorithm that the Scheduler uses
	private Queue algorithm;
	public boolean preEmptive = false;
	//Max value defined as timeslice at first, redefined only for Round Robin
	public long timeSlice = Long.MAX_VALUE;

	public void schedule(Process p){
		algorithm.enQueue(new ProcessControlBlock(p));
	}

	public Scheduler(){
		//Default Constructor uses FIFO Queue
		algorithm = new Queue();
	}

	public Scheduler(Queue algorithm){
		this.algorithm = algorithm;
	}

	public Scheduler(boolean preEmptive){
		//It only makes sense to have priority queue if preEmptive 
		algorithm = preEmptive ? new PriorityQueue() : new Queue();
		this.preEmptive = preEmptive;
	}

	


}