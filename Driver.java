import java.io.*;
import javax.swing.JOptionPane;

public class Driver{
	
	String option = "Enter S to Run Simulation\n"
				   +"Enter q to quit the program.";

	String commands = "Enter 1 for FIFO Queue CPU Simulation\n"
					+"Enter 2 for Shortest Job First CPU Simulation\n"
					+"Enter 3 for Priorty Queue CPU Simulation (Non Preemptive)\n"
					+"Enter 4 for Priorty Queue CPU Simulation (Preemptive)\n"
					+"Enter 5 for Round Robin CPU Simulation";

	public String getCPUType(){
		return JOptionPane.showInputDialog(null,commands,JOptionPane.QUESTION_MESSAGE);
	}

	public String getOption(){
		return JOptionPane.showInputDialog(null,option,JOptionPane.QUESTION_MESSAGE);	
	}

	public boolean handleOption(String option){
		if(option.toUpperCase().equals("S")){
			return true;
		}else if (option.toUpperCase().equals("Q")){
			return false;
		}else{
			//WE'll ask them for input again
			return true;
		}
	}

	public boolean handleCPUType(String cType){
		int aType = -1;
		try{
			aType = Integer.parseInt(cType);
		}catch(Exception e){
			//Problem parsing integer
			return false;
		}
		switch (aType) {
			case 1:

				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			case 5:

				break;
			default:

				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Driver d = new Driver();
		
	}

}