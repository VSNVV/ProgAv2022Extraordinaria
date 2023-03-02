package concurrencia;
import java.util.*;
import javax.swing.JTextField;

/* La clase ListaThreads permite gestionar las listas de threads en los monitores,
con métodos para meter y sacar threads en ella. Cada vez que una lista se modifica,
se imprime su nuevo contenido en el JTextField que toma como parámetro el constructor. */
public class ListaThreads{
    private ArrayList<Vehiculo> listaVehiculos;
    private ArrayList<Empleado> listaEmpleados;
    private JTextField tf;
    
    public ListaThreads(JTextField tf){
        listaVehiculos = new ArrayList<Vehiculo>();
        listaEmpleados = new ArrayList<Empleado>();
        this.tf = tf;
    }
    
    public synchronized void meterVehiculo(Vehiculo vehiculo){
        listaVehiculos.add(vehiculo);
        imprimirVehiculo();
    }

    public synchronized void meterEmpleado(Empleado empleado){
        listaEmpleados.add(empleado);
        imprimirEmpleado();
    }
    
    public synchronized void sacarVehiculo(Vehiculo vehiculo){
        listaVehiculos.remove(vehiculo);
        imprimirVehiculo();
    }

    public synchronized void sacarEmpleado(Empleado empleado){
        listaEmpleados.remove(empleado);
        imprimirEmpleado();
    }

    public synchronized void meterAmbulancia(Vehiculo vehiculo, int posicion){
        //Antes de meter a la ambulancia, tenemos que revisar cuantos elementos tiene el array listaVehiculos
        if(listaVehiculos.size() == posicion){
            //Se verifica que solo hay ambulancias en la lista de espera, por tanto no tendremos en cuanta la posicion
            //y simplemene añadimos la ambulancia sin indicar su posicion
            listaVehiculos.add(vehiculo);
        }
        else{
            //Se verifica que no solo hay ambulancias esperando, por tanto añadimos a la ambulancia detrás de la última
            //ambulancia en entrar
            listaVehiculos.add(posicion, vehiculo);
        }
        imprimirVehiculo();
    }

    private void imprimirVehiculo() {
        String contenido="";
        for(int i=0; i<listaVehiculos.size(); i++)
        {
            contenido=contenido+listaVehiculos.get(i).getIdentificador()+" ";
        }
        tf.setText(contenido);
    }

    private void imprimirEmpleado() {
        String contenido="";
        for(int i=0; i<listaEmpleados.size(); i++)
        {
            contenido=contenido+listaEmpleados.get(i).getIdentificador()+" ";
        }
        tf.setText(contenido);
    }
}