import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GUI extends JFrame implements ActionListener{
	

	private JButton btnHabitacion1;
	private JButton btnHabitacion2;
	private JButton btnHabitacion3;
	private JButton btnHabitacion4;
	
	private boolean selectedHabitacion1, selectedHabitacion2, selectedHabitacion3, selectedHabitacion4 = false;

	public GUI(){
		super("RaspBerry PI GUI v1.0");
		setSize(300, 290);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
		
		//Creamos los paneles que utilizaremos
		JPanel pnLeft = new JPanel(new GridLayout(2, 2));
		JPanel pnRight = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel pnCenter = new JPanel(new GridLayout(1, 2));
		pnLeft.setOpaque(false);
		pnRight.setOpaque(false);
	
		btnHabitacion1 = new JButton("<html>Habitación 1:<br> Luz apagada</html>");
		btnHabitacion2 = new JButton("<html>Habitación 2:<br> Luz apagada</html>");
		btnHabitacion3 = new JButton("<html>Habitación 3:<br> Luz apagada</html>");
		btnHabitacion4 = new JButton("<html>Habitación 4:<br> Luz apagada</html>");
		
		btnHabitacion1.addActionListener(this);
		btnHabitacion2.addActionListener(this);
		btnHabitacion3.addActionListener(this);
		btnHabitacion4.addActionListener(this);
		
		pnLeft.add(btnHabitacion1);
		pnLeft.add(btnHabitacion2);
		pnLeft.add(btnHabitacion3);
		pnLeft.add(btnHabitacion4);
		
		
		JTextArea txtLog = new JTextArea("(Log 0001) 14/05/2017 15:23:40 --- Conectando al servidor...", 12, 37);
		txtLog.setEditable(false);
		JScrollPane scrLog = new JScrollPane(txtLog);
		
		pnRight.add(scrLog);
		
		pnCenter.add(pnLeft);
		
		// Creamos la barra de herramientas que contendrá un botón para mostrar los logs
		JToolBar tbLogs = new JToolBar();
		tbLogs.setFloatable(false);
		JMenuBar barLogs = new JMenuBar();
		JMenu mnLogs = new JMenu("Logs");
		JMenuItem itmMostrar = new JMenuItem("Mostrar");
		mnLogs.add(itmMostrar);
		
		barLogs.add(mnLogs);
		tbLogs.add(barLogs);
		
		itmMostrar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(itmMostrar.getText().equals("Mostrar")){
					itmMostrar.setText("Ocultar");
					setSize(600, 290);
					pnCenter.add(pnRight);
				}else{
					itmMostrar.setText("Mostrar");
					setSize(300, 290);
					pnCenter.remove(pnRight);
				}
				
			}
			
		});
		
		setJMenuBar(barLogs);
		
		add(pnCenter, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new GUI();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object curButton = e.getSource();
		
		if(curButton == btnHabitacion1 && !selectedHabitacion1){
			selectedHabitacion1 = true;
			
			enviarMensaje("ON-LED1");
			
			btnHabitacion1.setText("<html>LED Verde:<br> Luz encendida</html>");
		}else if(curButton == btnHabitacion1 && selectedHabitacion1){
			selectedHabitacion1 = false;
			
			enviarMensaje("OFF-LED1");
			
			btnHabitacion1.setText("<html>LED Verde:<br> Luz apagada</html>");
		}else if(curButton == btnHabitacion2 && !selectedHabitacion2){
			selectedHabitacion2 = true;
			
			enviarMensaje("ON-LED2");
			
			btnHabitacion2.setText("<html>LED Rojo:<br> Luz encendida</html>");
		}else if(curButton == btnHabitacion2 && selectedHabitacion2){
			selectedHabitacion2 = false;
			
			enviarMensaje("OFF-LED2");
			
			btnHabitacion2.setText("<html>LED Rojo:<br> Luz apagada</html>");
		}else if(curButton == btnHabitacion3 && !selectedHabitacion3){
			selectedHabitacion3 = true;
			
			enviarMensaje("ON-LED3");
			
			btnHabitacion3.setText("<html>LED Amarillo:<br> Luz encendida</html>");
		}else if(curButton == btnHabitacion3 && selectedHabitacion3){
			selectedHabitacion3 = false;
			
			enviarMensaje("OFF-LED3");
			
			btnHabitacion3.setText("<html>LED Amarillo:<br> Luz apagada</html>");
		}
		
		
		
	}
	
	private void enviarMensaje(String mensaje) {

		Socket socket = null;
		PrintStream salida = null;
		try {
			socket = new Socket("localhost", 5000);
			
			salida = new PrintStream(socket.getOutputStream());
			
			salida.println(mensaje);
			
			
		} catch (IOException e) {
			System.out.println("Error: Ha ocurrido una excepción de entrada y salida.");
		}finally{
			try {
				salida.close();
				socket.close();
			} catch (IOException | NullPointerException e) {
				System.out.println("Error al conectar al servidor.");
			}
		}
		
	}
	

}
