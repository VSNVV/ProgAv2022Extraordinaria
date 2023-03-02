package distribuida;

import concurrencia.Peaje;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class GestorInterface extends UnicastRemoteObject implements InterfacePeaje {
    //Atributos de la clase GestorInterface
    private Peaje peaje;

    //Método constructor
    public GestorInterface(Peaje _peaje) throws RemoteException {
        this.peaje = _peaje;
    }
    //Método para cerrar la cabina de coches 4
    public void cierraCabinaCoche4(){
        peaje.cierraCabinas("Coches4");
    }
    //Método para abrir la cabina de coches 4
    public void abreCabinaCoche4(){
        peaje.abreCabinas("Coches4");
    }
    //Método para cerrar la cabina de coches 5
    public void cierraCabinaCoche5(){
        peaje.cierraCabinas("Coches5");
    }
    //Método para abrir la cabina de coches 5
    public void abreCabinaCoche5(){
        peaje.abreCabinas("Coches5");
    }
    //Método para cerrar la cabina de coches 6
    public void cierraCabinaCoche6(){
        peaje.cierraCabinas("Coches6");
    }
    //Método para abrir la cabina de coches 6
    public void abreCabinaCoche6(){
        peaje.abreCabinas("Coches6");
    }
    //Método para cerrar la cabina de camiones 3
    public void cierraCabinaCamion3(){
        peaje.cierraCabinas("Camiones3");
    }
    //Método para abrir la cabina de camiones 3
    public void abreCabinaCamion3(){
        peaje.abreCabinas("Camiones3");
    }
    //Método para cerrar la cabina de camiones 4
    public void cierraCabinaCamion4(){
        peaje.cierraCabinas("Camiones4");
    }
    //Método para abrir la cabina de camiones 4
    public void abreCabinaCamion4(){
        peaje.abreCabinas("Camiones4");
    }
    //Método que devuelve el contenido de un JTextField concreto del servidor para que aparezca en el cliente
    public String devuelveContenidoJTextFields(String cuadroDeTexto){
        String contenido = "";
        if(cuadroDeTexto.equals("colaDeEsperaPeaje")){
            //Devuelve el contenido de la cola de espera del peaje
            contenido = peaje.getColaEsperaPeaje().getText();
        }
        else if(cuadroDeTexto.equals("vehiculoCabinaCoche1")){
            contenido = peaje.getVehiculoCabinaCoche1().getText();
        }
        else if(cuadroDeTexto.equals("vehiculoCabinaCoche2")){
            contenido = peaje.getVehiculoCabinaCoche2().getText();
        }
        else if(cuadroDeTexto.equals("vehiculoCabinaCoche3")){
            contenido = peaje.getVehiculoCabinaCoche3().getText();
        }
        else if(cuadroDeTexto.equals("vehiculoCabinaCoche4")){
            contenido = peaje.getVehiculoCabinaCoche4().getText();
        }
        else if(cuadroDeTexto.equals("vehiculoCabinaCoche5")){
            contenido = peaje.getVehiculoCabinaCoche5().getText();
        }
        else if(cuadroDeTexto.equals("vehiculoCabinaCoche6")){
            contenido = peaje.getVehiculoCabinaCoche6().getText();
        }
        else if(cuadroDeTexto.equals("vehiculoCabinaCamion1")){
            contenido = peaje.getVehiculoCabinaCamion1().getText();
        }
        else if(cuadroDeTexto.equals("vehiculoCabinaCamion2")){
            contenido = peaje.getVehiculoCabinaCamion2().getText();
        }
        else if(cuadroDeTexto.equals("vehiculoCabinaCamion3")){
            contenido = peaje.getVehiculoCabinaCamion3().getText();
        }
        else if(cuadroDeTexto.equals("vehiculoCabinaCamion4")){
            contenido = peaje.getVehiculoCabinaCamion4().getText();
        }
        else if(cuadroDeTexto.equals("empleadoCabinaCoche1")){
            contenido = peaje.getEmpleadoCabinaCoche1().getText();
        }
        else if(cuadroDeTexto.equals("empleadoCabinaCoche2")){
            contenido = peaje.getEmpleadoCabinaCoche2().getText();
        }
        else if(cuadroDeTexto.equals("empleadoCabinaCoche3")){
            contenido = peaje.getEmpleadoCabinaCoche3().getText();
        }
        else if(cuadroDeTexto.equals("empleadoCabinaCamion1")){
            contenido = peaje.getEmpleadoCabinaCamion1().getText();
        }
        else if(cuadroDeTexto.equals("empleadoCabinaCamion2")){
            contenido = peaje.getEmpleadoCabinaCamion2().getText();
        }
        return contenido;
    }
}
