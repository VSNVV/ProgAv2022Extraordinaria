package concurrencia;

public class Empleado extends Thread{
    //Atributos de la clase empleado
    private int peajesCobrados = 0;
    private String identificador = "";
    private CabinaManual cabina;
    private Peaje peaje;
    private Paso paso;

    //Método constructor
    public Empleado(Peaje _peaje, String _identificador, int _numero, Paso _paso){
        this.peaje = _peaje;
        this.identificador = _identificador + _numero;
        this.paso = _paso;
        super.setName(this.identificador);
    }
    //Método run
    public void run(){
        //Primero entramos a la cabina y acto seguido la asignamos
        getPaso().mirar();
        setCabina(peaje.entradaEmpleados(this));
        //Una vez salido de aqui, el empleado ya está dentro de su cabina correspondiente
        getCabina().entraEmpleado(this);
        while (true){
            //Una vez dentro del peaje, cobramos el peaje
            getCabina().cobraVehiculo(this);
            //Una vez que haya cobrado al vehículo, revisara cuantos peajes lleva cobrados
            getPaso().mirar();
            if(getPeajesCobrados() == 6){
                //Verificamos que el empleado lleva 6 peajes cobrados, por tanto, nos iremos a descansar
                //Salimos de la cabina
                getCabina().saleEmpleado(this);
                //Una vez que hemos salido de la cabina, nos ponemos a descansar
                descansa();
                //Tras descansar reiniciamos nuestros peajes cobrados a 0
                setPeajesCobrados(0);
                //Volvemos a la cabina donde estábamos
                getPaso().mirar();
                getCabina().entraEmpleado(this);
            }
            else{
                getPaso().mirar();
                getCabina().vuelveEmpleado(this);
            }
        }
    }
    //Método para cobrar al vehiculo
    public void cobraVehiculo() {
        try{
            Thread.sleep((int) (Math.random() * 8000 + 6000));
        }catch(InterruptedException ie) {}
    }
    //Método para que el empleado descanse
    public void descansa(){
        try{
            Thread.sleep(5000);
        }catch(InterruptedException ie) {}
    }
    //Método get del nombre del empleado
    public String getIdentificador() {
        return identificador;
    }
    //Método get para el numero de peajes cobrados
    public int getPeajesCobrados() {
        return peajesCobrados;
    }
    //Método set para el numero de peajes cobrados
    public void setPeajesCobrados(int peajesCobrados) {
        this.peajesCobrados = peajesCobrados;
    }
    //Método get de la cabina en la que trabaja el empleado
    public CabinaManual getCabina() {
        return cabina;
    }
    //Método set de la cabina en la que trabaja el empleado
    public void setCabina(CabinaManual cabina) {
        this.cabina = cabina;
    }
    //Método get del paso
    public Paso getPaso() {
        return paso;
    }
}
