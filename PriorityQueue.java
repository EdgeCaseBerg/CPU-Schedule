

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
						previous.next = new Link(pcb,node);
					}
				}
				previous = node;
			}
			//Techinically I shouldn't have to do a boolean check becuase if I added it then I returned (refactor later)
			if(!added){
				//The priority is the worst
				tail.next = new Link(pcb);
				tail = tail.next;
			}
		}
	}



}