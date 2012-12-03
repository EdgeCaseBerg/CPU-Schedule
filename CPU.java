
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
		processes.put(p.PID,p);
	}

	public void executeJob(){
		//Count away 
		System.out.println("Executing Current Job:  " + currentJob);
		


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
					currentJob.changeStateTo(State.READY);
				}
			}else if(currentJob.getState()==State.TERMINATED){
				//Some how a dead job is sitting on the queue, remove it!
				jobScheduler.removeJob(currentJob);
			}else{
				//We have a job to do!
				//Some probabilty we get a new job coming in:
				if(rands.nextInt(1000) > 800){
					//About a 20% change of a new job coming in
					Process p = newJob();
					allocateJob(p);
					//Is the new job coming high priority? (Lower numbers = higher prob)
					long newJobPriority = rands.nextInt(200);

					if(jobScheduler.isPreEmptive()){
						//Allocate the job into memory
						if(newJobPriority < currentJob.getSchedule()){
							//It is higher! so we should preempt it!
							jobScheduler.scheduleWithPriority(p,newJobPriority);
							jobScheduler.scheduleWithPriority(currentJob,currentJob.getSchedule());
							continue;
						}else{
							//Not high so we'll allocate but not preempt the current process
							jobScheduler.scheduleWithPriority(p,newJobPriority);
						}
					}else{
						//Scheduler is not preemptive
						jobScheduler.scheduleWithPriority(p,newJobPriority);
					}
				}else{
					//No new job coming in...
				}


			}
		}while(currentJob != null);
		//Done Executing

	}



}