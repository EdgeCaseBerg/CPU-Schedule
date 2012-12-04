import java.io.*;
import javax.swing.JOptionPane;

public class Driver{
	
	String option = "";
	String command = "Enter 1 for FIFO Queue CPU Simulation"+ System.getProperty("line.seperator")
					+"";

	public String getInput(){
		return JOptionPane.showInputDialog(null,command);
	}

	public static void main(String[] args) {
		
	}

}