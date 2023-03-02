import concurrencia.Vehiculo;

import java.util.ArrayList;
import java.util.Random;

public class pruebas{
    public static void main(String[] args){
        ArrayList al = new ArrayList();
        al.add(0, "Coche1");
        al.add(0, "Coche2");
        al.add(0, "Coche3");
        al.add(0, "Coche4");
        for(int i = 0; i < al.size(); i++){
            System.out.print(al.get(i) + " ");
        }
        System.out.println("");
        al.add(0, "Ambulancia1");
        for(int i = 0; i < al.size(); i++){
            System.out.print(al.get(i) + " ");
        }
    }
}
