package xyz.campanita.poofinal.excepciones;

/**
* Indica que el monto a ingresar está por debajo del mínimo permitido
*/
public class ExcepcionIngresoInsuficiente extends Exception {
  public ExcepcionIngresoInsuficiente(){
    super("Ingreso insuficiente: el saldo mínimo a ingresar es de 20 pesos, o 200 créditos.");
  }
}
