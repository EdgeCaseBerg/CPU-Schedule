import java.lang.Throwable;
import java.util.Random;

public class RoundRobinQueue extends Queue{
	
	public RoundRobinQueue(){
	}
	//Round robin itself doesn't need have too many changes
	//Just to be a circular queue. Which is messy for an enqueue function but thats ok.
	//We just have to be careful to never have a null tail unless the list is empty.

	/**
	*Adds the ProcessControlBlock to the Queue as a new Head.
	*@param pcb The new head for the Queue.
	*/
	@Override
	protected void setHead( ProcessControlBlock pcb ){
		if(head ==null){
			//New Linked List
			head = new Link(pcb);
			tail = head;
			tail.next = head;
		}else{
			//Update list
			Link newHead = new Link(pcb,head);
			head = newHead;
			tail.next = head;
		}
	}

	/**
	*Adds a ProcessControlBlock to the Queue
	*@param adds ProcessControlBlock pcb to the Quque. FIFO style, with circular links throughout the list
	*/
	@Override
	public void enQueue(ProcessControlBlock pcb) {
		if(head == null){
			setHead(pcb);
		}else{
			//Tail should never be null, but we check for it for safety
			if(tail == null) {
				System.out.println("The integrity of the Queue has been compromised. Exiting. Null Pointer for Tail");
				System.exit(1);
			}else{
				//Enqueue first come first serve with circular links to head from tail
				tail.next = new Link(pcb,head);
				tail = tail.next;
			}
		}
	}

	/**
	*Returns and removes the head of the list
	*@return The head of the list.
	*/
	@Override
	public ProcessControlBlock deQueue(){
		if(tail == null && head == null){
			//No structure exists
			return null;
		}else{
			//Save the old head
			Link temp = head;
			
			//Handle degenerate case of the list (1 item in list)
			if(head == tail){
				tail = null;
				head = null;	
			}else{
				//If we have plenty of items then we need to link the new tail to the head
				head = head.next;
				tail.next = head;
			}
			
			return temp.getPCB();
		}
	}

	/**
	*Finds the Process Control Block for the given PID
	*@param PID The process id to search for in the Queue
	*@return Returns the ProcessControlBlock with process id PID, null if not found.
	*/
	@Override
	public ProcessControlBlock find(int PID){
		if(head == null){ return null;	}
		//We have a list:
		Link node = head;
		if(head == tail){
			//Special case of 1 list item
			if(head.getPCB().getPID()==PID){
				return head.getPCB();
			}else{
				return null;
			}
		}
		//For when we have items in the list
		for(;node != tail; node = node.next){
			if(node.getPCB().getPID()==PID){
				return node.getPCB();
			}
		}
		//Check the tail becuase the above traversel will not.
		if(tail.getPCB().getPID()==PID){
			return node.getPCB();
		}
		return null;
	}

	/**
	*Removes the PCB containing the process with the given PID 
	*@param PID the id of the process
	*@return true if the process was removed, false if the process was not found in the Queue
	*/
	@Override
	public boolean remove(int PID){
		if(empty()){
			return false;
		}
		Link previous = tail;
		Link node = head;
		boolean found = false;
		for(;node != tail && !found; node = node.next){
			if(node.getPCB().getPID()==PID){
				//We found it
				found = true;
				break;
			}else{
				previous = node;
			}
		}
		if(previous == tail){
			//We want to remove the head of  list
			if(head == tail){
				//One item in list:
				head = null;
				tail = null;
				return true;
			}
			//The other cases of the list
			head = head.next;
			tail.next = head;
			return true;
		}else{
			if(!found){
				return false;
			}
			if(node.next == head){
				//Removing the tail
				tail = previous;
				previous.next = head;
			}else{
				previous.next = node.next;
			}
			return true;
		}
	}

