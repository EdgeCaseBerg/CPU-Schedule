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
	*Expected burst time of the Process (This is placed into the scheduling information of the PCB for execution)
	*/
	private long burstTime = -1;
	/**
	*The program counter for execution of the program.
	*/
	private int progCounter = 0;

	public Process(int PID){
		this.PID = PID;
	}

	public Process setBurst(long milliseconds){
		burstTime = milliseconds;
		return this ;
	}

	public long getBurst(){
		return burstTime;
	}

}