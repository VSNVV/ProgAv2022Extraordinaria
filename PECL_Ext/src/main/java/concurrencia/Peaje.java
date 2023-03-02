package concurrencia;

import log.Log;

import javax.swing.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Peaje{
    //Atributos de la clase peaje
    private CabinaManual cabinaCoches1, cabinaCoches2, cabinaCoches3, cabinaCamiones1, cabinaCamiones2;
    private CabinaAutomatica cabinaCoches4, cabinaCoches5, cabinaCoches6, cabinaCamiones3, cabinaCamiones4;
    private int ambulanciasEsperando = 0;
    private Lock colaEspera = new ReentrantLock();
    private Lock entradaEmpleados = new ReentrantLock();
    private Condition cocheEspera = colaEspera.newCondition();
    private Condition camionEspera = colaEspera.newCondition();
    private Condition ambulanciaEspera = colaEspera.newCondition();
    private ListaThreads listaEsperaPeaje;
    private Log log;
    private JTextField colaEsperaPeaje;
    private JTextField vehiculoCabinaCoche1, vehiculoCabinaCoche2, vehiculoCabinaCoche3, vehiculoCabinaCoche4, vehiculoCabinaCoche5, vehiculoCabinaCoche6, vehiculoCabinaCamion1, vehiculoCabinaCamion2, vehiculoCabinaCamion3, vehiculoCabinaCamion4;
    private JTextField empleadoCabinaCoche1, empleadoCabinaCoche2, empleadoCabinaCoche3, empleadoCabinaCamion1, empleadoCabinaCamion2;
    //Métodos de la clase Peaje

    //Método constructor
    public Peaje(JTextField colaEntrada,
                 JTextField empleadoCabinaCoche1, JTextField vehiculoCabinaCoche1,
                 JTextField empleadoCabinaCoche2, JTextField vehiculoCabinaCoche2,
                 JTextField empleadoCabinaCoche3, JTextField vehiculoCabinaCoche3,
                 JTextField vehiculoCabinaCoche4, JTextField vehiculoCabinaCoche5,
                 JTextField vehiculoCabinaCoche6,
                 JTextField empleadoCabinaCamion1, JTextField vehiculoCabinaCamion1,
                 JTextField empleadoCabinaCamion2, JTextField vehiculoCabinaCamion2,
                 JTextField vehiculoCabinaCamion3, JTextField vehiculoCabinaCamion4, Log log){
        //Creamos todas las cabinas
        this.listaEsperaPeaje = new ListaThreads(colaEntrada);
        this.cabinaCoches1 = new CabinaManual("CabinaCohes1", empleadoCabinaCoche1, vehiculoCabinaCoche1, log);
        this.cabinaCoches2 = new CabinaManual("CabinaCoches2", empleadoCabinaCoche2, vehiculoCabinaCoche2, log);
        this.cabinaCoches3 = new CabinaManual("CabinaCoches3", empleadoCabinaCoche3, vehiculoCabinaCoche3, log);
        this.cabinaCoches4 = new CabinaAutomatica("CabinaCoches4", vehiculoCabinaCoche4, log);
        this.cabinaCoches5 = new CabinaAutomatica("CabinaCoches5", vehiculoCabinaCoche5, log);
        this.cabinaCoches6 = new CabinaAutomatica("CabinaCoches6", vehiculoCabinaCoche6, log);
        this.cabinaCamiones1 = new CabinaManual("CabinaCamiones1", empleadoCabinaCamion1, vehiculoCabinaCamion1, log);
        this.cabinaCamiones2 = new CabinaManual("CabinaCamiones2", empleadoCabinaCamion2, vehiculoCabinaCamion2, log);
        this.cabinaCamiones3 = new CabinaAutomatica("CabinaCamiones3", vehiculoCabinaCamion3, log);
        this.cabinaCamiones4 = new CabinaAutomatica("CabinaCamiones4", vehiculoCabinaCamion4, log);
        //Asignamos todos los JTextField para que el cliente pueda revisarlos
        //JTextField de vehiculos
        this.vehiculoCabinaCoche1 = vehiculoCabinaCoche1;
        this.vehiculoCabinaCoche2 = vehiculoCabinaCoche2;
        this.vehiculoCabinaCoche3 = vehiculoCabinaCoche3;
        this.vehiculoCabinaCoche4 = vehiculoCabinaCoche4;
        this.vehiculoCabinaCoche5 = vehiculoCabinaCoche5;
        this.vehiculoCabinaCoche6 = vehiculoCabinaCoche6;
        this.vehiculoCabinaCamion1 = vehiculoCabinaCamion1;
        this.vehiculoCabinaCamion2 = vehiculoCabinaCamion2;
        this.vehiculoCabinaCamion3 = vehiculoCabinaCamion3;
        this.vehiculoCabinaCamion4 = vehiculoCabinaCamion4;
        //JTextField de empleados
        this.empleadoCabinaCoche1 = empleadoCabinaCoche1;
        this.empleadoCabinaCoche2 = empleadoCabinaCoche2;
        this.empleadoCabinaCoche3 = empleadoCabinaCoche3;
        this.empleadoCabinaCamion1 = empleadoCabinaCamion1;
        this.empleadoCabinaCamion2 = empleadoCabinaCamion2;
        //JTextField de la cola de espera
        this.colaEsperaPeaje = colaEntrada;
        //Asignacion del Log
        this.log = log;
    }
    //Método para entrar al peaje
    public void entraPeajeVehiculo(Vehiculo vehiculo) {
        try{
            colaEspera.lock();
            //En primer lugar, veremos de que tipo es el vehiculo que va a entrar
            if(vehiculo.getTipo().equals("Coche") || vehiculo.getTipo().equals("Ambulancia")){
                //Se verifica que le coche que quiere entrar es una ambulancia o un coche
                //Comprobaremos si hay una cabina libre, y si no, haremos cola
                if(!cabinaCocheDisponible()){
                    if(vehiculo.getTipo().equals("Coche")){
                        //El coche esperará
                        listaEsperaPeaje.meterVehiculo(vehiculo);
                        try{
                            cocheEspera.await();
                        }catch(InterruptedException ie){}
                        listaEsperaPeaje.sacarVehiculo(vehiculo);
                    }
                    else{
                        //La ambulancia esperará
                        listaEsperaPeaje.meterAmbulancia(vehiculo, getAmbulanciasEsperando());
                        setAmbulanciasEsperando(getAmbulanciasEsperando() + 1);
                        try{
                            ambulanciaEspera.await();
                        }catch(InterruptedException ie){}
                        setAmbulanciasEsperando(getAmbulanciasEsperando() - 1);
                        listaEsperaPeaje.sacarVehiculo(vehiculo);
                    }
                }
                vehiculo.setCabinaManual(cabinaManualCocheDisponible());
                if(vehiculo.getCabinaManual() == null){
                    vehiculo.setCabinaAutomatica(cabinaAutomaticaCocheDisponible());
                }
            }
            else{
                //Se veriufica que el vehiculo que quiere entrar es un camion
                //Comprobaremos si hay una cabina libre para camiones, y si no, haremos cola
                if(!cabinaCamionDisponible()){
                    //El camión tendrá que esperar ya que no hay ninguna cabina disponible
                    listaEsperaPeaje.meterVehiculo(vehiculo);
                    try{
                        camionEspera.await();
                    }catch(InterruptedException ie){}
                    listaEsperaPeaje.sacarVehiculo(vehiculo);
                }
                //En caso de que haya alguna cabina disponible, nos la llevaremos
                vehiculo.setCabinaManual(cabinaManualCamionDisponible());
                if(vehiculo.getCabinaManual() == null){
                    vehiculo.setCabinaAutomatica(cabinaAutomaticaCamionDisponible());
                }
            }

        }finally{
            colaEspera.unlock();
        }
    }
    //Método que devuelve si hay una cabina de coche disponible o no
    private boolean cabinaCocheDisponible() {
        //Comprobamos las cabinas manuales en primer lugar
        if ((getCabinaCoches1().isDisponible() || getCabinaCoches2().isDisponible()) || getCabinaCoches3().isDisponible()) {
            //Tenemos cabinas manuales disponibles, por tanto devolvemos un true
            return true;
        }
        //Como no hay cabinas manuales disponibles, miraremos si hay automáticas
        else return getCabinaCoches4().acceso() || getCabinaCoches5().acceso() || getCabinaCoches6().acceso();
    }
    //Método que devuelve si hay una cabina de camion disponible o no
    private boolean cabinaCamionDisponible(){
        //Comprobamos las cabinas manuales en primer lugar
        if (getCabinaCamiones1().isDisponible() || getCabinaCamiones2().isDisponible()){
            return true;
        }
        //Como no hay cabinas manuales disponibles, miraremos si hay automáticas
        else return getCabinaCamiones3().acceso() || getCabinaCamiones4().acceso();
    }
    //Método que asigna una cabina manual a un coche
    private CabinaManual cabinaManualCocheDisponible(){
        CabinaManual cabinaDisponible = null;
        if(getCabinaCoches1().isDisponible()){
            //Como ese vehículo va a ocupar la cabina, la cabina no estará disponible
            getCabinaCoches1().setDisponible(false);
            cabinaDisponible = getCabinaCoches1();
        }
        else if(getCabinaCoches2().isDisponible()){
            //Como ese vehículo va a ocupar la cabina, la cabina no estará disponible
            getCabinaCoches2().setDisponible(false);
            cabinaDisponible = getCabinaCoches2();
        }
        else if(getCabinaCoches3().isDisponible()){
            //Como ese vehículo va a ocupar la cabina, la cabina no estará disponible
            getCabinaCoches3().setDisponible(false);
            cabinaDisponible = getCabinaCoches3();
        }
        return cabinaDisponible;
    }
    //Método que asigna una cabina automática a un coche
    private CabinaAutomatica cabinaAutomaticaCocheDisponible(){
        CabinaAutomatica cabinaDisponible = null;
        if(getCabinaCoches4().isDisponible() && getCabinaCoches4().isAbierto()){
            //Ocupamos la cabina
            //cabinaCoches4.setDisponible(false);
            //Ahora tendremos que ver si está abierta para ver si tenemos que esperar a que se abra o no
            //Como la cabina está abierta, la asignamos al vehiculo para que pueda entrar
            getCabinaCoches4().setDisponible(false);
            cabinaDisponible = getCabinaCoches4();
        }
        else if(getCabinaCoches5().isDisponible() && getCabinaCoches5().isAbierto()){
            //Ocupamos la cabina
            //getCabinaCoches5().setDisponible(false);
            //Ahora tendremos que ver si está abierta para ver si tenemos que esperar a que se abra o no
            getCabinaCoches5().setDisponible(false);
            cabinaDisponible = getCabinaCoches5();
        }
        else if(getCabinaCoches6().isDisponible() && getCabinaCoches6().isAbierto()){
            getCabinaCoches6().setDisponible(false);
            cabinaDisponible = getCabinaCoches6();
        }
        return cabinaDisponible;
    }
    //Método que asigna una cabina manual a un camion
    private CabinaManual cabinaManualCamionDisponible(){
        CabinaManual cabinaDisponible = null;
        if(getCabinaCamiones1().isDisponible()){
            //Como ese vehículo va a ocupar la cabina, la cabina no estará disponible
            getCabinaCamiones1().setDisponible(false);
            cabinaDisponible = getCabinaCamiones1();
        }
        else if(getCabinaCamiones2().isDisponible()){
            //Como ese vehículo va a ocupar la cabina, la cabina no estará disponible
            getCabinaCamiones2().setDisponible(false);
            cabinaDisponible = getCabinaCamiones2();
        }
        return cabinaDisponible;
    }
    //Método que asigna una cabina automática a un camion
    private CabinaAutomatica cabinaAutomaticaCamionDisponible(){
        CabinaAutomatica cabinaDisponible = null;
        if(getCabinaCamiones3().isDisponible() && getCabinaCamiones3().isAbierto()){
            //Como la cabina está abierta, la asignamos al vehiculo para que pueda entrar
            getCabinaCamiones3().setDisponible(false);
            cabinaDisponible = getCabinaCamiones3();
        }
        else if(getCabinaCamiones4().isDisponible() && getCabinaCamiones4().isAbierto()){
            //Como la cabina está abierta, la asignamos al vehiculo para que pueda entrar
            getCabinaCamiones4().setDisponible(false);
            cabinaDisponible = getCabinaCamiones4();
        }
        return cabinaDisponible;
    }
    //Método para que los empleados entren al peaje, y acto seguido a la cabina
    public CabinaManual entradaEmpleados(Empleado empleado){
        try{
            entradaEmpleados.lock();

            CabinaManual cabinaDisponible = null;
            if(getCabinaCoches1().getEmpleado() == null){
                cabinaDisponible = getCabinaCoches1();
                getCabinaCoches1().setEmpleado(empleado);
            }
            else if(getCabinaCoches2().getEmpleado() == null){
                cabinaDisponible = getCabinaCoches2();
                getCabinaCoches2().setEmpleado(empleado);
            }
            else if(getCabinaCoches3().getEmpleado() == null){
                cabinaDisponible = getCabinaCoches3();
                getCabinaCoches3().setEmpleado(empleado);
            }
            else if(getCabinaCamiones1().getEmpleado() == null){
                cabinaDisponible = getCabinaCamiones1();
                getCabinaCamiones1().setEmpleado(empleado);
            }
            else if(getCabinaCamiones2().getEmpleado() == null){
                cabinaDisponible = getCabinaCamiones2();
                getCabinaCamiones2().setEmpleado(empleado);
            }
            return cabinaDisponible;

        }finally{
            entradaEmpleados.unlock();
        }
    }
    //Método que usará el cliente de peaje para que cuando se abra una cabina, haga un signal por si hay vehiculos
    //que no pudieron entrar
    public void abreCabinas(String cabina){
        //Dependiendo del string, despertaremos a los coches de una cabina u otra
        try{
            colaEspera.lock();
            switch (cabina) {
                case "Coches4":
                    //Abrimos la cabina
                    getCabinaCoches4().setAbierto(true);
                    //Despertaremos a un coche de la cola en el caso de que la cabina esté disponible
                    if (getCabinaCoches4().isDisponible()) {
                        //Si hay ambulancias esperando, despertaremos a las ambulancias
                        if (getAmbulanciasEsperando() > 0) {
                            ambulanciaEspera.signal();
                        }else {
                            //Si no hay ambulancias esperando despertaremos a un coche
                            cocheEspera.signal();
                        }
                    }
                    break;
                case "Coches5":
                    getCabinaCoches5().setAbierto(true);
                    if (getCabinaCoches5().isDisponible()) {
                        if (getAmbulanciasEsperando() > 0) {
                            ambulanciaEspera.signal();
                        } else {
                            //Si no hay ambulancias esperando despertaremos a un coche
                            cocheEspera.signal();
                        }
                    }
                    break;
                case "Coches6":
                    getCabinaCoches6().setAbierto(true);
                    if (getCabinaCoches6().isDisponible()) {
                        if (getAmbulanciasEsperando() > 0) {
                            ambulanciaEspera.signal();
                        }else {
                            //Si no hay ambulancias esperando despertaremos a un coche
                            cocheEspera.signal();
                        }
                    }
                    break;
                case "Camiones3":
                    getCabinaCamiones3().setAbierto(true);
                    if (getCabinaCamiones3().isDisponible()) {
                        camionEspera.signal();
                    }
                    break;
                case "Camiones4":
                    getCabinaCamiones4().setAbierto(true);
                    if (getCabinaCamiones4().isDisponible()) {
                        camionEspera.signal();
                    }
                    break;
            }
        }finally{
            colaEspera.unlock();
        }
    }
    //Método para cerrar las cabinas
    public void cierraCabinas(String cabina){
        try{
            colaEspera.lock();
            switch (cabina) {
                case "Coches4":
                    getCabinaCoches4().setAbierto(false);
                    break;
                case "Coches5":
                    getCabinaCoches5().setAbierto(false);
                    break;
                case "Coches6":
                    getCabinaCoches6().setAbierto(false);
                    break;
                case "Camiones3":
                    getCabinaCamiones3().setAbierto(false);
                    break;
                case "Camiones4":
                    getCabinaCamiones4().setAbierto(false);
                    break;
            }
        }finally{
            colaEspera.unlock();
        }
    }
    //Método para que los vehiculos puedan salir del peaje
    public void saleVehiculo(Vehiculo vehiculo){
        try{
            colaEspera.lock();
            //Comprobamos el tipo de vehiculo que quiere salir
            if(vehiculo.getTipo().equals("Coche") || vehiculo.getTipo().equals("Ambulancia")){
                //Primero tenemos que ver de que cabina viene el coche
                if(vehiculo.getCabinaManual() != null){
                    //El vehículo viene de una cabina manual, que siempre están abiertas, por tanto podemos despertar al siguiente vehiculo sin problema
                    vehiculo.getCabinaManual().setDisponible(true);
                    //Despertamos al vehiculo
                    if(getAmbulanciasEsperando() > 0){
                        //Hay ambulancias esperando, por tanto despertaremos a una ambulancia
                        ambulanciaEspera.signal();
                    }
                    else{
                        //Como no hay ambulancias esperando, despertamos a un coche
                        cocheEspera.signal();
                    }
                }
                else{
                    //Tiene cabina automática
                    vehiculo.getCabinaAutomatica().setDisponible(true);
                    //En caso de que la cabina de la que venimos esté abierta (porque disponible ya está) despertaremos al siguiente vehiculo
                    if(vehiculo.getCabinaAutomatica().isAbierto()){
                        if(getAmbulanciasEsperando() > 0){
                            //Hay ambulancias esperando, por tanto despertaremos a una ambulancia
                            ambulanciaEspera.signal();
                        }
                        else{
                            //Como no hay ambulancias esperando, despertamos a un coche
                            cocheEspera.signal();
                        }
                    }
                }
            }
            else{
                //Verificamos que es un camion
                if(vehiculo.getCabinaManual() != null){
                    //Viene de una cabina manual, por tanto la pondrá disponible y despertará al siguiente vehiculo
                    vehiculo.getCabinaManual().setDisponible(true);
                    camionEspera.signal();
                }
                else{
                    //Viene de una cabina automática, por tanto, avisaremos de que hay una plaza libre
                    vehiculo.getCabinaAutomatica().setDisponible(true);
                    //El camion solo despertará al siguiente si, y solo si, la cabina esta tanto abierta como disponible
                    if(vehiculo.getCabinaAutomatica().isAbierto()){
                        camionEspera.signal();
                    }
                }
                //Una vez abierta la cabina para los demas camiones, despertaremos al siguiente camion, ya que las ambulancias
                //no utilizan estas cabinas
            }
            getLog().escribirEnLog("[PEAJE]: El vehiculo " + vehiculo.getIdentificador() + " ha abandonado el peaje");
            System.out.println("[PEAJE]: El vehiculo " + vehiculo.getIdentificador() + " ha abandonado el peaje");
        }finally{
            colaEspera.unlock();
        }
    }
    //Método get de la cabina de coches 1
    public CabinaManual getCabinaCoches1() {
        return cabinaCoches1;
    }
    //Método get de la cabina de coches 2
    public CabinaManual getCabinaCoches2() {
        return cabinaCoches2;
    }
    //Método get de la cabina de coches 3
    public CabinaManual getCabinaCoches3() {
        return cabinaCoches3;
    }
    //Método get de la cabina de camiones 1
    public CabinaManual getCabinaCamiones1() {
        return cabinaCamiones1;
    }
    //Método get de la cabina de camiones 2
    public CabinaManual getCabinaCamiones2() {
        return cabinaCamiones2;
    }

    //Método get de la cabina de coches 4
    public CabinaAutomatica getCabinaCoches4() {
        return cabinaCoches4;
    }
    //Método get de la cabina de coches 5
    public CabinaAutomatica getCabinaCoches5() {
        return cabinaCoches5;
    }
    //Método get de la cabina de coches 6
    public CabinaAutomatica getCabinaCoches6() {
        return cabinaCoches6;
    }
    //Método get de la cabina de camiones 3
    public CabinaAutomatica getCabinaCamiones3() {
        return cabinaCamiones3;
    }
    //Método get de la cabina de camiones 4
    public CabinaAutomatica getCabinaCamiones4() {
        return cabinaCamiones4;
    }
    //Método get para el numero de ambulancias esperando en la cola
    public int getAmbulanciasEsperando() {
        return ambulanciasEsperando;
    }
    //Método set para el número de ambulancias esperando en la cola
    public void setAmbulanciasEsperando(int ambulanciasEsperando) {
        this.ambulanciasEsperando = ambulanciasEsperando;
    }

    //Métodos get de todos los JTextField

    public JTextField getColaEsperaPeaje() {
        return colaEsperaPeaje;
    }

    public JTextField getVehiculoCabinaCoche1() {
        return vehiculoCabinaCoche1;
    }

    public JTextField getVehiculoCabinaCoche2() {
        return vehiculoCabinaCoche2;
    }

    public JTextField getVehiculoCabinaCoche3() {
        return vehiculoCabinaCoche3;
    }

    public JTextField getVehiculoCabinaCoche4() {
        return vehiculoCabinaCoche4;
    }

    public JTextField getVehiculoCabinaCoche5() {
        return vehiculoCabinaCoche5;
    }

    public JTextField getVehiculoCabinaCoche6() {
        return vehiculoCabinaCoche6;
    }

    public JTextField getVehiculoCabinaCamion1() {
        return vehiculoCabinaCamion1;
    }

    public JTextField getVehiculoCabinaCamion2() {
        return vehiculoCabinaCamion2;
    }

    public JTextField getVehiculoCabinaCamion3() {
        return vehiculoCabinaCamion3;
    }

    public JTextField getVehiculoCabinaCamion4() {
        return vehiculoCabinaCamion4;
    }

    public JTextField getEmpleadoCabinaCoche1() {
        return empleadoCabinaCoche1;
    }

    public JTextField getEmpleadoCabinaCoche2() {
        return empleadoCabinaCoche2;
    }

    public JTextField getEmpleadoCabinaCoche3() {
        return empleadoCabinaCoche3;
    }

    public JTextField getEmpleadoCabinaCamion1() {
        return empleadoCabinaCamion1;
    }

    public JTextField getEmpleadoCabinaCamion2() {
        return empleadoCabinaCamion2;
    }

    public Log getLog() {
        return log;
    }
}


