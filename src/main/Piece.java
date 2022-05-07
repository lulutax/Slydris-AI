package main;


import java.util.Objects;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;
import javafx.scene.paint.Color;


@Id("piece")
public class Piece {

	
	@Param(0)
	private int x;
	@Param(1)
	private int y;
	@Param(2)
	private int tipo;
	@Param(3)
	private String colore;

	private int punteggio;
	private Color c;
	
	
	public Piece()  {}
	
	public Piece(int x, int y, int tipo, int punteggio, Color c) {
		super();
		this.x = x;
		this.y = y;
		this.tipo = tipo;
		this.punteggio = punteggio;
		this.c = c;
	}

	public Piece(int x, int y, int tipo,String color) {
		super();
		this.x = x;
		this.y = y;
		this.tipo = tipo;
		this.colore = color;	

	}
	public Piece (Piece p) {
		this.x = p.x;
		this.y = p.y;
		this.tipo =p.tipo;
		this.c=p.c;
		this.punteggio=p.punteggio;
	}
	public int getPunteggio() {
		return punteggio;
	}
	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public Color getC() {
		return c;
	}
	
	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public void setC(Color c) {
		this.c = c;
	}

	@Override
	public String toString() {
		return "Piece [x=" + x + ", y=" + y + ", tipo=" + tipo + "]";
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		return Objects.equals(c, other.c) && punteggio == other.punteggio && tipo == other.tipo && x == other.x
				&& y == other.y;
	}

	
	



}