package xyz.campanita.poofinal.excepciones;

/**
* Indica que no existe una sesión activa
*/
public class ExcepcionSesionUsuarioInexistente extends Exception {
  public ExcepcionSesionUsuarioInexistente(){
    super("No hay ningún usuario con una sesión activa.");
  }
}
