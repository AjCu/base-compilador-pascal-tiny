package ve.edu.unet.nodosAST;

public class NodoIdentificador extends NodoBase {
	private String nombre;

	protected int indiceInicio, indiceFin;

	public void setIndice(NodoBase indice) {
		this.indice = indice;
	}

	private NodoBase indice;

	public void setValues(NodoBase[] values) {
		this.values = values;
	}

	public NodoBase[] getValues() {
		return values;
	}

	private NodoBase[] values;

	public void setValuesSize(int size) {
		values = new NodoBase[size];
	}



	public NodoBase getValue(NodoBase indice) {
		if(indice != null){
			int indiceCalculado = Util.calculateNodoValue(indice,null);
			int indiceReal = indiceCalculado - indiceInicio;
			return values[indiceReal];
		}else{
			return values[0];
		}

	}

	public int getRealSize(){
		return (indiceFin - indiceInicio) + 1;
	}
	public void setValue(NodoBase indice, NodoBase value) throws Exception {
		if(indice != null){
			int indiceCalculado = Util.calculateNodoValue(indice,null);
			int indiceReal = indiceCalculado - indiceInicio;
			if (value != null){
				values[indiceReal] = value;
			}else{
				throw new Exception("Error: Vector '" + nombre + "' no ha sido inicializado correctamente ");

			}
		}else{
			values[0] = value;
		}

	}

	public NodoIdentificador(String nombre) {
		super();
		this.nombre = nombre;
		this.indice = null;
		this.indiceInicio = 0;
		this.indiceFin = 0;
		this.values = new NodoBase[1];
	}

	public NodoIdentificador(String nombre, NodoBase indice) {
		super();
		this.nombre = nombre;
		this.indice = indice;
		this.indiceInicio = 0;
		this.indiceFin = 0;
		this.values = new NodoBase[1];
	}

	public NodoIdentificador(String nombre, int indiceInicio, int indiceFin) {

		this.nombre = nombre;
		this.indiceInicio = indiceInicio;
		this.indiceFin = indiceFin;
		this.values = new NodoBase[1];

	}

	public NodoIdentificador() {
		super();
	}

	public NodoIdentificador(String nombre, int indiceInicio, int indiceFin, NodoBase indice) {
		super();
		this.nombre = nombre;
		this.indice = indice;
		this.indiceInicio = indiceInicio;
		this.indiceFin = indiceFin;
		this.values = new NodoBase[1];

	}

	public String getNombre() {
		return nombre;
	}

	public int getIndiceInicio() {
		return indiceInicio;
	}

	public int getIndiceFin() {
		return indiceFin;
	}

	public NodoBase getIndice() {
		return indice;
	}

	public void setIndiceInicio(int indiceInicio) {
		this.indiceInicio = indiceInicio;
	}

	public void setIndiceFin(int indiceFin) {
		this.indiceFin = indiceFin;
	}

}
