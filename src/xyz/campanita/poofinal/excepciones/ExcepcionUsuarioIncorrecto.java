package xyz.campanita.poofinal.excepciones;

/**
* Indica que no existe ningún usuario con el nombre especificado
*/
public class ExcepcionUsuarioIncorrecto extends Exception {
  /**
  * @param u Nombre de usuario introducido
  */
  public ExcepcionUsuarioIncorrecto(String u){
    super("El usuario "+u+" no existe.");
  }
}
