import java.util.ArrayList;

public class Process{
	/**
	*PID of the process
	*/
	public int PID = -1;
	/**
	*Children of this process
	*/
	public ArrayList<Process> children = new ArrayList<Process>();
	/**
	*Expected burst time of the Process
	*/
	private int burstTime = -1;
	/**
	*The program counter for execution of the program.
	*/
	private int progCounter = 0;

}