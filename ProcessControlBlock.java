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
	*The program counter for execution of the program.
	*/
	int progCounter = 0;
	/**
	*Registers for the program to save it's state into. These are just defined for completeness according to page 104;
	*/
	Object[] registers = new Object[12];
	/**
	*Scheduling parameters for use in scheduling queues. Priority for priority queues. Time left for a sjf queue etc.
	*/
	int schedule = -1;

}