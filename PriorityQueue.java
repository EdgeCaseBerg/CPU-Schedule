

public class PriorityQueue extends FIFOQueue{
	
	public PriorityQueue(){
	}
	
	/**
	*Adds the ProcessControlBlock to the Queue as a new Head if its priorty is lower.
	*@param pcb The new head for the Queue.
	*/
	@Override
	private void setHead( ProcessControlBlock pcb ){
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
			for(node = head; node.next != null; node = node.next){
				if(pcb.getSchedule() < node.getPCB().getSchedule() ){

				}
			}
			
		}
	}



}