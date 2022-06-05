package xyz.campanita.poofinal.juego;

import java.io.Serializable;
import java.util.Random;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.Thread;
import java.lang.Runnable;
import xyz.campanita.poofinal.juego.Juego;

/**
* Símbolos usados por el tragamonedas.
* Cada símbolo lleva asociada una tupla de cuatro valores:
* - El número de créditos ganados al formar una combinación de 3 símbolos, al apostar 50 créditos, o 5 pesos
* - El número de créditos ganados al formar una combinación de 4 símbolos, al apostar 50 créditos, o 5 pesos
* - El número de créditos ganados al formar una combinación de 5 símbolos, al apostar 50 créditos, o 5 pesos
* - El caracter Unicode que representa al símbolo
* - El caracter ASCII que representa al símbolo, en caso de no poder usar caracteres Unicode
*/
enum Simbolos {
  COMODIN  (15000, 30000, 100000, "🎲", "X"),
  SIETE    (10000, 25000, 50000, "７", "7"),
  DIAMANTE (2500, 5000, 10000, "💎", "D"),
  TREBOL   (650, 1250, 2500, "🍀", "T"),
  CEREZA   (600, 1000, 1500, "🍒", "C"),
  SANDIA   (160, 325, 650, "🍉", "S"),
  UVAS     (160, 325, 650, "🍇", "U");

  private final int creditos[] = new int[3];
  public final String caracter;
  public final String caracterASCII;
  Simbolos(int a, int b, int c, String ch, String chw){
    this.creditos[0] = a;
    this.creditos[1] = b;
    this.creditos[2] = c;
    // debería ser ch, pero desafortunadamente, las consolas nativas no tienen los tipos necesarios para mostrar esos caracteres,
    // y solo los he podido representar en las consolas web, donde no existe ese problema
    this.caracter = chw;
    this.caracterASCII = chw;
  }
  /**
  * Devuelve el número de créditos ganados al formar una combinación de 3 símbolos, al apostar 50 créditos
  */
  public int obt3(){
    return this.creditos[0];
  }
  /**
  * Devuelve el número de créditos ganados al formar una combinación de 4 símbolos, al apostar 50 créditos
  */
  public int obt4(){
    return this.creditos[1];
  }
  /**
  * Devuelve el número de créditos ganados al formar una combinación de 5 símbolos, al apostar 50 créditos
  */
  public int obt5(){
    return this.creditos[2];
  }
};

// Porciones de código a paralelizar
class R1 implements Runnable {
  int i;
  Simbolos[] matriz;

  public R1(int i, Simbolos[] matriz) {
    this.i = i;
    this.matriz = matriz;
  }
  public void run() {
    ArrayList<Simbolos> rodillo = new ArrayList<>(Arrays.asList(Simbolos.values()));
    Collections.shuffle(rodillo);
    for (int j=0; j<3; j++){
      matriz[5*j+this.i] = rodillo.get(j);
    }
  }
}

// Simbolos.values()[new Random().nextInt(Simbolos.values().length)]

/**
* Juego de tragamonedas
*/
public class Tragamonedas implements Juego{
  public int juego(int c){
    int i, j, k, ganancia = -c;
    Simbolos matriz[] = new Simbolos[15];
    // Rellenar rodillos
    Thread[] hilos = new Thread[5];
    for (i=0; i<5; i++){
      Runnable r1 = new R1(i, matriz);
      hilos[i] = new Thread(r1);
      hilos[i].start();
    }
    for (i=0; i<5; i++){
      try {
        hilos[i].join();
      } catch (InterruptedException e){}
    }

    // Imprimir salida
    // (esto no debería hacerse aquí, sino en Cliente, pero bueno, es complicar el asunto un mucho)
    for (i=0; i<15; i++){
      if (i%5 == 0) System.out.print("\n");
      System.out.print(matriz[i].caracter+" ");
    }
    System.out.print("\n");

    // Buscar coincidencias
    int combinaciones[] = new int[5]; // Calidad de combinaciones
    Simbolos simbolos[] = new Simbolos[5]; // Símbolos de las combinaciones
    int lineas[][] = { // Posiciones a recorrer para buscar combinaciones ganadoras
      {0, 1, 2, 3, 4}, // 1ra línea
      {5, 6, 7, 8, 9}, // 2da línea
      {10, 11, 12, 13, 14}, // 3ra línea
      {0, 6, 12, 8, 4}, // Línea cóncava
      {10, 6, 2, 8, 14} // Línea convexa
    };
    for (i=0; i<5; i++){
      for (j=0; j<5; j++){
        if (j==0){
          combinaciones[i] = 1;
          simbolos[i] = matriz[lineas[i][j]];
          continue;
        }
        if (
          matriz[lineas[i][j]] == matriz[lineas[i][j-1]] ||
          matriz[lineas[i][j]] == Simbolos.COMODIN ||
          (matriz[lineas[i][j-1]] == Simbolos.COMODIN && matriz[lineas[i][j]] == simbolos[i])
        ){ combinaciones[i]++; }
        else {
          if (combinaciones[i]>=3){
            break;
          }
          combinaciones[i] = 1;
        }
        // Si el caracter anterior no era un comodín, y el siguiente sí lo es, no substuimos, pues el comodín no es un símbolo per se a menos que toda la combinación sea comodín
        if (!(simbolos[i] != Simbolos.COMODIN && matriz[lineas[i][j]] == Simbolos.COMODIN)){
          simbolos[i] = matriz[lineas[i][j]];
        }
      }
      //
      //System.out.println("* i="+i+";enum="+simbolos[i]+";comb="+combinaciones[i]);
    }
    // Calcular puntajes
    for (i=0; i<5; i++){
      if (combinaciones[i]==3){
        ganancia += (int) (simbolos[i].obt3()*c/50);
      } else if (combinaciones[i]==4){
        ganancia += (int) (simbolos[i].obt4()*c/50);
      } else if (combinaciones[i]==5){
        ganancia += (int) (simbolos[i].obt5()*c/50);
      }
    }

    return ganancia;
  }
}
