package com.example.mypuzzl.client;


import com.example.mypuzzl.HelloApplication;
import com.example.mypuzzl.sharedInterface.GameConfiguration;
import com.example.mypuzzl.sharedInterface.Rules;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class FieldController {

    public GameConfiguration gameConfiguration;
    public Rules rulesClient;
    public int choosenPosition;
    public int currentTime;
    public Timer gameTimer;

    @FXML
    public GridPane fieldGrid;
    @FXML
    public Label timeLabel;

    public Stage stage;

    public void getSettings(GameConfiguration gameConfiguration, Stage stage, Rules rulesClient) {

        System.out.println("New game _____________________________________");
        System.out.println(gameConfiguration.getCardsEmount());
        System.out.println(gameConfiguration.getLevel());
        System.out.println(gameConfiguration.getTheme());

        this.choosenPosition = -1;
        this.currentTime = 0;
        this.stage = stage;
        this.gameConfiguration = gameConfiguration;
        this.rulesClient = rulesClient;

        createField(gameConfiguration.getLevel(), gameConfiguration.getTheme());

    }

    private void createField(int level, String theme) {

        try {
            var a = rulesClient.GenerateArrangement(level, theme);
            for (var b : a) {
                System.out.println(b);
            }
            gameConfiguration.setArrangement(a);
            gameConfiguration.setTime(rulesClient.GetGameTime(level));

            System.out.println("time = " + gameConfiguration.getTime());
//			for (String s : gameConfiguration.getArrangement()) {
//				System.out.println(s);
//			}


            for (int i = 0; i < gameConfiguration.getCardsEmount() / 2 - 2; i++) {
//				System.out.println("add column");
                ColumnConstraints columnConstraint = new ColumnConstraints();
                columnConstraint.setMinWidth((level == 2) ? 260 : 200);
                fieldGrid.getColumnConstraints().add(columnConstraint);
            }

//			System.out.println("getCardsEmount = "+gameConfiguration.getCardsEmount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < gameConfiguration.getCardsEmount() / 2; j++) {


                    int size = level == 1 ? 160 : 130;


                    int index = (i * (gameConfiguration.getCardsEmount() / 2) + j);
                    Button button = new Button();

                    button.setMaxWidth(size);
                    button.setMinWidth(size);
                    button.setMaxHeight(size);
                    button.setMinHeight(size);
                    button.getStyleClass().add("card");
                    String styles = "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #a4e3f5, #00d4ff); -fx-border-color: #CBCBCB;\r\n" + "    -fx-border-width: 1;";

                    button.setStyle(styles);
                    button.setId("button" + index);
                    button.setOnAction(event -> ChooseCard(event, index));

                    fieldGrid.add(button, j, i);
                    GridPane.setHalignment(button, HPos.CENTER);
                    GridPane.setMargin(button, new Insets(10));
//					System.out.println(i + " - add column - "+ j);

                }
            }


            setTimer();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    private void SetButtonPicture(Button currentButton, String path) {
        currentButton.setStyle("-fx-background-color: #ffffff; ");
//        var fullPath = "src/main/java/com.example.mypuzzl"+path;

        Image img = new Image(HelloApplication.class.getResourceAsStream(path));
        ImageView view = new ImageView(img);
        view.setFitHeight(110);
        view.setPreserveRatio(true);
        currentButton.setGraphic(view);
//		System.out.println(currentButton);
    }

    private void ChooseCard(ActionEvent event, int index) {

        //"переворачиваю" выбранную карточку

        Button currentButton = (Button) event.getSource();
        System.out.println(this.gameConfiguration.toString());
        SetButtonPicture(currentButton, this.gameConfiguration.getImage(index));
        System.out.println("click");


        if (this.choosenPosition == -1) {
            this.choosenPosition = index;

        } else {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            try {
                boolean similar = rulesClient.CheckCardPair(this.gameConfiguration, this.choosenPosition, index);

                if (similar) {

                    for (Node node : fieldGrid.getChildren()) {

                        if (node.getId() != null && node.getId().equals("button" + this.choosenPosition)) {
                            Button choosenBtn = (Button) node;
                            choosenBtn.setDisable(true);

                        }
                    }
                    currentButton.setDisable(true);
                    this.gameConfiguration.setCardsEmount(this.gameConfiguration.getCardsEmount() - 2);
                    this.choosenPosition = -1;

//					System.out.println("CardsEmount = "+this.gameConfiguration.getCardsEmount());

                    if (this.gameConfiguration.getCardsEmount() == 0) {
                        this.gameTimer.cancel();
//						System.out.println(this.gameTimer.getClass().getClass().getFields());
                        showResults(true, currentTime);
                    }
                } else {
                    setBaseBackground(currentButton);
                }


            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void setBaseBackground(Button currentButton) {

        String styles = "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #a4e3f5, #00d4ff); -fx-border-color: #CBCBCB;\r\n" + "    -fx-border-width: 1;";

        for (Node node : fieldGrid.getChildren()) {
            if (node.getId() != null && node.getId().equals("button" + this.choosenPosition)) {
                Button choosenBtn = (Button) node;

                Platform.runLater(() -> {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    choosenBtn.setGraphic(null);
                    choosenBtn.setStyle(styles);
                });

            }
        }

        Platform.runLater(() -> {
            currentButton.setGraphic(null);
            currentButton.setStyle(styles);
        });
        this.choosenPosition = -1;
    }

    private void setTimer() {

        gameTimer = new Timer();


        gameTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Platform.runLater(() -> {
                    currentTime += 1;
                    String total = String.format("Осталось: %d секунд", gameConfiguration.getTime() - currentTime);

                    if (gameConfiguration.getTime() - currentTime == 0) {
                        this.cancel();
                        showResults(false, 0);
                    }

                    timeLabel.setText(total);
                });
            }

        }, 0, 1000);

//		System.out.println(this.gameTimer.getClass().getClass().getFields());
    }

    private void showResults(boolean resultType, int totalTyme) {

//		timeLabel.setText("Завершение игры");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Завершение игры");

        if (resultType) {
            alert.setHeaderText(String.format("Победа! Время вашей игры составило %d с. Хотите повторить игру с предыдущими настройками?", totalTyme));
        } else alert.setHeaderText("Неудача! Время вышло. Хотите попробовать еще раз?");

        ButtonType playAgain = new ButtonType("Попробовать еще раз");
        ButtonType cansel = new ButtonType("Перейти к экрану настроек");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(playAgain, cansel);

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
            System.out.println("no selection");
        } else if (option.get() == playAgain) {

            System.out.println("playAgain");
            try {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("field.fxml"));
                Parent root = loader.load();
//				System.out.println(loader);
//				System.out.println(root);
                FieldController fieldController = loader.getController();
//				System.out.println(fieldController);
                this.gameConfiguration.setStartSettings(this.gameConfiguration.getTheme(), this.gameConfiguration.getLevel());
                fieldController.getSettings(this.gameConfiguration, stage, this.rulesClient);
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.show();


            } catch (IOException e) {

                e.printStackTrace();
            }

        } else if (option.get() == cansel) {
            System.out.println("cansel");
            try {
                Parent root = FXMLLoader.load(HelloApplication.class.getResource("settings.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(HelloApplication.class.getResource("application.css").toExternalForm());

                stage.setScene(scene);
                stage.show();


            } catch (IOException e) {

                e.printStackTrace();
            }
        } else {
            System.out.println("no selection");
        }
    }
}

//
//if (similar) {
//	
//	Scene scene = ((Node)event.getSource()).getScene();
//	System.out.println(scene);
////	Button choosenBtn = (Button)scene.lookup("button0"+index);
//	for (Node node : fieldGrid.getChildren()) {
//		System.out.println(node);
//		System.out.println(node.getId());
//		if (node.getId()!=null&&(node.getId().equals("button"+index)||node.getId().equals("button"+this.choosenPosition)))
//		{
//			Button choosenBtn = (Button)node;
//			choosenBtn.setDisable(true);
//			choosenBtn.setStyle("-fx-background-color: #FF0000; ");
//		}
//	}
//	//обе картинки
//	this.choosenPosition=-1;
////	
//}


//	    	Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev ->
//	    	Tick()));

//	    	{
//	    		currentTime+=1;
//	    		System.out.println(gameConfiguration.getTime()-currentTime);
////    			String total = String.format("Осталось: %d секунд", gameConfiguration.getTime()-this.currentTime);
////    			timeLabel.setText(total);
//		    }

//		    timeline.setCycleCount(Animation.INDEFINITE);
//		    timeline.play();
//		    timeline.