package xyz.campanita.poofinal.excepciones;

/**
* Indica que el usuario que se intenta modificar es el mismo que ya está en sesión
*/
public class ExcepcionPropioUsuario extends Exception {
  public ExcepcionPropioUsuario(){
    super("No puedes modificar a tu propio usuario aquí.");
  }
}
