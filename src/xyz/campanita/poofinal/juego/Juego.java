package xyz.campanita.poofinal.juego;

/**
* Define la forma de un juego para el casino
*/
public interface Juego {
  /**
  * Función que contiene al juego
  * @param c Créditos apostados
  * @return Créditos ganados después del juego
  */ 
  int juego(int c);
}