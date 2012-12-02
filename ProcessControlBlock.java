import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.Stack;
import java.util.Map;

public class ProcessControlBlock{
	/**
	*The state of the process control block
	*/
	State pState = State.NEW;
	/**
	*The process number or PID of the process. Assigned by the OS. 
	*/
	int pNumber = -1;
	/**
	*Registers for the program to save it's state into. These are just defined for completeness according to page 104;
	*/
	Object[] registers = new Object[12];
	/**
	*Scheduling parameters for use in scheduling queues. Priority for priority queues. Time left for a sjf queue etc.
	*/
	int schedule = -1;
	/**
	*Start time of the process 
	*/
	int startTime = -1;

	/**
	*Default control block constructor
	*/
	public ProcessControlBlock(int PID){
		pNumber = PID;
		pState = State.NEW;
	}	

	/**
	*Change the state of the process
	*/
	public void changeStateTo(State s){
		pState = s;
	}

	/**
	*Pretends to do an IO Process
	*/
	public void doIO(){
		pState = State.WAITING;
	}

	public void finishIO(){
		pState = State.READY;
	}

}