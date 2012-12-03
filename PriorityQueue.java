

public class PriorityQueue extends Queue{
	
	public PriorityQueue(){
	}
	
	/**
	*Adds the ProcessControlBlock to the Queue as a new Head if its priorty is lower.
	*@param pcb The new head for the Queue.
	*/
	@Override
	protected void setHead( ProcessControlBlock pcb ){
		if(head == null){
			head = new Link(pcb);
			tail = head;
		}else{
			enQueue(pcb);
		}
	}

	/**
	*Adds a ProcessControlBlock to the Queue with lowest numbers as higher priorities
	*@param adds ProcessControlBlock pcb to the Quque. FIFO style.
	*/
	@Override
	public void enQueue(ProcessControlBlock pcb){
		if(head == null){
			setHead(pcb);
		}else{
			Link node;
			Link previous = null;
			boolean added = false;
			for(node = head; node != null && !added; node = node.next){
				if(pcb.getSchedule() < node.getPCB().getSchedule() ){
					if(previous == null){
						//Adding a new head!
						Link newLink = new Link(pcb,head);	
						//Check for single item in list to update the tail
						if(head.next == null){
							tail = newLink;
						}
						head = newLink;
						added = true;
						return;
					}else{
						//Adding a regular item
						System.out.println(node);
						previous.next = new Link(pcb,node);
						added = true;
						return;
					}
				}
				previous = node;
			}
			//Keep track of the tail. 
			tail = previous == null ? head : previous;
			//Techinically I shouldn't have to do a boolean check becuase if I added it then I returned (refactor later)
			if(!added){
				//The priority is the worst
				if(tail == null){
					tail = new Link(pcb);
					previous.next = tail;
				}else{
					tail.next = new Link(pcb);
					tail = tail.next;
				}
			}
		}
	}

	/**
	*Prints the Queue.
	*/
	public void printQueue(){
		for(Link node = head; node != null; node = node.next){
			ProcessControlBlock pcb = node.getPCB();
			System.out.println("PID: " + pcb.getPID() + " State: " + pcb.getState() + " Actual CPU Time (ms): " + (System.currentTimeMillis() -pcb.getStartTime())  +  " Desired CPU Time (ms): " + pcb.getProcess().getBurst() + " Priority: " + pcb.getSchedule() );
		}
	}

	/**
	*Unit Test for Priority Queue
	*/
	public static void main(String[] args) {
		try{
			System.out.println("Running Unit Tests on Priority Queue");
			PriorityQueue pq = new PriorityQueue();



			System.out.println("Adding a head");
			pq.setHead(new ProcessControlBlock(3).setSchedule(3));
			System.out.println("Printing Queue");
			pq.printQueue();
			System.out.println("Adding A new head make sure priority works properly");
			pq.setHead(new ProcessControlBlock(4).setSchedule(2));
			System.out.println("Printing Queue");
			pq.printQueue();
			System.out.println("Enqueuing PID 1, priority 1");
			pq.enQueue(new ProcessControlBlock(1).setSchedule(1));
			pq.printQueue();
			System.out.println("Adding ProcessControlBlock that should be at the end");
			pq.enQueue(new ProcessControlBlock(5).setSchedule(5));
			System.out.println("Printing Queue");
			pq.printQueue();

			System.out.println("End Unit Tests for Priority Queue");

		}catch(Exception e){
			System.out.println("Unit Test on Queue Failed");
			for(StackTraceElement element : e.getStackTrace()){
				System.out.println("Trace: " + element.toString());
			}		
		}


	}

}