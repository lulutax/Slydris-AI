package main;

import java.util.Collections;
import java.util.Random;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class graficaGame extends AnchorPane implements Runnable{



	public Piece[][] mat;
	public Piece p=null,p2=null,p3=null;
	int dimPezzo=Grafica.dimPezzo;

	public int nrow=Game.nrow;
	public int ncol=Game.ncol;
	static Canvas c;
	static GraphicsContext g;
	public boolean exit = false;
	public int l,l2,l3;


	public graficaGame() {
		c= new Canvas(12*dimPezzo+20 ,24*dimPezzo+60);
		g= c.getGraphicsContext2D();
		this.getChildren().add(c);
		this.mat = new Piece[Game.nrow][Game.ncol];

	}

	public Piece[][] getMat() {return mat;}

	public void setMat(Piece[][] piece) {



		for(int i=1;i<Game.nrow;i++) 
			for(int j=1;j<Game.ncol;j++) 
				mat[i][j]=null;


		for(int i=1;i<Game.nrow;i++) {
			for(int j=1;j<Game.ncol;j++) {
				if(piece[i][j]!=null) {
					mat[i][j]=piece[i][j];
				}
				else {
					mat[i][j]=null;

				}
			}
		}
	}

	public synchronized void repaint(){

		//DISEGNO LA MAPPA
		g.setFill(Color.BLACK);	
		g.fillRect(0, 0, (dimPezzo+1)*12, (dimPezzo+1)*24);



		for (int i = 0; i < nrow; i++) 
			for (int j = 0; j < ncol; j++) {


				if(j==0|| j==11 || i==23|| i==0 ) {
					g.setFill(Color.ORANGE);
					g.fillRect((dimPezzo+1)*j, (dimPezzo+1)*i, dimPezzo, dimPezzo);
					//g.setFill(Color.BLACK);
					//g.fillText(String.valueOf(j), (dimPezzo+1)*j,  (dimPezzo+1)*i);

				}
				else {
					g.setFill(Color.DARKKHAKI);
					g.fillRect((dimPezzo+1)*j, (dimPezzo+1)*i, dimPezzo, dimPezzo);
				}

				//DISEGNA NUMERI
				if(i==23 && j>=1 && j<=10) {
					g.setFill(Color.BLACK);
					g.fillText(String.valueOf(j), (dimPezzo+1)*j+10,  (dimPezzo+1)*i+20);
				}
				if(j==0 && i>=1 && i<=22) {
					g.setFill(Color.BLACK);
					g.fillText(String.valueOf(i), (dimPezzo+1)*j+10,  (dimPezzo+1)*i+20);
				}

			}

		//DISEGNO I BLOCCHI
		for (int i = 0; i < nrow; i++) 
			for (int j = 0; j < ncol; j++) 
				if(mat[i][j]!=null) {
					g.setFill(mat[i][j].getC());
					g.fillRoundRect((dimPezzo+1)*j, (dimPezzo+1)*i, dimPezzo*mat[i][j].getTipo(), dimPezzo,20,20);

					j+=mat[i][j].getTipo()-1;
				}




		//DISEGNO L'OMBRA DEL PBLOCCO (DOVE DOVREBBE CADERE)
		if(Game.muovi) {
			g.setStroke(Game.tmp_colore);
			g.strokeRoundRect((dimPezzo+1)*Game.tmp_y, (dimPezzo+1)*Game.tmp_x, dimPezzo*Game.tmp_tipo, dimPezzo,20,20);
			if(Game.tmp2_y!=0 && Game.tmp2_x!=0 && Game.tmp2_tipo!=0 && Game.tmp3_y!=0 && Game.tmp3_x!=0 && Game.tmp3_tipo!=0) {
				g.setStroke(Game.tmp2_colore);
				g.strokeRoundRect((dimPezzo+1)*Game.tmp2_y, (dimPezzo+1)*Game.tmp2_x, dimPezzo*Game.tmp2_tipo, dimPezzo,20,20);
				g.setStroke(Game.tmp3_colore);
				g.strokeRoundRect((dimPezzo+1)*Game.tmp3_y, (dimPezzo+1)*Game.tmp3_x, dimPezzo*Game.tmp3_tipo, dimPezzo,20,20);
			}
			else if(Game.tmp2_y!=0 && Game.tmp2_x!=0 && Game.tmp2_tipo!=0 && Game.tmp3_y==0 && Game.tmp3_x==0 && Game.tmp3_tipo==0) {
				g.setStroke(Game.tmp2_colore);
				g.strokeRoundRect((dimPezzo+1)*Game.tmp2_y, (dimPezzo+1)*Game.tmp2_x, dimPezzo*Game.tmp2_tipo, dimPezzo,20,20);
			}
			Game.muovi=false;


		}
	}
	public void setTmp(Piece tmp,int k,Piece tmp2,int k2,Piece tmp3,int k3) {
		p2=null;
		p3=null;
		l2=0;
		l3=0;
		p=new Piece(tmp.getX(),tmp.getY(),tmp.getTipo(),tmp.getPunteggio(),tmp.getC());
		l=k;
		if(tmp2!=null) {
			p2=new Piece(tmp2.getX(),tmp2.getY(),tmp2.getTipo(),tmp2.getPunteggio(),tmp2.getC());
			l2=k2;
		}
		if(tmp3!=null) {
			p3=new Piece(tmp3.getX(),tmp3.getY(),tmp3.getTipo(),tmp3.getPunteggio(),tmp3.getC());
			l3=k3;
		}
	}
	public void disegnaBlocco() {


		if(p2!=null) {
			g.setFill(p2.getC());
			g.fillRoundRect((dimPezzo+1)*p2.getY(), (dimPezzo+1)*p2.getX(), dimPezzo*p2.getTipo(), dimPezzo,20,20);
		}
		if(p3!=null) {

			g.setFill(p3.getC());
			g.fillRoundRect((dimPezzo+1)*p3.getY(), (dimPezzo+1)*p3.getX(), dimPezzo*p3.getTipo(), dimPezzo,20,20);
		}
		g.setFill(p.getC());
		g.fillRoundRect((dimPezzo+1)*p.getY(), (dimPezzo+1)*p.getX(), dimPezzo*p.getTipo(), dimPezzo,20,20);

	}

	public void stop(){
		exit = true;
	}
	@Override
	public void run() {

		int x2=0,x3=0;
		if(p2!=null)
			x2=p2.getX();
		if(p3!=null)
			x3=p3.getX();
		int x=p.getX();
		exit=false;
		while(!exit) {
			if( p3!= null && x==l && x2==l2 && x3==l3 )

				exit=true;

			else if(p2!=null && p3==null && x2==l2 && x==l)
				exit=true;

			else {

				if(x<l) {
					x++;
					p.setX(x);

				}
				if(p2!=null && x2< l2) {
					x2++;
					p2.setX(x2);
				}
				if(p3!=null && x3< l3) {
					x3++;
					p3.setX(x3);
				}

				disegnaBlocco();
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				repaint();
			}

		}

		setMat(Game.mat);

		//g.repaint();
		boolean controllo1=false;
		boolean controllo2=false;
		boolean esci=false;

		//ESCO DAL CICLO FINCHE NON CI SONO RIGHE DA VERIFICARE CHE SIANO PIENE E CHE NON CI SONO BLOCCHI CHE CADONO SPOSTANDONE ALTRI
		while(esci==false) {
			controllo1= verifica2();
			controllo2=verifica();
			if(controllo1== true && controllo2==true)
				esci=true;

		}
		setMat(Game.mat);
		repaint();


		if(haiPerso()) {
			System.out.println("---------------HAI PERSO--------------");
			resetMatrice();
			//DOBBIAMO METTERE IL CHIUDI GIOCO
		}

		generaPezzo();

	}




	void resetMatrice() {

		for(int i=1;i<Game.nrow;i++) 
			for(int j=1;j<Game.ncol;j++) 
				Game.mat[i][j]=null;
		setMat(Game.mat);
		repaint();

	}

	public boolean haiPerso() {
		int i=2;
		for(int j=1;j<ncol;j++) {
			if(mat[i][j]!=null) {
				return true;
			}
		}
		return false;
	}


	// RESTITUISCO LA COORDINATA IN CUI IL PEZZO DOVREBBE CADERE
	public int restituisciCoordinataX(int x2,int y2,Piece g) {
		for(int i=x2+1;i<23;i++) 
			for(int j=0;j<g.getTipo();j++) 
				if (Game.mat[i][y2+j] != null)
					return i-1;

		return 22;
	}
	public void generaPezzo() {


		/*punteggio pezzo1=1
	    	   pezzo2=4
	    	   pezzo3=9
		 */

		Piece p1Rosso=new Piece(1,5,1,1,Color.RED);
		Piece p1Verde=new Piece(1,5,1,1,Color.GREEN);



		Piece p2Rosso=new Piece(1,5,2,4,Color.RED);
		Piece p2Verde=new Piece(1,5,2,4,Color.GREEN);



		Piece p3Rosso=new Piece(1,5,3,9,Color.RED);
		Piece p3Verde=new Piece(1,5,3,9,Color.GREEN);



		if(Game.pezzi.isEmpty() ) {

			Collections.addAll(Game.pezzi, p1Rosso,p1Verde,p2Rosso,p2Verde, p3Rosso,p3Verde);
			Collections.shuffle(Game.pezzi);
		}

		Random random = new Random();
		int num1 = random.nextInt(5)+1;
		int num2 = random.nextInt(5)+6;


		int tipoPezzzo0= Game.pezzi.get(0).getTipo();
		int tipoPezzzo1= Game.pezzi.get(1).getTipo();
		if(num1!=num2 && num1+tipoPezzzo0<=num2 && num2+tipoPezzzo1<=11) {
			Game.pezzi.get(0).setY(num1);
			Game.pezzi.get(1).setY(num2);
		}
		else {
			Game.pezzi.get(0).setY(2);
			Game.pezzi.get(1).setY(7);
		}

		Game.pezzoCorrente= Game.pezzi.get(0);
		Game.pezzoCorrente2= Game.pezzi.get(1);

		for(int i=0;i<2;i++)
			Game.pezzi.remove(0);


		for(int i=0;i<Game.pezzoCorrente.getTipo();i++)
			Game.mat[Game.pezzoCorrente.getX()][Game.pezzoCorrente.getY()+i]=Game.pezzoCorrente;


		for(int i=0;i<Game.pezzoCorrente2.getTipo();i++)
			Game.mat[Game.pezzoCorrente2.getX()][Game.pezzoCorrente2.getY()+i]=Game.pezzoCorrente2;

		setMat(Game.mat);
		repaint();	

	}
	public boolean verifica2() {

		Piece t=null;
		int i=22;
		boolean controllo1= true; //SE NON FACCIO NESSUNA MODIFICA ALLORA RESTITUISCO TRUE

		while(i!=2) {

			boolean ok=false;
			for(int j=1;j<ncol;j++) {

				if(Game.mat[i][j]==null) {
					for(int o=i-1;o>1;o--) 
						if(Game.mat[o][j]!=null) { //HO TROVATO IL BLOCCO CHE DEVE SCENDERE 
							t=Game.mat[o][j];
							break;
						}

					if(t!=null  ) {
						int x1=t.getX();
						int y1=t.getY();
						int x=restituisciCoordinataX(x1,y1,t);
						if(x==i ) { // MI ACCERTO CHE DEVE SCENDERE NELLO STESSO PUNTO 
							int tipo=t.getTipo();
							t.setX(i);
							t.setY(y1);

							//FACCIO LO SPOSTAMENTO
							for(int z=0;z<tipo;z++)
								Game.mat[i][j+z]=t;

							for(int z=0;z<tipo;z++)
								Game.mat[x1][y1+z]=null;

							//
							ok=true;
						}
					}
				}
				t=null;
			}
			// SE HO FATTO DELLE MODIFICHE RIPARTO DI NUOVO DALL INIZIO
			if(ok) {
				i=22;
				controllo1=false;
			}//ALTRIMENTI VADO AVANTI
			else 
				i--;
		}

		return controllo1;
	}


	//CONTROLLO SE CI SONO RIGHE PIENE
	boolean rigaUguale=true;
	public boolean verifica() {
		int i=22;
		boolean controllo2=true;
		while(i!=2) {
			int cont=0;
			for (int j = 1; j < ncol; j++) 
				if(Game.mat[i][j]!=null) 
					cont++;



			//CONTA PUNTEGGIO
			if(cont==ncol-2) {
				for(int j=1; j<ncol-1; j++) {
					if(j+1<ncol-1 && !(Game.mat[i][j].getC().equals(Game.mat[i][j+1].getC()))) 
						rigaUguale=false;		

					Game.punteggioTotale+=Game.mat[i][j].getPunteggio();
					j+=Game.mat[i][j].getTipo()-1;
				}
				//System.out.println(Game.punteggioTotale);
				//AGGIUNGI BONUS
				if(rigaUguale)	 
					Game.punteggioTotale+=100;

			}

			if(cont==ncol-2) {

				controllo2=false;
				//SPOSTO TUTTI I PEZZO DI SPORA SOTTO
				for(int d=i;d>1;d--) {
					for(int k=1;k<ncol;k++) {
						if(Game.mat[d-1][k]!=null) {
							Game.mat[d-1][k].setX(d);
							Game.mat[d][k]=Game.mat[d-1][k];
						}
						else {
							Game.mat[d][k]=null;

						}

					}
				}
				i =22;		
			}
			else 
				i--;
			rigaUguale=true;	
		}	
		return controllo2;
	}



}