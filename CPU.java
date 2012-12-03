
import java.util.HashMap;
import java.util.Random;


//Main Driver Function for the Program
public class CPU{

	Random rands = new Random();

	long nextPID = 0;

	Scheduler jobScheduler;

	/*
	*List of current processes in Memory. Visivle to all
	*/
	public static HashMap<Integer,Process> processes = new HashMap<Integer,Process>();

	/**
	*The current process being executed 
	*/
	ProcessControlBlock currentJob = null;

	public CPU(String schedType){
		//Allow the user to decide from command line what the algorithm is
		schedType = schedType.toUpperCase();
		if(schedType.equals("SJB")){
			jobScheduler = new Scheduler(new SJBQueue());
		}else if(schedType.equals("PRIORITY PREEMPTIVE")){
			jobScheduler = new Scheduler(true);
		}else if(schedType.equals("PRIORITY")){
			jobScheduler = new Scheduler(new PriorityQueue());
		}else if(schedType.equals("ROUND ROBIN")){
			jobScheduler = new Scheduler(new RoundRobinQueue());
			//Set the time quanta arbitrarily
			jobScheduler.setTimeSlice(100);
		}else{
			jobScheduler = new Scheduler();
		}
	}
	
	/**
	*Generate a new job to be allocated
	*/
	public Process newJob(){
		//Make a new process with a random burst time between 0 and 1000 milliseconds
		Process p = new Process((int)nextPID).setBurst(rands.nextInt(1000));
		nextPID++;
		return p;
		
	}

	public void allocateJob(Process p){
		//Schedule the job with priority so that no matter what type of scheduler, we're covered.
		//Use the burst because I can't think of a way else to do it? 
		System.out.println("Allocating Job with PID " + p.PID +" into Process List");
		processes.put(p.PID,p);
	}

	public void freeJob(int PID){
		processes.remove(PID);
		currentJob.changeStateTo(State.TERMINATED);
		System.out.println("Terminating Job with PID " + PID);
	}

	public void executeJob(){
		//Count away 
		currentJob.changeStateTo(State.RUNNING);
		System.out.println("Executing Current Job:  " + currentJob);
		//With some probability we will do an IO and return control to CPU
		if(rands.nextInt(1000) > 900){
			//About a 10% chance to do an IO operation
			System.out.println("Current job needs to perform IO. Returning control to CPU");
			currentJob.doIO();
			//Return control to CPU
			return;
		}
		//No IO! So now we run for a timeslice or until its done by asking:
		if(jobScheduler.timeSlice < Long.MAX_VALUE-1){
			//We're using round robin: Execute only for a timeslice
			currentJob.setSchedule(currentJob.getSchedule() + jobScheduler.timeSlice);
			Process p = processes.get(currentJob.getPID());
			if(currentJob.getSchedule() > p.getBurst()){
				//Job is finished
				return;
			}
		}else{
			//We are using shortest job first, priority Queue, or FIFO
			//We are now executing this job for a little while
			Process p = processes.get(currentJob.getPID());
			int runtime = currentJob.getStartTime() - System.currentTimeMillis() + rands.nextInt(p.getBurst()) + rands.nextInt(100) - rands.nextInt(100);
			for(int i=0; i < runtime; i++){
				//Executing the job!
				if(rands.nextInt(1000) > 990){
					//1% chance to to have to do IO during execution (I do this because I dont want to sit for this program to 
					//stop running for a very long time.)
					System.out.println("Current job needs to perform IO. Returning control to CPU");
					currentJob.doIO();
					//Return control to CPU
					return;			
				}
			}
			//Execution of program is done
		}
	}

	public void run(){
		do{
			currentJob = jobScheduler.nextJob();
			if(currentJob == null){
				//No Job!
				break;
			}else if(currentJob.getState()==State.WAITING){
				//If the job was waiting on IO then is it done?
				//With some probability we change it back to be ready
				if(rands.nextInt(1000) %  2 == 0){
					//About a 50% change we'll switch it either way.
					currentJob.finishIO();
					//Now we need to execute
				}
			}
			if(currentJob.getState()==State.NEW){
				currentJob.setStartTime(System.currentTimeMillis());
			}else if(currentJob.getState()==State.TERMINATED){
				//Some how a dead job is sitting on the queue, remove it!
				jobScheduler.removeJob(currentJob);
				continue;
			}else{
				//We have a job to do!
				//Some probabilty we get a new job coming in:
				if(rands.nextInt(1000) > 800){
					//About a 20% change of a new job coming in
					Process p = newJob();
					allocateJob(p);
					//Is the new job coming high priority? (Lower numbers = higher prob)
					long newJobPriority = rands.nextInt(2000);

					if(jobScheduler.isPreEmptive()){
						//Allocate the job into memory
						if(newJobPriority < currentJob.getSchedule()){
							//It is higher! so we should preempt it!
							System.out.println("Scheduling new job with PID + " p.PID + " with higher priority than current job. PreEmpting...")
							jobScheduler.scheduleWithPriority(p,newJobPriority);
							jobScheduler.scheduleWithPriority(currentJob,currentJob.getSchedule());
							continue;
						}else{
							//Not high so we'll allocate but not preempt the current process
							System.out.println("Scheduling new job with PID " + p.PID);
							jobScheduler.scheduleWithPriority(p,newJobPriority);
						}
					}else{
						//Scheduler is not preemptive
						System.out.println("Scheduling new job with PID " + p.PID);
						jobScheduler.scheduleWithPriority(p,newJobPriority);
					}
				}
			}
			//Actually execute the job
			executeJob(currentJob);
			//Did we finish the job?
			if(currentJob.getState()==State.TERMINATED){
				freeJob(currentJob.getPID());
			}else{
				currentJob.changeStateTo(State.HALTED);
				jobScheduler.scheduleWithPriority(currentJob,currentJob.getSchedule());
			}
			//Print out the current state of the CPU:
		}while(currentJob != null);
		//Done Executing

	}



}