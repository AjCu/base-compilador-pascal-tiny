package ve.edu.unet;

import ve.edu.unet.nodosAST.tipoDec;

public class RegistroSimbolo {
	private String identificador;
	private int NumLinea;
	private int DireccionMemoria;
	private tipoDec tipo;
	
	public RegistroSimbolo(String identificador, int numLinea,
			int direccionMemoria, tipoDec tipo) {
		super();
		this.identificador = identificador;
		NumLinea = numLinea;
		DireccionMemoria = direccionMemoria;
		this.tipo = tipo;
	}

	public String getIdentificador() {
		return identificador;
	}

	public int getNumLinea() {
		return NumLinea;
	}

	public int getDireccionMemoria() {
		return DireccionMemoria;
	}

	public void setDireccionMemoria(int direccionMemoria) {
		DireccionMemoria = direccionMemoria;
	}

	public tipoDec getTipoDec() {
		return tipo;
	}

	public void setTipoDec(tipoDec tipo) {
		this.tipo = tipo;
	}
}
