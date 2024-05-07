// WatchlistController.java
package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable {

    @FXML
    public JFXButton homeBtn;
    @FXML
    private ListView<WatchlistMovieEntity> watchlistListView;

    private WatchlistRepository watchlistRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize watchlist view
        // For example: Load watchlist data and populate TableView


    }






    @FXML
    public void returnToHomeScreen(ActionEvent actionEvent) {
        try {
            // Load the home view FXML file
            FXMLLoader fxmlLoader1 = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
            Parent homeViewRoot = fxmlLoader1.load();

            // Get the current stage (window) from the event source
            Stage currentStage = (Stage) homeBtn.getScene().getWindow();
            currentStage.setWidth(820);
            currentStage.setHeight(620);

            // Set the root of the current scene to the home view root
            currentStage.getScene().setRoot(homeViewRoot);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors (e.g., FXML file not found)
        }
    }
    }

