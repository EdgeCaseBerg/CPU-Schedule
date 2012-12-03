
import java.util.HashMap;

public class Scheduler{
	//The scheduling algorithm that the Scheduler uses
	private Queue algorithm;
	public boolean preEmptive = false;
	//Max value defined as timeslice at first, redefined only for Round Robin
	public long timeSlice = Long.MAX_VALUE-1;

	public void removeJob(ProcessControlBlock pcb){
		algorithm.remove(pcb.getPID());
	}

	public boolean isPreEmptive(){
		return preEmptive;
	}

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
		scheduleWithPriority(pcb,pcb.getSchedule());
	}

	public void scheduleWithPriority(Process p, long priority){
		if(algorithm.getClass().getName().equals("PriorityQueue")){
			//Priority Queue
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(priority));
		}else if(algorithm.getClass().getName().equals("SJBQueue") || algorithm.getClass().getName().equals("RoundRobinQueue")){
			//Queue process with burst time as its priority
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(p.getBurst()));
		}else{
			//Round robin and normal Queue have no priorty but we'll set it anyway.
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(priority));
		}
	}

	public void scheduleWithPriority(ProcessControlBlock pcb, long priority){
		if(pcb.getState()==State.TERMINATED){
			algorithm.remove(pcb.getPID());
		}else{
			pcb = handleState(pcb);
			//The schedule in SJB is the time remaining and in priority queues is the pririty
			//When the CPU is done executing a job in SJB the priority passed in is the time elapsed in
			//the execution
			algorithm.enQueue(pcb.setSchedule(priority));
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

	public void setTimeSlice(long time){
		this.timeSlice = time;
	}


	public void printState(){
		System.out.println("Scheduler using " + algorithm.getClass().getName() + " Type Queue Structure");
		if(algorithm.getClass().getName().equals("RoundRobinQueue")){
			System.out.println("Time Quantum: " + timeSlice);
			System.out.println("Printing Queue:");
			algorithm.printQueue();
		}else{
			if(algorithm.getClass().getName().equals("PriorityQueue")){
				System.out.println("Pre-Emptive: " + preEmptive);
			}
			System.out.println("Printing Queue:");
			algorithm.printQueue();
		}

	}




}