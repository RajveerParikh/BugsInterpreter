package bugs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tree.Tree;


/**
 * GUI for Bugs language.
 * @author Dave Matuszek
 * @author Rajveer Parikh
 * @version 2015
 */
public class BugsGui extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	JPanel display;
	JSlider speedControl;
	int speed;
	JButton newButton;
	JButton stepButton;
	JButton runButton;
	JButton pauseButton;
	JButton resetButton;
	static String fileName;
	View view; 
	Interpreter interpret; 

	/**
	 * GUI constructor.
	 */
	public BugsGui() {
		super();
		
		setSize(600, 600);
		setLayout(new BorderLayout());
		
		createAndInstallMenus();
		
		createDisplayPanel();
		createControlPanel();
		initializeButtons();
		setVisible(true);
		
//		System.out.println(interpret.listOfCommands);
		
	}

	private void createAndInstallMenus() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");        
		JMenu helpMenu = new JMenu("Help");
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		JMenuItem helpMenuItem = new JMenuItem("Help");
		JMenuItem loadMenuItem = new JMenuItem("Load");
	
		menuBar.add(fileMenu);
		fileMenu.add(quitMenuItem);
		quitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				quit();
			}});

		fileMenu.add(loadMenuItem);
		loadMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					load();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});

		menuBar.add(helpMenu);
		helpMenu.add(helpMenuItem);
		helpMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				help();
			}});

		this.setJMenuBar(menuBar);
	}

	private void createDisplayPanel() {
		display = new JPanel();
		
		add(display, BorderLayout.CENTER);
		
//		System.out.println("got here");
//		add(view, BorderLayout.CENTER);
	}


	private void createControlPanel() {
		JPanel controlPanel = new JPanel();

		addSpeedLabel(controlPanel);       
		addSpeedControl(controlPanel);
		addNewButton(controlPanel);
		addStepButton(controlPanel);
		addRunButton(controlPanel);
		addPauseButton(controlPanel);
		addResetButton(controlPanel);

		add(controlPanel, BorderLayout.SOUTH);
	}

	private void addSpeedLabel(JPanel controlPanel) {
		controlPanel.add(new JLabel("Speed:"));
	}

	private void addSpeedControl(JPanel controlPanel) {
		speedControl = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
		speed = 50;
		speedControl.setMajorTickSpacing(10);
		speedControl.setMinorTickSpacing(5);
		speedControl.setPaintTicks(true);
		speedControl.setPaintLabels(true);
		speedControl.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				resetSpeed(speedControl.getValue());
			}
		});
		controlPanel.add(speedControl);
	}

	private void addNewButton(JPanel controlPanel) {
		newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newAnimation();
			}
		});
		controlPanel.add(newButton);
	}

	private void addStepButton(JPanel controlPanel) {
		stepButton = new JButton("Step");
		stepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stepAnimation();
			}
		});
		controlPanel.add(stepButton);
	}

	private void addRunButton(JPanel controlPanel) {
		runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runAnimation();
			}
		});
		controlPanel.add(runButton);
	}

	private void addPauseButton(JPanel controlPanel) {
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pauseAnimation();
			}
		});
		controlPanel.add(pauseButton);
	}

	private void addResetButton(JPanel controlPanel) {
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetAnimation();
			}
		});
		controlPanel.add(resetButton);
	}

	private void initializeButtons() {
		newButton.setEnabled(true);
		stepButton.setEnabled(false);
		runButton.setEnabled(false);
		pauseButton.setEnabled(false);
		resetButton.setEnabled(false);
		//        timer = new Timer(40, taskPerformer);
	}

	private void resetSpeed(int value) {
		speed = value;
	}

	protected void newAnimation() {
		//        model = new Model();
		//        model.setSpeed(speed);
		//        view = displayPanel;
		//        g = displayPanel.getGraphics();
		//        timer.stop();
		//        paint(g);

		newButton.setEnabled(true);
		stepButton.setEnabled(true);
		runButton.setEnabled(true);
		pauseButton.setEnabled(false);
		resetButton.setEnabled(false);
	}

	protected void stepAnimation() {
		//        timer.stop();
		//        runButton.setEnabled(true);
		//        model.setLimits(view.getWidth(), view.getHeight());
		//        model.makeOneStep();
		//        paint(g);
		newButton.setEnabled(true);
		stepButton.setEnabled(true);
		runButton.setEnabled(true);
		pauseButton.setEnabled(false);
		resetButton.setEnabled(true);
	}

	protected void runAnimation() {
		//        timer.start();
		newButton.setEnabled(true);
		stepButton.setEnabled(true);
		runButton.setEnabled(false);
		pauseButton.setEnabled(true);
		resetButton.setEnabled(true);
	}

	protected void pauseAnimation() {
		//        timer.stop();
		newButton.setEnabled(true);
		stepButton.setEnabled(true);
		runButton.setEnabled(true);
		pauseButton.setEnabled(false);
		resetButton.setEnabled(true);
	}

	protected void resetAnimation() {
		//        timer.stop();
		//        model.reset();
		newButton.setEnabled(true);
		stepButton.setEnabled(true);
		runButton.setEnabled(true);
		pauseButton.setEnabled(false);
		resetButton.setEnabled(false);
		//        paint(g);
	}

	protected void load() throws IOException{
		ArrayList<String> lines = null;
		BufferedReader reader;

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Load which file?");
		int result = chooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			if (file != null) {
				fileName = file.getCanonicalPath();
				reader =
						new BufferedReader(new FileReader(fileName));
				lines = new ArrayList<String>();
				String line;
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					sb.append("\n");

				}
				reader.close();
				System.out.println(sb);
				Parser parser = new Parser(sb.toString());
				parser.isProgram(); 				
				Tree<Token> stack = parser.stack.pop();
				interpret = new Interpreter(stack);
				interpret.run();				
				view = new View(interpret);
			}
		}
	}

	protected void help() {
		// TODO Auto-generated method stub
	}

	protected void quit() {
		System.exit(0);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new BugsGui();	
	}

	@Override
	public void run() {	
	}
}