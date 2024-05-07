package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.interfaces.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label directors = new Label();
    private final Label writers = new Label();
    private final Label mainCast = new Label();
    private final Label rating = new Label();
    private final Label releaseYear = new Label();
    private final VBox layout = new VBox(title,releaseYear, detail, genre, directors, writers, mainCast, rating);
    private Button watchlistBtn = new Button("Watchlist");
    private Button removeBtn = new Button("Remove");
    private final JFXButton detailBtn = new JFXButton("Show Details");



    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setGraphic(null);
            setText(null);
        } else {
            // Clear existing children
            layout.getChildren().clear();
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            releaseYear.setText(movie.getReleaseYear());

            String genres = movie.getGenres()
                    .stream()
                    .map(Enum::toString)
                    .collect(Collectors.joining(", "));
            genre.setText("Genres: " + genres);

            String directorsText = (String) movie.getDirectors()
                    .stream()
                    .collect(Collectors.joining(", "));
            directors.setText("Directors: " + directorsText);

            String writersText = (String) movie.getWriters()
                    .stream()
                    .collect(Collectors.joining(", "));
            writers.setText("Writers: " + writersText);

            String mainCastText = movie.getMainCast()
                    .stream()
                    .collect(Collectors.joining(", "));
            mainCast.setText("Main Cast: " + mainCastText);

            rating.setText("Rating: " + movie.getRating());



            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            releaseYear.getStyleClass().add("text-white");
            releaseYear.setStyle("-fx-font-style: italic");
            genre.getStyleClass().add("text-white");
            genre.setStyle("-fx-font-style: italic");
            directors.getStyleClass().add("text-white");
            directors.setStyle("-fx-font-style: italic");
            writers.getStyleClass().add("text-white");
            writers.setStyle("-fx-font-style: italic");
            mainCast.getStyleClass().add("text-white");
            mainCast.setStyle("-fx-font-style: italic");
            rating.getStyleClass().add("text-white");
            rating.setStyle("-fx-font-style: italic");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.fontProperty().set(title.getFont().font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.setAlignment(Pos.CENTER_LEFT);
            layout.getChildren().addAll( title, detail, releaseYear,  genre, directors, writers, mainCast, rating);
            setGraphic(layout);
        }
    }

}