	/**
	*Inserts the pcb before the specified PID in the queue. if the PID is not found then the pcb is just enqueued.
	*@param PID The process id of the Process to insert before
	*@param the ProcessControlBlock to insert
	*/
	@Override
	public void insertBefore(int PID,ProcessControlBlock pcb){
		//Check for null list
		if(empty()){return;
		}else{
			Link previous = tail;
			Link node = head;
			for(; node != tail; node = node.next){
				if(node.getPCB().getPID()==PID){
					//found it!
					if(previous == tail){
						//Insert before the head
						setHead(pcb);
						//Returns for efficiency
						return;
					}else{
						previous.next = new Link(pcb,node);
						//Returns for efficiency
						return;
					}
				}
				previous = node;
			}
			//Check the tail
			if(tail.getPCB().getPID()==PID){
				if(head == tail){
					//Special case of 1 item in list
					head.next = new Link(pcb,head);
					return;
				}else{
					//More than one item
					previous.next = new Link(pcb,tail);
					return;
				}
			}
			//Did not find the PID in the list, adding to the queue at the end (in SJB for priority)
			tail.next = new Link(pcb,head);
			tail = tail.next;
		}
	}

	/**
	*Prints the Queue.
	*/
	@Override
	public void printQueue(){
		if(head == null){
			System.out.println("Empty Queue");
			return;
		}
		for(Link node = head; node != tail; node = node.next){
			ProcessControlBlock pcb = node.getPCB();
			System.out.println("PID: " + pcb.getPID() + " Actual CPU Time (ms): " + (System.currentTimeMillis() -pcb.getStartTime() ) + " Desired CPU Time (ms): " + pcb.getProcess().getBurst() + " State: " + pcb.getState() + " Time Left: " + pcb.getSchedule());
		} 
		ProcessControlBlock pcb = tail.getPCB();
		System.out.println("PID: " + pcb.getPID() + " Actual CPU Time (ms): " + (System.currentTimeMillis() -pcb.getStartTime()) + " Desired CPU Time (ms): " + pcb.getProcess().getBurst() + " State: " + pcb.getState() + " Time Left: " + pcb.getSchedule());
	}

	public static void main(String[] args) {
		try{
			System.out.println("Running Unit Tests on Round Robin Queue");
			RoundRobinQueue q = new RoundRobinQueue();


			//Random stuff for time slices
			Random gen = new Random();

			for(int i = 0; i < 5; i++){
				//Random time slices
				ProcessControlBlock p = new ProcessControlBlock(i).setSchedule(gen.nextInt(1000)); 	
				if(i % 3 == 0){
					//Just messing with some state
					p.doIO();
				}
				q.enQueue(p);
			}
			q.printQueue();
			System.out.println("Queue is not empty: " + !q.empty());
			System.out.println("Setting new head: ");
			q.setHead(new ProcessControlBlock(11).changeStateTo(State.RUNNING));
			q.printQueue();
			System.out.println("Attempting to find non existent pcb: " + q.find(45));
			System.out.println("Finding PCB with PID 3: " + q.find(3));
			System.out.println("Dequeue: " + q.deQueue());
			System.out.println("Printing Queue");
			q.printQueue();
			System.out.println("Enqueing PID 7: ");
			q.enQueue(new ProcessControlBlock(7));
			System.out.println("Printing Queue");
			q.printQueue();
			System.out.println("Inserting before 7 a PID 6");
			q.insertBefore(7,new ProcessControlBlock(6));
			System.out.println("Printing Queue");
			q.printQueue();
			System.out.println("Trying to remove something that isn't there");
			q.remove(67);
			System.out.println("Printing Queue");
			q.printQueue();
			System.out.println("Removing PID 2 ");
			q.remove(2);
			System.out.println("Printing Queue");
			q.printQueue();
			System.out.println("Enqueing PID 17: ");
			q.enQueue(new ProcessControlBlock(17));
			System.out.println("Printing Queue");
			q.printQueue();
			System.out.println("Unit Tests on Round Robin Queue done.");

		}catch(Exception e){
			System.out.println("Unit Test on Round Robin Queue Failed");
			for(StackTraceElement element : e.getStackTrace()){
				System.out.println("Trace: " + element.toString());
			}		
		}
	}

}