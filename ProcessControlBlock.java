import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.Stack;
import java.util.Map;

public class ProcessControlBlock{
	/**
	*The state of the process control block
	*/
	private State pState = State.NEW;
	/**
	*The process number or PID of the process. Assigned by the OS. 
	*/
	public int pNumber = -1;
	/**
	*Registers for the program to save it's state into. These are just defined for completeness according to page 104;
	*/
	private Object[] registers = new Object[12];
	/**
	*Scheduling parameters for use in scheduling queues. Priority for priority queues. Time elapsed for a sjf queue etc.
	*/
	private long schedule = -1;
	/**
	*Start time of the process 
	*/
	private long startTime = -1;


	/**
	*Default control block constructor
	*/
	public ProcessControlBlock(int PID){
		pNumber = PID;
		pState = State.NEW;
	}	

	public ProcessControlBlock(Process p){
		this(p.PID);
	}

	public String toString(){
		return "PID: " + pNumber + " STATE: " + pState + " SCHEDULE: " + schedule; 
	}

	public long getSchedule(){
		return schedule;
	}

	public ProcessControlBlock setSchedule(long priority){
		schedule = priority;
		return this;
	}

	/**
	*Change the state of the process
	*/
	public ProcessControlBlock changeStateTo(State s){
		pState = s;
		return this;
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

	public int getPID(){
		return pNumber;
	}


	public State getState(){
		return pState;
	}

	public void setStartTime(long start){
		this.startTime = start;
	}

	public long getStartTime(){
		return this.startTime;
	}


}