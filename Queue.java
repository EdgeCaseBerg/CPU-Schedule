
/**
*Basic FIFO queue
*/
public class Queue{
	//Inner class to avoid code bloat into multiple files.
	private class Link{
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

	Link head = null;
	Link tail = null;

	public Queue(){
	}

	public void setHead( ProcessControlBlock pcb ){
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
	*Removes the PCB containing the process with the given PID 
	*@param PID the id of the process
	*@return true if the process was removed, false if the process was not found in the Queue
	*/
	public boolean remove(int PID){
		if(head == null){
			return false;
		}
		Link previous = null;
		Link node;
		for(node = head; node != null; node = node.next){
			if(node.getPCB().getPID()==PID){
				//We found it
				break;
			}else{
				previous = node;
			}
		}
		if(previous == null){
			//We want to remove the head of  list
			Link temp = head;
			head = head.next;
			//Handle case where list was 1 thing long
			tail = head == null ? null : tail;
			return true;
		}else{
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

	public boolean empty(){
		return head == null;
	}

	public void printQueue(){
		for(Link node = head; node != null; node = node.next){
			System.out.println("PID: " + node.getPCB().getPID() + " STATE: " + node.getPCB().getState() );
		}
	}

}