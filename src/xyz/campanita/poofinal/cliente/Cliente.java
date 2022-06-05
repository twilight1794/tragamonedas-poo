package xyz.campanita.poofinal.cliente;

import xyz.campanita.poofinal.excepciones.*;
import xyz.campanita.poofinal.juego.*;
import xyz.campanita.poofinal.cliente.Sesion;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.*;

/**
* Interfaz de línea de comandos para el juego del casino
*/
public class Cliente {
  private static Scanner sc;
  private static Sesion s;
  private static ObjectInputStream fin;
  private static ObjectOutputStream fout;

  /**
  * Punto de entrada del programa
  */
  public static void main(String[] args){
    // Recuperar sesión
    try {
      fin = new ObjectInputStream(new FileInputStream("datos.dat"));
      s = (Sesion) fin.readObject();
      s.restaurar();
    } catch (Exception e){
      System.err.println("No existe la sesión, o no se pudo cargar; se creará una nueva sesión.");
      s = new Sesion();
    } finally {
      try {
        sc = new Scanner(System.in);
        fout = new ObjectOutputStream(new FileOutputStream("datos.dat"));
      } catch (Exception e){
        System.err.println("¡No se pudo crear un nuevo archivo de sesión! Saliendo...");
        return;
      }
    }

    // Variables a utilizar
    String n, c, t, cv, cn;
    boolean s0 = false, s1 = false, s2 = false;
    int v, k, i;
    try {
      while (s0 != true){
        if (!s.existeSesion()){
          // No hay sesión de usuario
          System.out.println("¡Bienvenidos al casino!");
          if (s.numeroUsuarios() == 0){
            // No hay usuarios registrados
            s1 = false;
            while (s1 == false){
              System.out.println("No hay usuarios registrados en el programa. Se debe crear un usuario de administrador:\n");
              System.out.print("Nombre de usuario: ");
              n = sc.nextLine();
              System.out.print("Contraseña (debe ser mayor de cuatro caracteres y contener al menos un número): ");
              c = sc.nextLine();
              try {
                s.crearUsuario(n, c, true);
                s.iniciarSesion(n, c);
                s1 = true;
              } catch (ExcepcionContrasenaInvalida e){
                System.err.println(e.getMessage());
              } catch (ExcepcionUsuarioExistente e){}
              // De iniciarSesion: estas excepciones jamás ocurrirán porque estamos iniciando sesión con el usuario que acabamos de crear
              catch (ExcepcionContrasenaIncorrecta e){}
              catch (ExcepcionUsuarioIncorrecto e){}
              catch (ExcepcionSesionUsuarioExistente e){}
            }
          } else {
            s1 = false;
            i = 3;
            while (s1 == false && i>0){
              System.out.println("Iniciar sesión: quedan "+i+" intentos.");
              try {
                System.out.print("Nombre de usuario: ");
                n = sc.nextLine();
                System.out.print("Contraseña: ");
                c = sc.nextLine();
                s.iniciarSesion(n, c);
                s1 = true;
              } catch (ExcepcionContrasenaIncorrecta e){
                System.err.println(e.getMessage());
                i--;
              } catch (ExcepcionUsuarioIncorrecto e){
                System.err.println(e.getMessage());
                i--;
              } catch (ExcepcionSesionUsuarioExistente e){
              } finally {
                if (i==0){
                  s0 = true;
                  System.err.println("Demasiados intentos fallidos");
                }
              }
            }
          }
        } else {
          // Un usuario ya inició sesión
          s1 = false;
          try {
            System.out.println("¡Bienvenido/a, "+s.nombreUsuario()+"!");
            if (s.tipoUsuario() == true){ // Si es administrador
              while (s1 == false){
                System.out.println("¿Qué desea hacer?");
                System.out.println("1. Crear un usuario");
                System.out.println("2. Eliminar un usuario");
                System.out.println("3. Cambiar nombre de un usuario");
                System.out.println("4. Cambiar contraseña de un usuario");
                System.out.println("5. Cambiar contraseña");
                System.out.println("6. Listar usuarios");
                System.out.println("7. Cerrar sesión");
                System.out.println("0. Salir");
                try {
                  v = sc.nextInt();
                  sc.nextLine();
                  switch (v){
                    case 1:
                      System.out.print("Nombre de usuario: ");
                      n = sc.nextLine();
                      System.out.print("Contraseña (debe ser mayor de cuatro caracteres y contener al menos un número): ");
                      c = sc.nextLine();
                      System.out.print("Tipo de usuario ('a' para administrador, cualquier otro valor para usuario normal): ");
                      t = sc.nextLine();
                      s.crearUsuario(n, c, t.equals("a"));
                      break;
                    case 2:
                      System.out.print("Nombre de usuario: ");
                      n = sc.nextLine();
                      s.eliminarUsuario(n);
                      break;
                    case 3:
                      System.out.print("Ingrese el nombre anterior: ");
                      cv = sc.nextLine();
                      System.out.print("Ingrese el nombre nuevo: ");
                      cn = sc.nextLine();
                      s.renombrarUsuario(cv, cn);
                      break;
                    case 4:
                      System.out.print("Ingrese el nombre de usuario: ");
                      n = sc.nextLine();
                      if (n.equals(s.nombreUsuario())){
                        throw new ExcepcionPropioUsuario();
                      }
                      System.out.print("Ingrese la nueva contraseña: ");
                      c = sc.nextLine();
                      s.cambiarContrasenaUsuario(n, c);
                      break;
                    case 5:
                      System.out.print("Ingrese la contraseña anterior: ");
                      cv = sc.nextLine();
                      System.out.print("Ingrese la nueva contraseña: ");
                      cn = sc.nextLine();
                      s.cambiarContrasena(cv, cn);
                      break;
                    case 6:
                      System.out.println("Usuarios del sistema:");
                      for (String l: s.getListaUsuarios()){
                        System.out.println("- "+l);
                      }
                      break;
                    case 7:
                      s.cerrarSesion();
                      s1 = true;
                      break;
                    case 0:
                      throw new InterruptedException();
                    default:
                      throw new ExcepcionOpcionIncorrecta(v, 0, 3);
                  }
                } catch (InputMismatchException e){
                  System.err.println("¡El valor ingresado debe ser un número entero!");
                } catch (ExcepcionUsuarioExistente e){
                  System.err.println(e.getMessage());
                } catch (ExcepcionUsuarioIncorrecto e){
                  System.err.println(e.getMessage());
                } catch (ExcepcionContrasenaInvalida e){
                  System.err.println(e.getMessage());
                } catch (ExcepcionContrasenaIncorrecta e){
                  System.err.println(e.getMessage());
                } catch (ExcepcionPropioUsuario e){
                  System.err.println(e.getMessage());
                } catch (ExcepcionSesionUsuarioInexistente e){}
              }
            } else {
              while (s1 == false){
                System.out.println("Ahora tienes "+s.creditosUsuario()+" créditos ("+(s.creditosUsuario()/10)+" pesos) en tu cuenta.");
                System.out.println("1. Jugar");
                System.out.println("2. Jugar automáticamente");
                System.out.println("3. Depositar dinero");
                System.out.println("4. Retirar dinero");
                System.out.println("5. Cambiar contraseña");
                System.out.println("6. Cerrar sesión");
                System.out.println("0. Salir");
                try {
                  v = sc.nextInt();
                  sc.nextLine();
                  switch (v){
                    case 1:
                      System.out.println("¿Cuántos pesos deseas apostar?");
                      v = sc.nextInt();
                      sc.nextLine();
                      if (s.creditosUsuario() > v){
                        int ganancia = (int) (s.jugar(new Tragamonedas(), v)/10);
                        if (ganancia>=0){
                          System.out.println("Has ganado "+ganancia+" pesos.");
                        } else {
                          System.out.println("Has perdido "+(-1*ganancia)+" pesos.");
                        }
                      } else {
                        System.err.println("No tienes créditos suficientes para jugar. Por favor, deposita más dinero para jugar, o introduce una cantidad menor.");
                      }
                      break;
                    case 2:
                      System.out.println("¿Cuántos pesos deseas apostar?");
                      v = sc.nextInt();
                      sc.nextLine();
                      System.out.println("¿Cuántos tiros desea jugar automáticamente?");
                      k = sc.nextInt();
                      sc.nextLine();
                      if (s.creditosUsuario() > k*v){
                        int gananciaTotal = 0;
                        for (i=1; i<=k; i++){
                          System.out.println("Juego "+i+" de "+k);
                          int ganancia = (int) (s.jugar(new Tragamonedas(), v)/10);
                          gananciaTotal += ganancia;
                          if (ganancia>=0){
                            System.out.println("Has ganado "+ganancia+" pesos.");
                          } else {
                            System.out.println("Has perdido "+(-1*ganancia)+" pesos.");
                          }
                        }
                        if (gananciaTotal>=0){
                          System.out.println("En total, has ganado "+gananciaTotal+" pesos.");
                        } else {
                          System.out.println("En total, has perdido "+(-1*gananciaTotal)+" pesos.");
                        }
                      } else {
                        System.err.println("No tienes créditos suficientes para jugar. Por favor, deposita más dinero para jugar, o introduce una cantidad menor.");
                      }
                      break;
                    case 3:
                      System.out.println("Recuerda que 1 peso = 10 créditos");
                      System.out.println("Ingrese el saldo a depositar en pesos, mínimo de 20, máximo 1 millón:");
                      v = sc.nextInt();
                      sc.nextLine();
                      s.depositarDinero(v);
                      break;
                    case 4:
                      System.out.println("Recuerda que 1 peso = 10 créditos");
                      System.out.println("Ingrese el saldo a retirar en pesos:");
                      v = sc.nextInt();
                      sc.nextLine();
                      s.retirarDinero(v);
                      break;
                    case 5:
                      System.out.print("Ingrese la contraseña anterior: ");
                      cv = sc.nextLine();
                      System.out.print("Ingrese la nueva contraseña: ");
                      cn = sc.nextLine();
                      s.cambiarContrasena(cv, cn);
                      break;
                    case 6:
                      s.cerrarSesion();
                      s1 = true;
                      break;
                    case 0:
                      throw new InterruptedException();
                    default:
                      throw new ExcepcionOpcionIncorrecta(v, 0, 4);
                  }
                } catch (InputMismatchException e){
                  System.err.println("¡El valor ingresado debe ser un número entero!");
                } catch (ExcepcionContrasenaIncorrecta e){
                  System.err.println(e.getMessage());
                } catch (ExcepcionContrasenaInvalida e){
                  System.err.println(e.getMessage());
                } catch (ExcepcionIngresoInsuficiente e){
                  System.err.println(e.getMessage());
                } catch (ExcepcionSaldoInsuficiente e){
                  System.err.println(e.getMessage());
                } catch (ExcepcionSaldoExcedente e){
                  System.err.println(e.getMessage());
                } catch (ExcepcionSesionUsuarioInexistente e){}
              }
            }
          } catch (ExcepcionSesionUsuarioInexistente e){}
        }
      }
    } catch (NoSuchElementException e){ // Al pulsar C-d mientras se escribe algo
      System.out.println("Guardando sesión...");
      s0 = true;
    } catch (InterruptedException e){
      System.out.println("Guardando sesión...");
      s0 = true;
    }
    // Guardar sesión
    sc.close();
    try {
      s.limpiar();
      fout.writeObject(s);
      fout.close();
      System.out.println("¡Sesión guardada!");
    } catch (Exception e){
      System.err.println(e);
      System.err.println("No se pudo guardar la sesión.");
    }
  }
}
