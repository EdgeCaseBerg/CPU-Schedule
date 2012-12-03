
import java.util.HashMap;
import java.util.Random;


//Main Driver Function for the Program
public class CPU{

	Random rands = new Random();

	long nextPID = 0;

	Scheduler jobScheduler;

	/*
	*List of current processes in Memory. Visible to all
	*/
	public static HashMap<Integer,Process> processes = new HashMap<Integer,Process>();

	/**
	*The current process being executed 
	*/
	ProcessControlBlock currentJob = null;

	public static void main(String[] args) {
		//Check for arguments
		CPU cpu;
		if(args.length > 0){
			if(args[0].equals("TEST")){
				CPU.UnitTest();
			}
			cpu = new CPU(args[0]);
		}else{
			cpu = new CPU("");
		}
		//Now add some starting jobs...
		for(int i = 0; i < 8; i++){
			Process p = cpu.newJob();

			cpu.allocateJob(p);
			if(args.length > 0){
				if(args[0].equals("PRIORITY") || args[0].equals("PRIORITY PREEMPTIVE")){
					cpu.scheduleJob(p, cpu.rands.nextInt(2000));
				}else{
					cpu.scheduleJob(p);		
				}
			}else{
				cpu.scheduleJob(p);
			}
			
		}
		cpu.run();
		
	}

	public static void UnitTest(){
		//Test everything in here!
	}

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
	public void scheduleJob(Process p){
		//This function is only used to add jobs at the inialization step of the simulator
		jobScheduler.schedule(p);
	}
	public void scheduleJob(Process p,long priority){
		jobScheduler.scheduleWithPriority(p,priority);
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
			currentJob.setSchedule(currentJob.getSchedule() - jobScheduler.timeSlice);
			Process p = processes.get(currentJob.getPID());
			if(currentJob.getSchedule() <= 0){
				//Job is finished
				currentJob.changeStateTo(State.TERMINATED);
				return;
			}
		}else{
			//We are using shortest job first, priority Queue, or FIFO
			//We are now executing this job for a little while
			Process p = processes.get(currentJob.getPID());
			long runtime = System.currentTimeMillis() - currentJob.getStartTime()  + rands.nextInt(Math.abs((int)p.getBurst())+1) + rands.nextInt(100) - rands.nextInt(100);
			for(int i=0; i < runtime; i++){
				//Executing the job!
				if(rands.nextInt(1000) > 998){
					//.01% chance to to have to do IO during execution (I do this because I dont want to sit for this program to 
					//stop running for a very long time.)
					System.out.println("Current job needs to perform IO. Returning control to CPU");
					currentJob.doIO();
					//Update stats with i as running time...
					//Return control to CPU
					return;			
				}
			}
			//Execution of program is done
			currentJob.changeStateTo(State.TERMINATED);
		}
	}

	public void printStatus(){
		//Print out the current state of the CPU:
			System.out.println("Current State of the CPU: ");
			System.out.println("________________________________________");
			System.out.println("Process List:");
			for(Process p : processes.values() ){
				System.out.println("PID: " + p.PID  );
			}
			System.out.println("________________________________________");
			System.out.println("Current State of the Process Queue:");
			jobScheduler.printState();
	}

	public void run(){
		do{
			System.out.println("=============================================");
			System.out.println("Getting Job from Queue");
			currentJob = jobScheduler.nextJob();


			if(currentJob == null){
				//No Job!
				System.out.println("No More Jobs on Queue. Exiting Simulation");
				break;
			}
			System.out.println(" with PID " + currentJob.getPID());

			if(currentJob.getState()==State.WAITING){
				//If the job was waiting on IO then is it done?
				//With some probability we change it back to be ready
				if(rands.nextInt(1000) %  2 == 0){
					//About a 50% change we'll switch it either way.
					System.out.println("Process with PID " + currentJob.getPID() + " has finished IO");
					currentJob.finishIO();
					//Now we need to execute
				}
			}
			if(currentJob.getState()==State.NEW){
				currentJob.setStartTime(System.currentTimeMillis());
			}else if(currentJob.getState()==State.TERMINATED){
				//Some how a dead job is sitting on the queue, remove it!
				jobScheduler.removeJob(currentJob);
				printStatus();
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
							System.out.println("Scheduling new job with PID " + p.PID + " with higher priority than current job. PreEmpting...");
							jobScheduler.scheduleWithPriority(p,newJobPriority);
							jobScheduler.scheduleWithPriority(currentJob,currentJob.getSchedule());
							printStatus();
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
			executeJob();
			//Did we finish the job?
			if(currentJob.getState()==State.TERMINATED){
				freeJob(currentJob.getPID());
			}else{
				if(currentJob.getState()!= State.WAITING){
					currentJob.changeStateTo(State.HALTED);	
				}
				jobScheduler.scheduleWithPriority(currentJob,currentJob.getSchedule());
			}
			printStatus();
			
		}while(currentJob != null);
		//Done Executing

	}



}