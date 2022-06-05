package xyz.campanita.poofinal.excepciones;

/**
* Indica que la contraseña especificada no cumple con los criterios mínimos de seguridad
*/
public class ExcepcionContrasenaInvalida extends Exception {
  public ExcepcionContrasenaInvalida(){
    super("La contraseña debe tener mínimo 4 caracteres y tener al menos un número.");
  }
}
