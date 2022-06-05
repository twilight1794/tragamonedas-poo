package xyz.campanita.poofinal.excepciones;

/**
* Indica que ya existe un usuario con el nombre especificado
*/
public class ExcepcionUsuarioExistente extends Exception {
  /**
  * @param u Nombre de usuario existente
  */
  public ExcepcionUsuarioExistente(String u){
    super("El usuario "+u+" ya existe.");
  }
}
