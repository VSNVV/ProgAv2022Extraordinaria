package distribuida;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfacePeaje extends Remote {
    void cierraCabinaCoche4() throws RemoteException;
    void abreCabinaCoche4() throws RemoteException;
    void cierraCabinaCoche5() throws RemoteException;
    void abreCabinaCoche5() throws RemoteException;
    void cierraCabinaCoche6() throws RemoteException;
    void abreCabinaCoche6() throws RemoteException;
    void cierraCabinaCamion3() throws RemoteException;
    void abreCabinaCamion3() throws RemoteException;
    void cierraCabinaCamion4() throws RemoteException;
    void abreCabinaCamion4() throws RemoteException;
    String devuelveContenidoJTextFields(String cuadroDeTexto) throws RemoteException;
}