import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App extends Application
{
    private Label resultLabel;
    private int lapNumber = 0;

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private Label main_clock_lb = new Label();
    private Label speed = new Label("<Speed Goes here>");
    private Label lapCounterLabel = new Label("" + 0 + "");
    private Label listOfLapsLabel = new Label();


    private String listOfLaps[] = new String[10];

    private Button lapIncrease = new Button("Lap Increase");
    private Button lapDecrease = new Button("Lap Decrease");
    private Button lapReset = new Button("Press to wipe lap data");

    private float distanceOfLap[] = {0,0,0,0,0,0,0,0,0,0};
    private float getDistance;

    private float avgSpeedOfLap[] = {0,0,0,0,0,0,0,0,0,0};
    private float getAvgSpeed;

    String start;
    String end;
    

//______________________________________________________________________________MODEL
    @Override
    public void start(Stage primaryStage)
    {

        primaryStage.setTitle("SAE Speedometer V0.2");
        setButtons();
        setGetClock();
        getSerialEvent();
        setScene(primaryStage);
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }


//______________________________________________________________________________VIEW

    public void setScene(Stage primaryStage){

        lapCounterLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
        main_clock_lb.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
        speed.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
        listOfLapsLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));

        VBox increaseDecrease = new VBox(20, lapIncrease, lapDecrease);
        HBox lapCounter = new HBox(20, increaseDecrease, lapCounterLabel, lapReset);


        VBox vBox = new VBox(50, main_clock_lb, speed, lapCounter, listOfLapsLabel);
        HBox hBox = new HBox(20,vBox);

        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.BASELINE_CENTER);

        Scene scene = new Scene(hBox);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void setButtons(){
        lapIncrease.setOnAction(new lapIncreaseHandler());
        lapDecrease.setOnAction(new lapDecreaseHandler());
        lapReset.setOnAction(new lapResetHandler());

        lapIncrease.setPrefSize(100,50);
        lapDecrease.setPrefSize(100, 50);

        lapIncrease.setLayoutX(45);
        lapIncrease.setLayoutY(45);
    }

    public void setGetClock(){

        Thread timerThread = new Thread(() -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            while (true) {
                try {
                    Thread.sleep(500); //1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final String time = simpleDateFormat.format(new Date());
                Platform.runLater(() -> {
                    main_clock_lb.setText(time);
                });
            }
        });   timerThread.start();//start the thread and its ok

        this.resultLabel = new Label();
    }

    public void getSerialEvent(){

        // here you should get the input from the arduino
        getDistance = 1000+getDistance;
        getAvgSpeed = 10+getAvgSpeed;

        distanceOfLap[lapNumber] = getDistance;
        System.out.println(distanceOfLap[lapNumber]);

        avgSpeedOfLap[lapNumber] = getAvgSpeed;
        System.out.println(avgSpeedOfLap[lapNumber]);

    }


//______________________________________________________________________________CONTROLLER
    class lapIncreaseHandler implements EventHandler<ActionEvent>
    {

        @Override
        public void handle(ActionEvent event)
        {
            lapNumber += 1;
            lapCounterLabel.setText("" +lapNumber+ "");
            if(distanceOfLap[lapNumber] == 0){
                getSerialEvent();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                end = sdf.format(new Date());
                System.out.println(end);

                start = sdf.format(new Date());

                listOfLaps[lapNumber] = " " + Integer.toString(lapNumber) + "\t" + end + "\n";

                switch(lapNumber){
                    case 1:
                        listOfLapsLabel.setText("" + listOfLaps[1] + "");
                        break;
                    case 2:
                        listOfLapsLabel.setText("" + listOfLaps[1] + listOfLaps[2] + "");
                        break;
                    case 3:
                        listOfLapsLabel.setText("" + listOfLaps[1] + listOfLaps[2] + listOfLaps[3] + "");
                        break;
                    case 4:
                        listOfLapsLabel.setText(""+listOfLaps[1] + listOfLaps[2] + listOfLaps[3]+listOfLaps[4]+"");
                        break;
                    case 5:
                        listOfLapsLabel.setText("" +listOfLaps[1] + listOfLaps[2] + listOfLaps[3]+listOfLaps[4] + listOfLaps[5] + "");
                        break;
                    case 6:
                        listOfLapsLabel.setText("" +listOfLaps[1] + listOfLaps[2] + listOfLaps[3]+listOfLaps[4] + listOfLaps[5] + listOfLaps[6] + "");
                        break;
                    case 7:
                        listOfLapsLabel.setText("" +listOfLaps[1] + listOfLaps[2] + listOfLaps[3]+listOfLaps[4] + listOfLaps[5] + listOfLaps[6] + listOfLaps[7] + "");
                        break;
                    case 8:
                        listOfLapsLabel.setText(""+listOfLaps[1] + listOfLaps[2] + listOfLaps[3]+listOfLaps[4] + listOfLaps[5] + listOfLaps[6] + listOfLaps[7] + listOfLaps[8]+"");
                        break;
                }
            }
            else{
                System.out.println(distanceOfLap[lapNumber]);
                System.out.println(avgSpeedOfLap[lapNumber]);
                // update the label associated with this
            }
        }
    }

    class lapDecreaseHandler implements EventHandler<ActionEvent>
    {

        @Override
        public void handle(ActionEvent event)
        {
            lapNumber -= 1;
            lapCounterLabel.setText("" +lapNumber+ "");
            System.out.println(distanceOfLap[lapNumber]);
            System.out.println(avgSpeedOfLap[lapNumber]);
        }
    }

    class lapResetHandler implements EventHandler<ActionEvent>
    {

        @Override
        public void handle(ActionEvent event)
        {
            lapNumber = 0;
            getDistance = 0;
            getAvgSpeed = 0;
            lapCounterLabel.setText("" +lapNumber+ "");
            for(int i = 0; i < 10; i++){
                distanceOfLap[i] = 0;
                avgSpeedOfLap[i] = 0;
                listOfLaps[i] = "";
                listOfLapsLabel.setText("");
            }
        }
    }
}
