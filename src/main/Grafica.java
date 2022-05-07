package main;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Grafica implements Initializable {

	@FXML
	private SplitPane splite;
	@FXML
	private AnchorPane milena;
	@FXML
	public Text punti;
	@FXML
	private TextArea suggerimenti;

	public static int dimPezzo=35;
	public static int x,y;
	public static int coord_x,coord_y;
	public static int c_x,c_y;
	static int dimx=12;
	static int dimy=24;


	private Thread thread;
	private Int intelligenza;
	private static  Game game;





	@Override
	public void initialize(URL location, ResourceBundle resources) {
		game= new Game(12, 24);
		intelligenza=new Int(game);
				
		milena.getChildren().add(game.g);  
	
		
		splite.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				c_x=(int) (e.getSceneY()/(dimPezzo+1));
				c_y=(int) (e.getSceneX()/(dimPezzo+1));
				game.muoviMouse(x, y,c_x, c_y);

			}
		});

		splite.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {

				x=(int) (e.getSceneY()/(dimPezzo+1));
				y=(int) (e.getSceneX()/(dimPezzo+1));


			}
		});


		splite.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {

				coord_x=(int) (e.getSceneY()/(dimPezzo+1));
				coord_y=(int) (e.getSceneX()/(dimPezzo+1));
				
				game.sposta(x, y,coord_x, coord_y);		        	
				punti.setText(String.valueOf(game.getPunteggio()));
				}
			
		});




	}

	@FXML
	void generaSuggerimento(MouseEvent event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		game.generaMosse();
		ArrayList<String> mosse=new ArrayList<String>();
		mosse=game.getMosse();
		for(int i=0; i<mosse.size(); i++)
			suggerimenti.setText(mosse.get(i));
	//	game.S();
	}


	@FXML
	void pausa(MouseEvent event) {
		intelligenza.stop();
	}

	@FXML
	void play(MouseEvent event) {
		thread=new Thread(intelligenza);
		thread.start();
	}

	@FXML
	void unaSolaMossa(MouseEvent event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		game.spostaConIntelligenza();
	}

	
}
