package xyz.campanita.poofinal.excepciones;

/**
* Indica que ya existe una sesión activa
*/
public class ExcepcionSesionUsuarioExistente extends Exception {
  /**
  * @param n Nombre de usuario
  */
  public ExcepcionSesionUsuarioExistente(String n){
    super("El usuario "+n+" ya inició sesión en el programa.");
  }
}
