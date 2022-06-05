package xyz.campanita.poofinal.excepciones;

/**
* Indica que la operación no puede hacerse porque se sobrepasaría el límite de créditos para un usuario
*/
public class ExcepcionSaldoExcedente extends Exception {
  public ExcepcionSaldoExcedente(){
    super("Saldo excedente: solo puedes ingresar hasta 1 millón de pesos, o 10 millones de créditos.");
  }
}
