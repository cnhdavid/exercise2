package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.interfaces.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistMovieCell;
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
import java.sql.SQLException;
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
        watchlistRepository = new WatchlistRepository(); // Make sure this is initialized
        updateWatchlistView(); // Load and display data in the ListView
    }

    private void updateWatchlistView() {
        try {
            List<WatchlistMovieEntity> watchlistMovies = watchlistRepository.getWatchlist(); // Get the current watchlist
            watchlistListView.getItems().clear(); // Clear old entries
            watchlistListView.setCellFactory(watchlistListView -> new WatchlistMovieCell(removeFromWatchlist)); // Set custom cells
            watchlistListView.getItems().addAll(watchlistMovies); // Add new entries
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error message if necessary
        }
    }

    // Click event handler for removing a movie from the watchlist
    private final ClickEventHandler removeFromWatchlist = clickedItem -> {
        try {
            WatchlistMovieEntity movie = (WatchlistMovieEntity) clickedItem;
            watchlistRepository.removeFromWatchlist(movie.getId()); // Remove movie from watchlist
            updateWatchlistView(); // Update the watchlist view after removal
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential errors
        }
    };

    @FXML
    public void returnToHomeScreen(ActionEvent actionEvent) {
        try {
            // Load the home view FXML file
            FXMLLoader fxmlLoader1 = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
            Parent homeViewRoot = fxmlLoader1.load();

            // Get the current stage (window) from the event source
            Stage currentStage = (Stage) homeBtn.getScene().getWindow();
            currentStage.setWidth(890);
            currentStage.setHeight(620);

            // Set the root of the current scene to the home view root
            currentStage.getScene().setRoot(homeViewRoot);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors (e.g., FXML file not found)
        }
    }
}
