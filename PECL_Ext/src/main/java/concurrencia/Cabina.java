package concurrencia;

public class Cabina {
    //Atributos de la clase cabina
    private String nombreCabina = "";
    private boolean disponible = true;

    //Métodos de la clase cabina

    //Método constructor
    public Cabina(String _nombre){
        this.nombreCabina = _nombre;
    }
    //Método get para el nombre de la cabina
    public String getNombreCabina() {
        return nombreCabina;
    }
    //Método que devuelve un booleano de si la cabina está disponible para ocupar
    public boolean isDisponible() {
        return disponible;
    }
    //Método que cambia el booleano de si la cabina está disponible para ocupar
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
