
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketAddress;

import javax.swing.JLabel;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class ManejadorPeticiones implements Runnable{

	private Socket socketCliente;
	private ServerWindow ventana;
	
	
	public ManejadorPeticiones(Socket socketCliente, ServerWindow ventana){
		this.socketCliente = socketCliente;
		this.ventana = ventana;
	}
	
	@Override
	public void run() {
		
		ventana.getTxtMensajes().append("Atendiendo una petición de un cliente desde el hilo " + Thread.currentThread().getName());
		
		System.out.println("\nAtendiendo una petición de un cliente desde el hilo " + Thread.currentThread().getName());
		
		// Creamos un buffer de entrada para recibir el mensaje del cliente
		
		BufferedReader buffer = null;
		
		try {
			buffer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			String mensaje = buffer.readLine();
			
			SocketAddress direccionSocket = socketCliente.getRemoteSocketAddress();
			
			String ipCliente = direccionSocket.toString();
			String[] campos = ipCliente.split(":");
			ipCliente = campos[0].substring(1);
			
			ventana.getTxtMensajes().append(ipCliente + " - " + mensaje + "\n");
			
			switch(mensaje){
			case "ON-LED1":
				ventana.setLed1(true);
				ventana.getLblLight1().setText("<html><div style='width:50px;height:50px;background-color:green;'></div></html>");
				ventana.getMyLedGreen().setState(PinState.HIGH);
				break;
			case "OFF-LED1":
				ventana.setLed1(false);
				ventana.getLblLight1().setText("<html><div style='width:50px;height:50px;background-color:red;'></div></html>");
				ventana.getMyLedGreen().setState(PinState.LOW);
				break;
			case "ON-LED2":
				ventana.setLed2(true);
				ventana.getLblLight2().setText("<html><div style='width:50px;height:50px;background-color:green;'></div></html>");
				ventana.getMyLedRed().setState(PinState.HIGH);

				break;
			case "OFF-LED2":
				ventana.setLed2(false);
				ventana.getLblLight2().setText("<html><div style='width:50px;height:50px;background-color:red;'></div></html>");
				ventana.getMyLedRed().setState(PinState.LOW);

				break;
			case "ON-LED3":
				ventana.setLed3(true);
				ventana.getLblLight3().setText("<html><div style='width:50px;height:50px;background-color:green;'></div></html>");
				ventana.getMyLedYellow().setState(PinState.HIGH);
				break;
			case "OFF-LED3":
				ventana.setLed3(false);
				ventana.getLblLight3().setText("<html><div style='width:50px;height:50px;background-color:red;'></div></html>");
				ventana.getMyLedYellow().setState(PinState.LOW);
				break;
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	
	
}
