
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

}