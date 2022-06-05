package xyz.campanita.poofinal.excepciones;

/**
* Indica que la contraseña especificada es incorrecta
*/
public class ExcepcionContrasenaIncorrecta extends Exception {
  public ExcepcionContrasenaIncorrecta(){
    super("La contraseña especificada es incorrecta.");
  }
}
