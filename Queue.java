
/**
*Basic FIFO queue
*/
public class Queue{
	//Inner class to avoid code bloat into multiple files.
	protected class Link{
		private ProcessControlBlock pcb = null;
		public Link next = null;

		//Empty link
		public Link(){
		}

		public Link(ProcessControlBlock pcb){
			this.pcb = pcb;
		}

		public Link(ProcessControlBlock pcb, Link next){
			this.pcb = pcb;
			this.next = next;
		}

		public ProcessControlBlock getPCB(){
			return pcb;
		}
	}

	protected Link head = null;
	protected Link tail = null;

	public Queue(){
	}

	/**
	*Adds the ProcessControlBlock to the Queue as a new Head.
	*@param pcb The new head for the Queue.
	*/
	protected void setHead( ProcessControlBlock pcb ){
		if(head ==null){
			//New Linked List
			head = new Link(pcb);
			tail = head;
		}else{
			//Update list
			Link newHead = new Link(pcb,head);
			head = newHead;
		}
	}

	/**
	*Adds a ProcessControlBlock to the Queue
	*@param adds ProcessControlBlock pcb to the Quque. FIFO style.
	*/
	public void enQueue(ProcessControlBlock pcb){
		if(head == null){
			setHead(pcb);
		}else{
			//Tail should never be null, but we check for it for safety
			if(tail == null){
				tail = new Link(pcb);

				Link node;
				//Traverse
				for(node = head; node.next != null; node = node.next){}
				node.next = tail;
			}else{
				tail.next = new Link(pcb);
				tail = tail.next;
			}
		}
	}

	/**
	*Returns and removes the head of the list
	*@return The head of the list.
	*/
	public ProcessControlBlock deQueue(){
		if(tail == null && head == null){
			//No structure exists
			return null;
		}else{
			//Save the old head
			Link temp = head;
			head = head.next;
			//Handle degenerate case of the list
			tail = head == null ? null : tail;
			return temp.getPCB();
		}
	}

	/**
	*Finds the Process Control Block for the given PID
	*@param PID The process id to search for in the Queue
	*@return Returns the ProcessControlBlock with process id PID, null if not found.
	*/
	public ProcessControlBlock find(int PID){
		Link node = head;
		for(;node != null; node = node.next){
			if(node.getPCB().getPID()==PID){
				return node.getPCB();
			}
		}
		return null;
	}

	/**
	*Removes the PCB containing the process with the given PID 
	*@param PID the id of the process
	*@return true if the process was removed, false if the process was not found in the Queue
	*/
	public boolean remove(int PID){
		if(head == null){
			return false;
		}
		Link previous = null;
		Link node = head;
		boolean found = false;
		for(;node != null && !found; node = node.next){
			if(node.getPCB().getPID()==PID){
				//We found it
				found = true;
				break;
			}else{
				previous = node;
			}
		}
		if(previous == null){
			//We want to remove the head of  list
			head = head.next;
			//Handle case where list was 1 thing long
			tail = head == null ? null : tail;
			return true;
		}else{
			if(!found){
				return false;
			}
			if(node.next == null){
				//Removing the tail
				tail = previous;
				previous.next = null;
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
	public void insertBefore(int PID,ProcessControlBlock pcb){
		//Check for null list
		if(empty()){return;
		}else{
			Link previous = null;
			Link node = head;
			for(; node != null; node = node.next){
				if(node.getPCB().getPID()==PID){
					//found it!
					if(previous == null){
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
			//Did not find the PID in the list, adding to the queue at the end (in SJB for priority)
			tail.next = new Link(pcb);
			tail = tail.next;
		}
	}

	/**
	*Returns whether or not the Queue is empty.
	*@return Returns true if the queue is empty, false otherwise.
	*/
	public boolean empty(){
		return head == null;
	}

	/**
	*Prints the Queue.
	*/
	public void printQueue(){
		for(Link node = head; node != null; node = node.next){
			System.out.println(node.getPCB());
		}
	}

	/**
	*Unit Test.
	*/
	public static void main(String[] args) {
		System.out.println("Running Unit Tests on Queue");
		Queue q = new Queue();
		for(int i = 0; i < 5; i++){
			ProcessControlBlock p = new ProcessControlBlock(i);
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
		System.out.println("Unit Tests on Queue done.");

	}

}