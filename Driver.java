import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Dimension;

public class Driver{
	private class ConsoleStream extends PrintStream{
		JFrame consoleOut = new JFrame();
		JScrollPane scroll = new JScrollPane();
		JTextArea pane = new JTextArea();

		public ConsoleStream(OutputStream out){
			super(out);

			
			pane.setPreferredSize(new Dimension(550,400));
			pane.setEditable(false);
			pane.setVisible(true);

			scroll = new JScrollPane(pane);
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setVisible(true);
			

			consoleOut.setTitle("Simulation Output");
			consoleOut.setSize(600,400);
			consoleOut.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			consoleOut.add(scroll);

			consoleOut.setVisible(true);
			
		}

		@Override
		public void println(String x){
			//Override the println so we can put it to the console.
			pane.append(x);
			pane.append("\n");
			pane.invalidate();
			//My simple way of clearing things
			if(x.equals("CLEARTHISTEXT")){
				clear();
			}
		}

		public void clear(){
			pane.setText("");
		}
	}
	String option = "Enter S to Run Simulation\n"
				   +"Enter q to quit the program.";

	String commands = "Enter 1 for FIFO Queue CPU Simulation\n"
					+"Enter 2 for Shortest Job First CPU Simulation\n"
					+"Enter 3 for Priorty Queue CPU Simulation (Non Preemptive)\n"
					+"Enter 4 for Priorty Queue CPU Simulation (Preemptive)\n"
					+"Enter 5 for Round Robin CPU Simulation";

	boolean confusion = false;

	

	public void loop(){
		while(handleOption(getOption())){
			//If they didn't enter a proper command then they are confused, ask again.
			if(confusion){continue;}

			if(handleCPUType(getCPUType())){
				//We have run the cpu. So open it's output file and show it the Haiguang
				if(handleCPUType(getCPUType())){
					
				}
			}else{
				//Improper Input or invalid option

			}

		}
	}

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
			System.out.println("Entered Incorrect Option");
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
		System.out.println("CLEARTHISTEXT");
		System.out.println("Creating new Simulation: ");
		switch (aType) {
			case 1:
				CPU.main(new String[]{"QUEUE"});
				break;
			case 2:
				CPU.main(new String[]{"SJB"});
				break;
			case 3:
				CPU.main(new String[]{"PRIORITY"});
				break;
			case 4:
				CPU.main(new String[]{"PRIORITY PREEMPTIVE"});
				break;
			case 5:
				CPU.main(new String[]{"ROUND ROBIN"});
				break;
			default:
				System.out.println("CLEARTHISTEXT");
				System.out.println("Entered Invalid option for Queue type");
				return false;
		}
		return true;
	}

	public Driver(){
		try{
			System.setOut(new ConsoleStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
		}catch(Exception e){
			System.out.println("Couldn't open output.txt");
		}
		
	}

	public static void main(String[] args) {
		Driver d = new Driver();


		
	}

}