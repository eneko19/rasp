import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


public class ServerWindow extends JFrame{
	
	private JTextArea txtMensajes;
	private boolean led1, led2, led3 = false;
	private JLabel lblLight1, lblLight2, lblLight3;
	

	final GpioController gpio = GpioFactory.getInstance();

	private GpioPinDigitalOutput myLedGreen = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);
	private GpioPinDigitalOutput myLedRed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
	private GpioPinDigitalOutput myLedYellow = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);

	
	public GpioPinDigitalOutput getMyLedGreen(){
		return this.myLedGreen;
	}
	
	public GpioPinDigitalOutput getMyLedRed(){
		return this.myLedRed;
	}
	
	public GpioPinDigitalOutput getMyLedYellow(){
		return this.myLedYellow;
	}
	
	public JLabel getLblLight1() {
		return lblLight1;
	}

	public JLabel getLblLight2() {
		return lblLight2;
	}

	public JLabel getLblLight3() {
		return lblLight3;
	}

	public void setLed1(boolean led1) {
		this.led1 = led1;
	}

	public void setLed2(boolean led2) {
		this.led2 = led2;
	}

	public void setLed3(boolean led3) {
		this.led3 = led3;
	}

	public ServerWindow(){
		super("Servidor - Casa Domótica");
		setSize(500, 500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		
		JPanel pnIndicadores = new JPanel();
		pnIndicadores.setBorder(BorderFactory.createEmptyBorder(70, 0, 0, 0));
		
		lblLight1 = new JLabel("<html><div style='width:50px;height:50px;background-color:red;'></div></html>");
		lblLight2 = new JLabel("<html><div style='width:50px;height:50px;background-color:red;'></div></html>");
		lblLight3 = new JLabel("<html><div style='width:50px;height:50px;background-color:red;'></div></html>");
		
		lblLight1.setOpaque(true);
		lblLight2.setOpaque(true);
		lblLight3.setOpaque(true);
		
		lblLight2.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
		lblLight3.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		
		pnIndicadores.add(lblLight1);
		pnIndicadores.add(lblLight2);
		pnIndicadores.add(lblLight3);
		
		JPanel pnMensajes = new JPanel();
		pnMensajes.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
		
		txtMensajes = new JTextArea(20,30);
		txtMensajes.setLineWrap(true);
		
		JScrollPane scrMensajes = new JScrollPane(txtMensajes);
		
		pnMensajes.add(scrMensajes);
		
		JTabbedPane mainTab = new JTabbedPane();
		mainTab.setPreferredSize(new Dimension(400,400));
		mainTab.addTab("Indicadores", pnIndicadores);
		mainTab.addTab("Mensajes", pnMensajes);
		
		mainTab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		
		add(mainTab);
		setVisible(true);
		
		iniciarServicio();
		
	}
	
	private void iniciarServicio() {

		ServerSocket socketServidor;
		
		try {
			socketServidor = new ServerSocket(5000);
			
			Socket socketCliente;
			
			while(true){
				
				socketCliente = socketServidor.accept();
				
				// Crear un hilo nuevo, pasarle socketCliente
				// (que permite obtener el mensaje que nos han enviado desde un cliente.
				Thread hilo = new Thread(new ManejadorPeticiones(socketCliente, this));
				
				hilo.start();
				
				
				txtMensajes.append("Ha llegado una petición!!!\n LED1: " + led1 + "\nLED2: " + led2 + "\nLED3: " + led3);
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	

	public static void main(String[] args) {
		
		new ServerWindow();

	}

	public JTextArea getTxtMensajes() {
		return txtMensajes;
	}
	
}
