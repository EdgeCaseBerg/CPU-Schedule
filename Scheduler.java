

public class Scheduler{
	//The scheduling algorithm that the Scheduler uses
	private Queue algorithm;
	public boolean preEmptive = false;
	//Max value defined as timeslice at first, redefined only for Round Robin
	public long timeSlice = Long.MAX_VALUE;

	public ProcessControlBlock handleState(ProcessControlBlock pcb){
		if(pcb.getState()==State.TERMINATED){
			//The process is gone, remove it from the queue.
			algorithm.remove(pcb.getPID());
			return null;
		}else if(pcb.getState()!=State.WAITING){
			//If the state is waiting then we have to wait on IO
			//So yes its going back in, but it wont change until 
			//The IO or event its waiting oncompletes
			pcb = pcb.changeStateTo(State.HALTED);
		}
		return pcb;
	}

	public void schedule(Process p){
		if(algorithm.getClass().getName().equals("PriorityQueue") || algorithm.getClass().getName().equals("SJBQueue")){
			//If its priorty or SJB then we send it along, since no priority is given, 
			//the burst time is used for both types of algorithm (my own decision)
			scheduleWithPriority(p,p.getBurst());
		}else{
			//This is the only way a process can be scheduled with a NEW State
			algorithm.enQueue(new ProcessControlBlock(p));
		}
	}

	public void schedule(ProcessControlBlock pcb){
		//When a pcb comes back into the Scheduler we need to update it's state:
		pcb = handleState(pcb);
		if(pcb==null){
			//PCB was in a terminated state, removed from Queue now
			return;
		}
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
			//Round robin and normal Queue have no priorty but we'll set it anyway.
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(priority));
		}
	}

	public ProcessControlBlock nextJob(){
		ProcessControlBlock pcb = algorithm.deQueue();
		if(pcb == null){
			return null;
		}
		//Change the state of the process to Running if its not waiting on IO
		pcb = pcb.getState() != State.WAITING ? pcb.changeStateTo(State.RUNNING) : pcb;
		return pcb;
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

	public void allocateSpaceForNextJob(){
		ProcessControlBlock pcb = algorithm.deQueue();
		if(pcb != null){
			//Magic happens and we allocate space for the next job
			pcb.changeStateTo(State.READY);
		}
	}






}