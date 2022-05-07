package main;
		
		import java.lang.reflect.InvocationTargetException;
		import java.util.ArrayList;
		import it.unical.mat.embasp.base.Handler;
		import it.unical.mat.embasp.base.InputProgram;
		import it.unical.mat.embasp.base.Output;
		import it.unical.mat.embasp.languages.IllegalAnnotationException;
		import it.unical.mat.embasp.languages.ObjectNotValidException;
		import it.unical.mat.embasp.languages.asp.ASPInputProgram;
		import it.unical.mat.embasp.languages.asp.ASPMapper;
		import it.unical.mat.embasp.languages.asp.AnswerSet;
		import it.unical.mat.embasp.languages.asp.AnswerSets;
		import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
		import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;
		import javafx.scene.paint.Color;
		
		
		
		public class Game  {
		
		
			int dimPezzo=Grafica.dimPezzo;
			static Piece pezzoCorrente,pezzoCorrente2 ; 	
			static int tmp_x =0, tmp_y=0,tmp2_x =0,tmp2_y=0,tmp2_tipo=0,tmp_tipo=0,tmp3_x=0,tmp3_y=0,tmp3_tipo=0;
			static Color tmp_colore,tmp2_colore,tmp3_colore;
			static boolean muovi=false;
			static int nrow,ncol;
			static public Piece[][] mat;
			static public ArrayList<Piece> pezzi = new ArrayList<Piece>();
			static public  int punteggioTotale;	
		
		
			//INTELLIGENZA
			private static String encodingResource="encodings/slydris";
			private static Handler handler;
			private Output o ;
			private AnswerSets answers;
			private InputProgram facts;
			private InputProgram program;
		
		
			//THREAD PER GRAFICA
			public graficaGame g;
			public Thread p;
		
		
		
			public Game(int x, int y) {
		
		
				handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.win.x64_3"));
				nrow=y;
				ncol=x;
				mat=new Piece [nrow][ncol];
				punteggioTotale=0;
		
				facts= new ASPInputProgram();
				program= new ASPInputProgram();
		
				g=new graficaGame();
				g.generaPezzo();
				g.setMat(mat);
				g.repaint();
			}
		
			public int getPunteggio() {return punteggioTotale;}
			
			public void sposta(int x, int y,int x2,int y2 ) {
				muovi=false;
				//X Y, CORDINATE DOVE HAI PREMUTO
				//X2 Y2, COORDINATE DOVE DEVI SPOSTARE
				Piece tmp=null;
				//POSSO SPOSTARE SOLO SE FACCIO UN MOVIMENTO VERSO DETRA O VERSO SINISTRA MA NON SOPRA E SOTTO
				if(y!=y2 && mat[x][y]!=null) {
		
					//verifica a destra ovvero se il pezzo è di due aggirno cordinata y
					if( y-1> 0 && mat[x][y].getTipo()==2 && mat[x][y-1]==mat[x][y]) {
						y2--;	
						y--;
					}
					else if(y-2 > 0 && mat[x][y].getTipo()==3 && mat[x][y-1]==mat[x][y] && mat[x][y-2]==mat[x][y]   ) {
						y2= y2-2;	
						y=y-2;
					}
					else if(y-1 > 0 && mat[x][y].getTipo()==3 && mat[x][y-1]==mat[x][y] && mat[x][y+1]==mat[x][y]   ) {
						y2--;	
						y--;
					}
		
					boolean ok=true;
		
					for(int i=0;i<mat[x][y].getTipo();i++) {
						int m=y2+i;
						if(!(x2<nrow-1 && x2>0 && m<ncol-1 && m>0))
							ok=false;
					}
					if(ok ) {
						tmp=mat[x][y];
						boolean v=verificaBloccoAccanto(y2,tmp);
						boolean v2=false,v3=false,v4=true;
						int xp=0,yp=0,xp2=0,yp2=0,l=0,l1=0,l2=0;
		
		
						if(v) {
							//IMPOSTO PRIMA A NULL SENNO IL BLOCCO CHE DEVE SCENDERE VEDE CHEIL POSTO E' OCCUPATO
							for(int i=0;i<tmp.getTipo();i++)
								mat[x][y+i]=null;
							//LO STESSO QUA
							if(tmp!=pezzoCorrente && tmp!=pezzoCorrente2) {
								
								xp=pezzoCorrente.getX();
								yp=pezzoCorrente.getY();
								for(int i=0;i<pezzoCorrente.getTipo();i++) 
									mat[xp][yp+i]=null;
								
								xp2=pezzoCorrente2.getX();
								yp2=pezzoCorrente2.getY();
								for(int i=0;i<pezzoCorrente2.getTipo();i++) 
									mat[xp2][yp2+i]=null;
		
							}
							else if(tmp!= pezzoCorrente && tmp==pezzoCorrente2) {
							
							xp=pezzoCorrente.getX();
							yp=pezzoCorrente.getY();
							for(int i=0;i<pezzoCorrente.getTipo();i++) 
								mat[xp][yp+i]=null;
							}
							else if(tmp!= pezzoCorrente2 && tmp==pezzoCorrente) {
								xp2=pezzoCorrente2.getX();
								yp2=pezzoCorrente2.getY();
								for(int i=0;i<pezzoCorrente2.getTipo();i++) 
									mat[xp2][yp2+i]=null;
							}
							g.setMat(mat);
							
							//RESTITUISCE LA RIGA IN CUI DEVE CADERE
							l= g.restituisciCoordinataX(x,y2,tmp);
							tmp.setY(y2);
							tmp.setX(l);
							for(int i=0;i<tmp.getTipo();i++) 
								mat[l][y2+i]=tmp;
		
							if(tmp!= pezzoCorrente && tmp!=pezzoCorrente2) {
								
								v2=true;
								l1=g.restituisciCoordinataX(xp,yp,pezzoCorrente);	
								pezzoCorrente.setX(l1);
								for(int i=0;i<pezzoCorrente.getTipo();i++) 
									mat[l1][yp+i]=pezzoCorrente;
								
								l2=g.restituisciCoordinataX(xp2,yp2,pezzoCorrente2);	
								
								pezzoCorrente2.setX(l2);
								for(int i=0;i<pezzoCorrente2.getTipo();i++) 
									mat[l2][yp2+i]=pezzoCorrente2;
							}
							else if(tmp!= pezzoCorrente && tmp==pezzoCorrente2) {
								v3=true;
								l1=g.restituisciCoordinataX(xp,yp,pezzoCorrente);	
								pezzoCorrente.setX(l1);
								for(int i=0;i<pezzoCorrente.getTipo();i++) 
									mat[l1][yp+i]=pezzoCorrente;
							}
							else if(tmp!= pezzoCorrente2 && tmp==pezzoCorrente) {
								v4=true;
		
								l2=g.restituisciCoordinataX(xp2,yp2,pezzoCorrente2);	
								
								pezzoCorrente2.setX(l2);
								for(int i=0;i<pezzoCorrente2.getTipo();i++) 
									mat[l2][yp2+i]=pezzoCorrente2;
							}
						}
						//non ho spostato i pezzi di sopra quindi chiamo l animazione di tutti e tre
						if(v && v2) {
							tmp.setX(x);
							pezzoCorrente.setX(xp);
							pezzoCorrente2.setX(xp2);
		
							g.setTmp(tmp,l,pezzoCorrente,l1,pezzoCorrente2,l2);
							p=new Thread(g);
							p.start();
		
							tmp.setX(l);
							pezzoCorrente.setX(l1);
							pezzoCorrente2.setX(l2);
		
		
						}
						//ho spostato solo il pezzo 2 quindi chiamo l animazione di pezzo1
						else if(v && !v2 && v3) {
		
							tmp.setX(x);
							pezzoCorrente.setX(xp);
							g.setTmp(tmp,l,pezzoCorrente,l1,null,0);
							p=new Thread(g);
							p.start();
							tmp.setX(l);
							pezzoCorrente.setX(l1);
		
		
						}
						//ho spostato il pezzo1 quindi chiamo l animazione di pezzo2
						else if(v && v4 && !v2 ) {
		
							tmp.setX(x);
							pezzoCorrente2.setX(xp2);
							g.setTmp(tmp,l,pezzoCorrente2,l2,null,0);
							p=new Thread(g);
							p.start();
							tmp.setX(l);
							pezzoCorrente2.setX(l2);
		
		
						}
						
		
					}

				
				}
				
				
				
			}
		
		
			//VERIFICA SE CI SONO BLOCCHI DOVE VOGLIO SPOSTARE
			public boolean verificaBloccoAccanto(int y2,Piece p) {
		
				if(p.getY()<y2) {
					for(int y=p.getY()+1;y<=y2;y++)
						for(int j=0;j<p.getTipo();j++) { 
							if(mat[p.getX()][y+j]!= null && mat[p.getX()][y+j]!= p) 
								return false;
						}
					return true;
		
				}
				else {
					for(int y=p.getY()-1;y>=y2;y--)
						for(int j=0;j<p.getTipo();j++) 
							if(mat[p.getX()][y+j]!= null && mat[p.getX()][y+j]!= p)
								return false;
		
					return true;
				}
			}
		
		
		
		
			//STAMPA LA MATRICE
			public void S() {
				for(int i=1;i<nrow;i++) {
					for(int l=1;l<ncol;l++)
					{
						if(mat[i][l]!=null)
							System.out.print("0");
						else if(mat[i][l]==null)
							System.out.print("4");
					}
					System.out.println();
		
				}
			}
		
		
		
			public void muoviMouse(int x, int y, int x2, int y2) {
				tmp_x =0;
				tmp_y=0;
				tmp2_x =0;
				tmp2_y=0; 
				tmp2_tipo=0;
				tmp_tipo=0;
				tmp3_x =0;
				tmp3_y=0;
				tmp3_tipo=0;
				tmp3_tipo=0;
		
				//X Y, CORDINATE DOVE HAI PREMUTO
				//X2 Y2, COORDINATE DOVE TRASCINI
		
				//POSSO SPOSTARE SOLO SE FACCIO UN MOVIMENTO VERSO DETRA O VERSO SINISTRA Ma NON SOPRA E SOTTO
				if(y!=y2 && mat[x][y]!=null) {
		
					//verifica a destra ovvero se il pezzo è di due aggirno cordinata y
					if( y-1> 0 && mat[x][y].getTipo()==2 && mat[x][y-1]==mat[x][y]) {
						y2--;	
						y--;
					}
					//SE IL PEZZO E' DI 3
					else if(y-2 > 0 && mat[x][y].getTipo()==3 && mat[x][y-1]==mat[x][y] && mat[x][y-2]==mat[x][y]   ) {
						y2= y2-2;	
						y=y-2;
					}
					else if(y-1 > 0 && mat[x][y].getTipo()==3 && mat[x][y-1]==mat[x][y] && mat[x][y+1]==mat[x][y]   ) {
						y2--;	
						y--;
					}
		
					boolean ok=true;
		
					for(int i=0;i<mat[x][y].getTipo();i++) {
						int m=y2+i;
						if(!(x2<nrow-1 && x2>0 && m<ncol-1 && m>0))
							ok=false;
					}
					if(ok ) {
						muovi=true;
						boolean v=verificaBloccoAccanto(y2,mat[x][y]);
						if(v) {
		
							int l= g.restituisciCoordinataX(x,y2,mat[x][y]);
							tmp_x=l;
							tmp_y=y2;
							tmp_tipo=mat[x][y].getTipo();
							tmp_colore=mat[x][y].getC();
		
							if(mat[x][y]!= pezzoCorrente && mat[x][y]!= pezzoCorrente2) {
		
								int xp=pezzoCorrente.getX();
								int yp=pezzoCorrente.getY();
								int l1=g.restituisciCoordinataX(xp,yp,pezzoCorrente);
								
								int xp2=pezzoCorrente2.getX();
								int yp2=pezzoCorrente2.getY();
								int l2=g.restituisciCoordinataX(xp2,yp2,pezzoCorrente2);
								
								tmp2_x=l1;
								tmp2_y=yp;
								tmp2_tipo=pezzoCorrente.getTipo();
								tmp2_colore=pezzoCorrente.getC();
								
								tmp3_x=l2;
								tmp3_y=yp2;
								tmp3_tipo=pezzoCorrente2.getTipo();
								tmp3_colore=pezzoCorrente2.getC();
		
							}
							else if(mat[x][y]== pezzoCorrente && mat[x][y]!= pezzoCorrente2) {
								
								int xp2=pezzoCorrente2.getX();
								int yp2=pezzoCorrente2.getY();
								int l2=g.restituisciCoordinataX(xp2,yp2,pezzoCorrente2);
								tmp2_x=l2;
								tmp2_y=yp2;
								tmp2_tipo=pezzoCorrente2.getTipo();
								tmp2_colore=pezzoCorrente2.getC();
							}
							else if(mat[x][y]!= pezzoCorrente && mat[x][y]== pezzoCorrente2) {
								
								int xp=pezzoCorrente.getX();
								int yp=pezzoCorrente.getY();
								int l1=g.restituisciCoordinataX(xp,yp,pezzoCorrente);
								tmp2_x=l1;
								tmp2_y=yp;
								tmp2_tipo=pezzoCorrente.getTipo();
								tmp2_colore=pezzoCorrente.getC();
								
							}
						}
		
					}
					g.repaint();
				}	
		
			}
			
			
			
			/////////////////////////////////
			ArrayList<String> mosse=new ArrayList<String>();
		
			public ArrayList<String> getMosse(){ return mosse;}
		
		
			public	synchronized void intelligenz() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException  {
				facts= new ASPInputProgram();
				program= new ASPInputProgram();
		
				g.setMat(mat);
				
				for(int i=1;i<nrow;i++)
						for(int j=1;j<ncol;j++){
							if(mat[i][j]!=null){
								try {
									String colore;
									if(mat[i][j].getC()==Color.RED)
										colore="rosso";
									else
										colore="verde";
									facts.addObjectInput(new Piece(mat[i][j].getX(),j,mat[i][j].getTipo(),colore));  
								} catch (Exception e) {
									e.printStackTrace();
								}
		  				j+= mat[i][j].getTipo()-1;
									
							}
							
						
				}
		
		
		
		
				try {
					ASPMapper.getInstance().registerClass(SpostaP.class);
					ASPMapper.getInstance().registerClass(Piece.class);
					
				} catch (ObjectNotValidException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAnnotationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				handler.addProgram(facts);	
				program.addFilesPath(encodingResource);
				handler.addProgram(program);
				
		
				o =  handler.startSync();
				answers = (AnswerSets) o;
		
		
		
			}
		
			public synchronized  void spostaConIntelligenza() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		
				if( p==null || !p.isAlive()) {
					
					intelligenz(); 
		
					Piece pezzogen=new Piece();		
					SpostaP spostap = new SpostaP();
					for(AnswerSet a:answers.getAnswersets()){
						System.out.println(a.getAnswerSet());
						for(Object obj:a.getAtoms()){
						
							if(obj instanceof SpostaP) {
		
								spostap = (SpostaP) obj;
								sposta(spostap.getX(),spostap.getY(),spostap.getX2(),spostap.getY2());
								String testo="sposta il pezzo: "+Integer.toString(spostap.getX())+","+Integer.toString(spostap.getY())+"nella colonna: "+Integer.toString(spostap.getX2())+" "+Integer.toString(spostap.getY2());
		
								System.out.println("sposta("+spostap.getX()+","+spostap.getY()+","+spostap.getX2()+","+spostap.getY2()+")");

							}
						}
		
					}
		
		
					facts.clearAll();
					handler.removeProgram(program);			
				}
				
			}
		
	
			public void generaMosse() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		
				intelligenz(); 
		
				SpostaP spostap = new SpostaP();
				for(AnswerSet a:answers.getAnswersets()){
					System.out.println(a.getAnswerSet());
					for(Object obj:a.getAtoms()){
						
					if(obj instanceof SpostaP) {
		
							spostap = (SpostaP) obj;
							String testo="sposta il pezzo in coordinate : "+Integer.toString(spostap.getX())+","+Integer.toString(spostap.getY())+"\n in coordinate: "+Integer.toString(spostap.getX2())+" "+Integer.toString(spostap.getY2());
							//System.out.println(testo);
							mosse.add(testo);
							System.out.println("sposta("+spostap.getX()+","+spostap.getY()+","+spostap.getX2()+","+spostap.getY2()+")");

		
		
		
						}
					}
		
				}
		
				//	handler.removeProgram(facts);
				facts.clearAll();
				handler.removeProgram(program);		
		
			}
		
	
		}
		
		
		