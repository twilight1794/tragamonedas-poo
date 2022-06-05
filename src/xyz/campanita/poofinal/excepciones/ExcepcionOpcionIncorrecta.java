package xyz.campanita.poofinal.excepciones;

import java.util.InputMismatchException;

/**
* Indica que el usuario no ha escogido la opción correcta
*/
public class ExcepcionOpcionIncorrecta extends InputMismatchException {
  /**
  * @param n Opción introducida por el usuario
  * @param a Primera opción del intervalo válido
  * @param b Última opción del intervalo válido
  */
  public ExcepcionOpcionIncorrecta(int n, int a, int b){
    super("La opción "+n+" es incorrecta. Debe escribir un número del "+a+" al "+b+".");
  }
}
