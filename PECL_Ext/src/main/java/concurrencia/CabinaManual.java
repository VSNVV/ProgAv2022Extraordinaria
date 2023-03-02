package concurrencia;

import log.Log;

import javax.swing.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CabinaManual extends Cabina{
    //Atributos de la clase CabinaManual
    private boolean vehiculoEsperando = false;
    private boolean empleadoEsperando = false;
    private Lock cabinaManual = new ReentrantLock();
    private Condition dormirVehiculo = cabinaManual.newCondition();
    private Condition duermeEmpleado = cabinaManual.newCondition();
    private Empleado empleado = null;
    private Vehiculo vehiculoParaCobrar = null;
    private ListaThreads listaEmpleado;
    private ListaThreads listaVehiculo;
    private Log log;

    //Métodos de la clase CabinaManual

    //Método constructor
    public CabinaManual(String _nombre, JTextField _jTextFieldEmpleado, JTextField _jTextFieldVehiculo, Log _log) {
        super(_nombre);
        this.listaEmpleado = new ListaThreads(_jTextFieldEmpleado);
        this.listaVehiculo = new ListaThreads(_jTextFieldVehiculo);
        this.log = _log;
    }
    //Método para que el empleado entre a la cabina
    public void entraEmpleado(Empleado empleado){
        try{
            cabinaManual.lock();
            //Asignamos al empleado como empleado de la cabina
            listaEmpleado.meterEmpleado(empleado);
            //Escribimos por el log y por pantalla
            getLog().escribirEnLog("El empleado " + empleado.getIdentificador() + " ha entrado a la cabina " + getNombreCabina());
            System.out.println("El empleado " + empleado.getIdentificador() + " ha entrado a la cabina " + getNombreCabina());
            //Antes de cobrar a un vehículo tenemos que comprobar si hay uno para cobrar
            if(!isVehiculoEsperando()){
                //Se verifica que no hay un vehiculo para cobrar, por tanto nos quedaremos esperando hasta que venga uno
                setEmpleadoEsperando(true);
                try{
                    duermeEmpleado.await();
                }catch(InterruptedException ignored){}
            }
        }finally{
            cabinaManual.unlock();
        }
    }
    //Método para que el empleado vuelva a la cabina
    public void vuelveEmpleado(Empleado empleado){
        try{
            cabinaManual.lock();
            //Antes de cobrar a un vehículo tenemos que comprobar si hay uno para cobrar
            if(!isVehiculoEsperando()){
                //Se verifica que no hay un vehiculo para cobrar, por tanto nos quedaremos esperando hasta que venga uno
                setEmpleadoEsperando(true);
                try{
                    duermeEmpleado.await();
                }catch(InterruptedException ignored){}
            }
        }finally{
            cabinaManual.unlock();
        }
    }
    //Método que define el ciclo de vida de un vehiculo
    public void vehiculoCabinaManual(Vehiculo vehiculo){
        try{
            cabinaManual.lock();
            //En primer lugar entramos a la cabina
            entraVehiculo(vehiculo);
            //Una vez que hemos pagado, nos iremos
            vehiculo.getPaso().mirar();
            saleVehiculo(vehiculo);
        }finally{
            cabinaManual.unlock();
        }
    }
    //Método para que un vehiculo entre a una cabina manual
    private void entraVehiculo(Vehiculo vehiculo){
        //Nos metemos a la lista de vehiculos de la cabina
        listaVehiculo.meterVehiculo(vehiculo);
        //Nos metemos como vehiculo a cobrar
        setVehiculoParaCobrar(vehiculo);
        //Una vez dentro del peaje, comprobaremos si el empleado está listo para cobrarnos
        if(isEmpleadoEsperando()){
            //Se verifica que el empleado está esperando a cobrarnos, por tanto le despertamos y nos iremos a dormir
            duermeEmpleado.signal();
            //Una vez que el empleado está despierto, nos iremos a dormir hasta que nos cobre el empleado
        }
        else{
            //No hay ningún empleado esperando, por tanto nos quedaremos a la espera de que nos despierte cuando nos cobre
            setVehiculoEsperando(true);
        }
        try{
            dormirVehiculo.await();
        }catch(InterruptedException ie){}
    }
    //Método para que un vehiculo entre a una cabina manual
    public void cobraVehiculo(Empleado empleado){
        try{
            cabinaManual.lock();
            //El empleado tarda entre 6 y 8 segundos
            //Escribimos por pantalla y en el log
            getLog().escribirEnLog("[" + getNombreCabina() + "]: El empleado " + empleado.getIdentificador() + " esta cobrando al vehiculo " + getVehiculoParaCobrar().getIdentificador());
            System.out.println("[" + getNombreCabina() + "]: El empleado " + empleado.getIdentificador() + " esta cobrando al vehiculo " + getVehiculoParaCobrar().getIdentificador());
            empleado.cobraVehiculo();
            getLog().escribirEnLog("[" + getNombreCabina() + "]: El empleado " + empleado.getIdentificador() + " ha cobrado al vehiculo " + getVehiculoParaCobrar().getIdentificador());
            System.out.println("[" + getNombreCabina() + "]: El empleado " + empleado.getIdentificador() + " ha cobrado al vehiculo " + getVehiculoParaCobrar().getIdentificador());
            //Una vez que hemos cobrado al vehiculo incrementamos en 1 los peajes cobrados del empleado y despertamos al vehiculo
            empleado.setPeajesCobrados(empleado.getPeajesCobrados() + 1);
            setVehiculoEsperando(false);
            dormirVehiculo.signal();
        }finally{
            cabinaManual.unlock();
        }
    }
    //Método para que un vehículo salga del peaje
    private void saleVehiculo(Vehiculo vehiculo){
        //Si vamos a salir es porque el empleado nos despertó para poder irnos, por tanto el vehiculo no está esperando
        listaVehiculo.sacarVehiculo(vehiculo);
        //Quitamos el vehiculo de la cabina
        setVehiculoParaCobrar(null);
        //Escribimos por pantalla y por el log
        getLog().escribirEnLog("[" + getNombreCabina() + "]: El vehiculo " + vehiculo.getIdentificador() + " ha salido de la cabina");
        System.out.println("[" + getNombreCabina() + "]: El vehiculo " + vehiculo.getIdentificador() + " ha salido de la cabina");
    }
    //Método para que el empleado salga de la cabina para descansar
    public void saleEmpleado(Empleado empleado){
        try{
            cabinaManual.lock();
            //En primer lugar, nos quitamos de la lista de empleados
            listaEmpleado.sacarEmpleado(empleado);
            //Como el empleado se va no estará esperando
            setEmpleadoEsperando(false);
            //Escribimos por pantalla y por el log
            getLog().escribirEnLog("El empleado " + empleado.getIdentificador() + " ha salido de la cabina " + getNombreCabina());
            System.out.println("El empleado " + empleado.getIdentificador() + " ha salido de la cabina " + getNombreCabina());
        }finally{
            cabinaManual.unlock();
        }
    }
    //Método get del empleado de la cabina
    public Empleado getEmpleado() {
        return empleado;
    }
    //Método set del empleado de la cabina
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    //Mñetodo get del booleano vehiculo esperando
    public boolean isVehiculoEsperando() {
        return vehiculoEsperando;
    }
    //Método set del booleano vehiculo esperando
    public void setVehiculoEsperando(boolean vehiculoEsperando) {
        this.vehiculoEsperando = vehiculoEsperando;
    }
    //Método get para el booleano empleadoEsperando
    public boolean isEmpleadoEsperando() {
        return empleadoEsperando;
    }
    //Método set para el booleano empleadoEsperando
    public void setEmpleadoEsperando(boolean empleadoEsperando) {
        this.empleadoEsperando = empleadoEsperando;
    }
    //Método get para el vehiculo a cobrar
    public Vehiculo getVehiculoParaCobrar() {
        return vehiculoParaCobrar;
    }
    //Método set para el vehiculo a cobrar
    public void setVehiculoParaCobrar(Vehiculo vehiculoParaCobrar) {
        this.vehiculoParaCobrar = vehiculoParaCobrar;
    }
    //Método get del log
    public Log getLog() {
        return log;
    }
}
