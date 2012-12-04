import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Dimension;

public class Driver{
	ConsoleStream cs;

	private class ConsoleStream extends PrintStream{

		String option = "<html>Enter S to Run Simulation<br>"
				   +"Enter q to quit the program.</html>";

		String commands = "<html>Enter 1 for FIFO Queue CPU Simulation<br>"
					+"Enter 2 for Shortest Job First CPU Simulation<br>"
					+"Enter 3 for Priorty Queue CPU Simulation (Non Preemptive)<br>"
					+"Enter 4 for Priorty Queue CPU Simulation (Preemptive)<br>"
					+"Enter 5 for Round Robin CPU Simulation</html>";

		JFrame consoleOut = new JFrame();
		JScrollPane scroll = new JScrollPane();
		JTextArea pane = new JTextArea();
		JTextArea input = new JTextArea();
		JButton enter = new JButton("Enter");
		JButton quit = new JButton("Quit");
		JLabel info = new JLabel(option);

		public ConsoleStream(OutputStream out){
			super(out);

			
			pane.setPreferredSize(new Dimension(550,100000));
			pane.setEditable(false);
			pane.setVisible(true);

			scroll = new JScrollPane(pane);
			scroll.setPreferredSize(new Dimension(550,300));
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setVisible(true);
			scroll.setAutoscrolls(true);

			consoleOut.setLayout(new BoxLayout(consoleOut.getContentPane(),BoxLayout.Y_AXIS));
			

			consoleOut.setTitle("Simulation Output");
			consoleOut.setSize(600,400);
			consoleOut.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			consoleOut.add(scroll);
			consoleOut.add(info);
			consoleOut.add(enter);
			consoleOut.add(quit);

			consoleOut.setVisible(true);
			
		}

		@Override
		public void println(String x){
			//We need to add mroe space for all our text!
			JScrollBar sb = scroll.getVerticalScrollBar();
			int curVal = sb.getValue();
			if(sb.getMaximum() == curVal + sb.getVisibleAmount()){
				//We're at the max
				pane.setPreferredSize(new Dimension(550,(int)pane.getPreferredSize().getHeight() + 1000 ));
			}

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
	

	boolean confusion = false;

	

	public void loop(){
		while(handleOption(getOption())){
			//If they didn't enter a proper command then they are confused, ask again.
			if(confusion){continue;}

			if(handleCPUType(getCPUType())){
				//We have run the cpu. So open it's output file and show it the Haiguang
				if(handleCPUType(getCPUType())){

				}else{
					//Invalid CPU Type or command
				}
			}else{
				//Improper Input or invalid option
			}
		}
	}

	public String getCPUType(){
		return "S";
	}

	public String getOption(){
		return "1";
	}

	public boolean handleOption(String option){
		if(option==null){
			return false;
		}
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
		if(cType==null){return false;}
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
			cs = new ConsoleStream(new BufferedOutputStream(new FileOutputStream("output.txt")));
			System.setOut(cs);
		}catch(Exception e){
			System.out.println("Couldn't open output.txt");
		}
		
	}

	public static void main(String[] args) {
		Driver d = new Driver();
		d.loop();

		
	}

}