import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.FlowLayout;

public class Driver{
	ConsoleStream cs;

	private class ConsoleStream extends PrintStream{

		int step = 0;

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
		JTextField input = new JTextField();
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
			
			enter.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					entry();
				}
			});
			enter.setMnemonic(KeyEvent.VK_ENTER);

			quit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.exit(0);
				}
			});
			quit.setMnemonic(KeyEvent.VK_ESCAPE);

			input.addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e){
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						entry();
					}else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
						System.exit(0);
					}
				}
			});

			input.setPreferredSize(new Dimension(200,20));
			input.setText("Enter Commands Here");
			input.requestFocus();
			input.setEditable(true);

			JPanel commandPanel = new JPanel(new FlowLayout());


			commandPanel.add(input);
			commandPanel.add(enter);
			commandPanel.add(quit);

			consoleOut.setTitle("Simulation Output");
			consoleOut.setSize(600,400);
			consoleOut.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			consoleOut.add(scroll);
			consoleOut.add(info);
			consoleOut.add(commandPanel);
			
			consoleOut.setVisible(true);
			
		}

		public void entry(){
			if(Driver.handleText(input.getText())){
				if(step==0){
					step++;
					info.setText(commands);
				}else{
					step=0;
					info.setText(option);
					info.invalidate();
				}
			}
			input.setText("");
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
	

	public static boolean handleText(String text){
		if(handleOption(text)){
			//True!
			return true;
		}else{
			//Might have entered it incorrectly, or are doing second part:
			if(handleCPUType(text)){

			}else{
				//They failed
				System.out.println("Incorrect Command");
			}
		}
		return false;
	}

	public static boolean handleOption(String option){
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
			return false;
		}
	}

	public static boolean handleCPUType(String cType){
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
	
	}

}