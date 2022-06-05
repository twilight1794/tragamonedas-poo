package xyz.campanita.poofinal.excepciones;

/**
* Indica que no hay saldo suficiente en la cuenta del usuario para realizar una operación
*/
public class ExcepcionSaldoInsuficiente extends Exception {
  /**
  * @param r Créditos a retirar
  * @param o Créditos disponibles
  */
  public ExcepcionSaldoInsuficiente(int r, int o){
    super("Saldo insuficiente: hace falta "+(o-r)+" créditos para realizar la operación.");
  }
}
