package concurrencia;

public class Vehiculo extends Thread{
    //Atributos de la clase vehiculo
    private String identificador = "";
    private String tipo = "";
    private Peaje peaje;
    private CabinaManual cabinaManual = null;
    private CabinaAutomatica cabinaAutomatica = null;
    private Paso paso;

    //Métodos de la clase Vehículo

    //Método constructor
    public Vehiculo(Peaje _peaje, String _tipo, int numero, Paso _paso){
        this.peaje = _peaje;
        this.tipo = _tipo;
        this.identificador = _tipo + numero;
        this.paso = _paso;
        super.setName(this.identificador);
    }
    //Método run
    public void run(){
        //En primer lugar, entramos al peaje
        getPaso().mirar();
        peaje.entraPeajeVehiculo(this);
        //Una vez salga de la funcion de entrar es porque tiene una cabina a la que entrar, tenemos que ver si es manual o automática
        if(this.getCabinaManual() != null){
            //Verificamos que ha entrado a una cabina manual
            getPaso().mirar();
            getCabinaManual().vehiculoCabinaManual(this);
        }
        else{
            //Verificamos que ha entrado a una cabina automática
            getPaso().mirar();
            getCabinaAutomatica().entraVehiculo(this);
            getPaso().mirar();
            getCabinaAutomatica().vehiculoPaga(this);
            getPaso().mirar();
            getCabinaAutomatica().saleVehiculo(this);
        }
        //Una vez que hemos salido de la cabina, nos saldremos del peaje
        getPaso().mirar();
        peaje.saleVehiculo(this);
    }
    //Método para que el vehiculo pague en una cabina automática
    public void pagaCabinaAutomatica(){
        try{
            Thread.sleep(5000);
        }catch(InterruptedException ie) {}
    }


    //Método get del identificador del vehículo
    public String getIdentificador() {
        return identificador;
    }
    //Método get para el tipo de vehículo
    public String getTipo() {
        return tipo;
    }
    //Método get para la cabina manual a la que va a ir ese vehiculo
    public CabinaManual getCabinaManual() {
        return cabinaManual;
    }
    //Método set de la cabina manual a la que va a ir ese vehiculo
    public void setCabinaManual(CabinaManual cabinaManual) {
        this.cabinaManual = cabinaManual;
    }
    //Método get para la cabina automática a la que va a ir ese vehiculo
    public CabinaAutomatica getCabinaAutomatica() {
        return cabinaAutomatica;
    }
    //Método set para la cabina automática a la que va a ir ese vehiculo
    public void setCabinaAutomatica(CabinaAutomatica cabinaAutomatica) {
        this.cabinaAutomatica = cabinaAutomatica;
    }
    //Método para acceder al paso
    public Paso getPaso() {
        return paso;
    }
}