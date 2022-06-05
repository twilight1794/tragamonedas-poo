package xyz.campanita.poofinal.cliente;

import xyz.campanita.poofinal.excepciones.*;
import xyz.campanita.poofinal.juego.Juego;
import xyz.campanita.poofinal.cliente.Usuario;
import java.util.ArrayList;
import java.io.Serializable;

/**
* Almacena todos los datos de los usuarios, y de la sesión actual. En otras palabras, representa el estado del programa
*/
public class Sesion implements Serializable {
  private Usuario usuarioActual;
  private ArrayList<Usuario> usuarios;

  /**
  * Constructor para <code>Sesion</code>
  */
  public Sesion(){
    this.usuarios = new ArrayList<Usuario>();
  }

  /**
  * Indica si un usuario ya inició sesión en el sistema
  * @return <code>true</code> si lo anterior es cierto, <code>false</code> si no
  */
  public boolean existeSesion(){
    if (this.usuarioActual == null) return false;
    return true;
  }
  /**
  * Devuelve una lista de usuarios
  * @return Una lista de cadenas de caracteres con los nombres de todos los usuarios registrados en el programa
  */
  public ArrayList<String> getListaUsuarios(){
    ArrayList<String> a = new ArrayList<String>();
    for (Usuario u: this.usuarios){
      a.add(u.getNombre());
    }
    return a;
  }
  /**
  * Indica el número de usuarios registrados en el programa
  */
  public int numeroUsuarios(){
    return this.usuarios.size();
  }
  /**
  * Indica el nombre del usuario cuya sesión está abierta
  * @throws ExcepcionSesionUsuarioInexistente si no hay alguna sesión abierta
  * @see xyz.campanita.poofinal.cliente.Usuario#getNombre
  */
  public String nombreUsuario() throws ExcepcionSesionUsuarioInexistente{
    if (this.usuarioActual == null){
      throw new ExcepcionSesionUsuarioInexistente();
    } else {
      return this.usuarioActual.getNombre();
    }
  }
  /**
  * Indica el tipo del usuario cuya sesión está abierta
  * @throws ExcepcionSesionUsuarioInexistente si no hay alguna sesión abierta
  * @return <code>true</code> si el usuario es un administrador, de lo contrario, <code>false</code>
  * @see xyz.campanita.poofinal.cliente.Usuario#getTipo
  */
  public boolean tipoUsuario() throws ExcepcionSesionUsuarioInexistente{
    if (this.usuarioActual == null){
      throw new ExcepcionSesionUsuarioInexistente();
    } else {
      return this.usuarioActual.getTipo();
    }
  }
  /**
  * Indica el número de créditos del usuario cuya sesión está abierta
  * @throws ExcepcionSesionUsuarioInexistente si no hay alguna sesión abierta
  * @see xyz.campanita.poofinal.cliente.Usuario#getCreditos
  */
  public int creditosUsuario() throws ExcepcionSesionUsuarioInexistente{
    if (this.usuarioActual == null){
      throw new ExcepcionSesionUsuarioInexistente();
    } else {
      return this.usuarioActual.getCreditos();
    }
  }
  /**
  * Cambia la contraseña del usuario cuya sesión está abierta
  * @param cv Contraseña a cambiar
  * @param cn Contraseña nueva
  * @throws ExcepcionSesionUsuarioInexistente si no hay alguna sesión abierta
  * @throws ExcepcionContrasenaIncorrecta si la contraseña proporcionada para autentificarse es incorrecta
  * @throws ExcepcionContrasenaInvalida si la contraseña nueva no cumple con los requisitos de seguridad
  * @see xyz.campanita.poofinal.cliente.Usuario#setContrasena
  */
  public void cambiarContrasena(String cv, String cn) throws ExcepcionSesionUsuarioInexistente, ExcepcionContrasenaIncorrecta, ExcepcionContrasenaInvalida{
    if (this.usuarioActual == null){
      throw new ExcepcionSesionUsuarioInexistente();
    } else {
      if (this.usuarioActual.esContrasena(cv) == false){
        throw new ExcepcionContrasenaIncorrecta();
      } else {
        this.usuarioActual.setContrasena(cn);
      }
    }
  }
  /**
  * Aumenta el saldo del usuario cuya sesión está abierta, según el precio especificado
  * @param c Precio equivalente en pesos
  * @throws ExcepcionSesionUsuarioInexistente si no hay alguna sesión abierta
  * @throws ExcepcionIngresoInsuficiente si el precio a pagar es menor al mínimo de 20 pesos
  * @throws ExcepcionSaldoExcedente si la cantidad de crédito resultante es mayor al límite de créditos
  * @see xyz.campanita.poofinal.cliente.Usuario#aumentarCredito
  */
  public void depositarDinero(int c) throws ExcepcionSesionUsuarioInexistente, ExcepcionIngresoInsuficiente, ExcepcionSaldoExcedente{
    if (this.usuarioActual == null){
      throw new ExcepcionSesionUsuarioInexistente();
    } else {
      if (c < 20){
        throw new ExcepcionIngresoInsuficiente();
      } else if (c*10+this.usuarioActual.getCreditos() > 10000000){
        throw new ExcepcionSaldoExcedente();
      } else {
        this.usuarioActual.aumentarCredito(c*10);
      }
    }
  }
  /**
  * Disminuye el saldo del usuario cuya sesión está abierta, según el precio especificado
  * @param c Precio equivalente en pesos
  * @throws ExcepcionSesionUsuarioInexistente si no hay alguna sesión abierta
  * @throws ExcepcionSaldoInsuficiente si la cantidad de crédito a retirar no es suficiente para el precio especificado
  * @see xyz.campanita.poofinal.cliente.Usuario#disminuirCredito
  */
  public void retirarDinero(int c) throws ExcepcionSaldoInsuficiente, ExcepcionSesionUsuarioInexistente{
    if (this.usuarioActual == null){
      throw new ExcepcionSesionUsuarioInexistente();
    } else {
      this.usuarioActual.disminuirCredito(c*10);
    }
  }
  /**
  * Crea una sesión para el usuario especificado
  * @param n Nombre del usuario
  * @param c Contraseña del usuario
  * @throws ExcepcionSesionUsuarioExistente si ya existe una sesión abierta
  * @throws ExcepcionUsuarioIncorrecto si no existe un usuario con el nombre proporcionado
  * @throws ExcepcionContrasenaIncorrecta si la contraseña proporcionada para autentificarse es incorrecta
  */
  public void iniciarSesion(String n, String c) throws ExcepcionSesionUsuarioExistente, ExcepcionUsuarioIncorrecto, ExcepcionContrasenaIncorrecta{
    if (this.usuarioActual != null){
      throw new ExcepcionSesionUsuarioExistente(this.usuarioActual.getNombre());
    } else {
      for (Usuario u: this.usuarios){
      if (u.getNombre().equals(n)) {
          this.usuarioActual = u; // this.usuarios.get(u);
          break;
        }
      }
      if (this.usuarioActual == null){ // Si el bucle anterior no encontró el nombre
        throw new ExcepcionUsuarioIncorrecto(n);
      }
      if (this.usuarioActual.esContrasena(c) == false){
        this.usuarioActual = null;
        throw new ExcepcionContrasenaIncorrecta();
      }
    }
  }
  /**
  * Elimina una sesión abierta
  * @throws ExcepcionSesionUsuarioInexistente si no hay alguna sesión abierta
  */
  public void cerrarSesion() throws ExcepcionSesionUsuarioInexistente{
    if (this.usuarioActual == null){
      throw new ExcepcionSesionUsuarioInexistente();
    } else {
      this.usuarioActual = null;
    }
  }
  /**
  * Crea un usuario nuevo
  * @param n Nombre del usuario a crear
  * @param c Contraseña del usuario a crear
  * @param t Tipo del usuario a crear
  * @throws ExcepcionUsuarioExistente si el nombre proporcionado ya pertenece a otro usuario
  * @throws ExcepcionContrasenaInvalida si la contraseña nueva no cumple con los requisitos de seguridad
  */
  public void crearUsuario(String n, String c, boolean t) throws ExcepcionUsuarioExistente, ExcepcionContrasenaInvalida {
    for (Usuario u: this.usuarios){
      if (u.getNombre().equals(n)) { throw new ExcepcionUsuarioExistente(n); }
    }
    this.usuarios.add(new Usuario(n, c, t));
  }
  /**
  * Elimina un usuario existente
  * @param n Nombre del usuario a eliminar
  * @throws ExcepcionUsuarioIncorrecto si no existe un usuario con el nombre especificado
  */
  public void eliminarUsuario(String n) throws ExcepcionUsuarioIncorrecto{
    int i = 0;
    for (Usuario u: this.usuarios){
      if (u.getNombre().equals(n)) {
        this.usuarios.remove(i);
        return;
      } else { i++; }
    }
    throw new ExcepcionUsuarioIncorrecto(n);
  }
  /**
  * Cambia el nombre de un usuario existente
  * @param nv Nombre del usuario a renombrar
  * @param nn Nombre nuevo
  * @throws ExcepcionUsuarioIncorrecto si no existe un usuario con el nombre especificado
  * @throws ExcepcionUsuarioExistente si el nuevo nombre ya pertenece a otro usuario existente
  * @see xyz.campanita.poofinal.cliente.Usuario#setNombre
  */
  public void renombrarUsuario(String nv, String nn) throws ExcepcionUsuarioIncorrecto, ExcepcionUsuarioExistente {
    for (Usuario u: this.usuarios){
      if (u.getNombre().equals(nn)){
        throw new ExcepcionUsuarioExistente(nn);
      }
    }
    for (Usuario u: this.usuarios){
      if (u.getNombre().equals(nv)){
        u.setNombre(nn);
        return;
      }
    }
    throw new ExcepcionUsuarioIncorrecto(nn);
  }
  /**
  * Cambia la contraseña de un usuario existente
  * @param n Nombre del usuario cuya contraseña cambiará
  * @param c Contraseña nueva
  * @throws ExcepcionUsuarioIncorrecto si no existe un usuario con el nombre especificado
  * @throws ExcepcionContrasenaInvalida si la contraseña nueva no cumple con los requisitos de seguridad
  * @see xyz.campanita.poofinal.cliente.Usuario#setContrasena
  */
  public void cambiarContrasenaUsuario(String n, String c) throws ExcepcionUsuarioIncorrecto, ExcepcionContrasenaInvalida {
    for (Usuario u: this.usuarios){
      if (u.getNombre().equals(n)){
        u.setContrasena(c);
        return;
      }
    }
    throw new ExcepcionUsuarioIncorrecto(n);
  }
  /**
  * Administra el juego y las pérdidas/ganancias
  * @param j Clase que implementa un juego
  * @param d Cantidad en pesos a apostar dentro del juego
  * @throws ExcepcionSesionUsuarioInexistente si no hay alguna sesión abierta
  * @throws ExcepcionSaldoInsuficiente si la cantidad de crédito a apostar no es suficiente para el precio especificado
  * @return la cantidad de créditos ganados
  */
  public int jugar(Juego j, int d) throws ExcepcionSesionUsuarioInexistente, ExcepcionSaldoInsuficiente{
    if (this.usuarioActual == null){
      throw new ExcepcionSesionUsuarioInexistente();
    } else {
      int puntaje = j.juego(d*10);
      this.usuarioActual.aumentarCredito(puntaje);
      return puntaje;
    }
  }
  // Cosas de limpieza
  /**
  * Quita todos los objetos que no implementan @link{java.io.Serializable}
  */
  public void limpiar(){
    for (Usuario u: this.usuarios){ u.limpiar(); }
  }
  /**
  * Pone todo los objetos retirados por el método limpiar
  */
  public void restaurar(){
    for (Usuario u: this.usuarios){ u.restaurar(); }
  }
}
