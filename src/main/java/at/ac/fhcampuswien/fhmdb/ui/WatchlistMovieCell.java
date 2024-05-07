package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.interfaces.ClickEventHandler;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class WatchlistMovieCell extends ListCell<WatchlistMovieEntity> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final JFXButton removeBtn = new JFXButton("Remove");
    private final HBox buttons = new HBox(removeBtn);
    private final VBox layout = new VBox(title, detail, genre, buttons);

    public WatchlistMovieCell(ClickEventHandler removeFromWatchlist) {
        super();

        // Styling
        removeBtn.setStyle("-fx-background-color: #f5c518;");
        title.getStyleClass().add("text-yellow");
        detail.getStyleClass().add("text-white");
        genre.getStyleClass().add("text-white");
        genre.setStyle("-fx-font-style: italic");
        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

        // Layout
        title.setFont(title.getFont().font(20));
        detail.setWrapText(true);
        layout.setPadding(new Insets(10));
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER_LEFT);
        buttons.setPadding(new Insets(10));
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.TOP_RIGHT);

        removeBtn.setOnMouseClicked(mouseEvent -> {
            try {
                removeFromWatchlist.onClick(getItem());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void updateItem(WatchlistMovieEntity movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setGraphic(null);
            setText(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );

            String genres = movie.getGenres();


            genre.setText(genres);

            setGraphic(layout);
        }
    }
}
