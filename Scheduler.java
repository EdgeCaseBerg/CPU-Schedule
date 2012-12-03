

public class Scheduler{
	//The scheduling algorithm that the Scheduler uses
	private Queue algorithm;
	public boolean preEmptive = false;
	//Max value defined as timeslice at first, redefined only for Round Robin
	public long timeSlice = Long.MAX_VALUE;

	public void schedule(Process p){
		algorithm.enQueue(new ProcessControlBlock(p));
	}

	public void schedule(ProcessControlBlock pcb){
		algorithm.enQueue(pcb);
	}

	public void scheduleWithPriority(Process p, long priority){
		if(algorithm.getClass().getName().equals("PriorityQueue")){
			//Priority Queue
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(priority));
		}else if(algorithm.getClass().getName().equals("SJBQueue")){
			//Queue process with burst time as its priority
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(p.getBurst()));
		}else{
			//Round robin and normal Queue have no priorty
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(priority));
		}
	}

	public Scheduler(){
		//Default Constructor uses FIFO Queue
		algorithm = new Queue();
	}

	public Scheduler(Queue algorithm){
		this.algorithm = algorithm;
	}

	/**
	*Creates a default FIFO Scheduler if preEmptive is false, otherwise creates a priority queue
	*/
	public Scheduler(boolean preEmptive){
		//It only makes sense to have priority queue if preEmptive 
		algorithm = preEmptive ? new PriorityQueue() : new Queue();
		this.preEmptive = preEmptive;
	}






}