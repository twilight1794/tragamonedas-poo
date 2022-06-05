package xyz.campanita.poofinal.cliente;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import xyz.campanita.poofinal.excepciones.*;
import java.io.Serializable;

/**
* Representa un usuario en concreto
*/
public class Usuario implements Serializable {
  private String nombre;
  private byte[] hashContrasena;
  private boolean tipo; // true = administrador, false = jugador
  private Integer creditos;
  private MessageDigest d;

  /**
  * Constructor para <code>Usuario</code>
  * @param n Nombre de usuario
  * @param c Contraseña. Debe ser una cadena de, como mínimo, 4 caracteres, de los cuales al menos uno debe ser un dígito
  * @param t Tipo de usuario: <code>true</code> si el usuario es administrador, de lo contrario es <code>false</code>
  * @throws ExcepcionContrasenaInvalida si la contraseña proporcionada no cumple con el formato requerido
  */
  public Usuario(String n, String c, boolean t) throws ExcepcionContrasenaInvalida{
    this.tipo = t;
    this.nombre = n;
    this.restaurar();
    this.setContrasena(c);
    this.creditos = 0;
  }
  /**
  * Devuelve el nombre de este usuario
  */
  public String getNombre(){
    return this.nombre;
  }
  /**
  * Indica si la contraseña proporcionada es la de este usuario
  * @param c Contraseña a comprobar
  * @return <code>true</code> si la contraseña proporcionada coincide con la del usuario, en otro caso es <code>false</code>
  */
  public boolean esContrasena(String c){
    return Arrays.equals(this.d.digest(c.getBytes(StandardCharsets.UTF_8)), this.hashContrasena);
  }
  /**
  * Indica el tipo de cuenta de este usuario
  * @return <code>true</code> si el tipo de cuenta de este usuario es de administrador, <code>false</code> si es de usuario normal
  */
  public boolean getTipo(){
    return this.tipo;
  }
  /**
  * Indica la cantidad de créditos que posee este usuario
  */
  public int getCreditos(){
    return this.creditos;
  }

  /**
  * Establece el nombre de este usuario
  * @param n Nombre a establecer
  */
  public void setNombre(String n){
    this.nombre = n;
  }
  /**
  * Establece la contraseña de este usuario
  * @param c Contraseña a establecer
  * @throws ExcepcionContrasenaInvalida si la contraseña proporcionada no cumple con el formato requerido
  */
  public void setContrasena(String c) throws ExcepcionContrasenaInvalida{
    if (!c.matches(".*\\d.*") || c.length() < 4){
      throw new ExcepcionContrasenaInvalida();
    }
    hashContrasena = this.d.digest(c.getBytes(StandardCharsets.UTF_8));
  }
  /**
  * Aumenta el número de créditos disponibles para este usuario
  * @param c Créditos a sumar
  */
  public void aumentarCredito(int c){
    this.creditos += c;
  }
  /**
  * Disminuye el número de créditos disponibles para este usuario
  * @param c Créditos a restar
  * @throws ExcepcionSaldoInsuficiente si se intenta restar más créditos de los que hay disponibles
  */
  public void disminuirCredito(int c) throws ExcepcionSaldoInsuficiente{
    if (c>this.creditos) {
      throw new ExcepcionSaldoInsuficiente(c, this.creditos);
    } else {
      this.creditos -= c;
    }
  }
  /**
  * Elimina de la clase todos los valores que no necesitan ser guardados, y/o que no pueden ser serializados
  */
  public void limpiar(){
    this.d = null;
  }
  /**
  * Restaura en la clase todos los valores eliminados por <code>limpiar</code>
  */
  public void restaurar(){
    try {
      this.d = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e){} // Existe desde Java 7, no fallará
  }
}
