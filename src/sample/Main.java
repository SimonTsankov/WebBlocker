package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                System.out.println(Controller.backup);
                    if(Controller.ProductiveMode) {
                        JOptionPane.showMessageDialog(null, "Productive mode disabled :))");
                        try {
                            writeFile(Controller.backup);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                Platform.exit();
                System.exit(0);
            }
        });
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Web Blocker");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public  void writeFile(String text) throws IOException {
        FileWriter writer = new FileWriter("C:\\Users\\User\\IdeaProjects\\WebBlocker\\src\\hosts.txt");
        writer.write(text);
        writer.close();
    }
}
