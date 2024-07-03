package com.rilke.blueprint;

import com.rilke.blueprint.util.Util;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private Canvas canvas;
    private StackPane canvasContainer;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        VBox root = fxmlLoader.load();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        // Canvas 와 GraphicsContext 설정
        canvas = new Canvas(screenWidth, screenHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.LIGHTGRAY);
        Util util = new Util(canvas, gc);
        util.drawGrid(canvas, screenWidth, screenHeight);

        // StackPane을 사용하여 Canvas를 감쌈
        canvasContainer = new StackPane(canvas);
        canvasContainer.setMinSize(screenWidth, screenHeight);

        // Canvas를 ScrollPane에 넣고 중앙에 배치
        ScrollPane canvasScrollPane = new ScrollPane(canvasContainer);
        canvasScrollPane.setPrefSize(screenWidth, screenHeight);

        // 버튼 생성 및 설정
        HBox buttonBox = new HBox(10); // 수평으로 버튼을 나열하는 HBox
        buttonBox.setAlignment(Pos.CENTER); // 가운데 정렬


        Button drawLineButton = new Button("Line");
        drawLineButton.setOnAction(event -> util.startFillingCells());

        Button clearButton = new Button("All Clear");
        clearButton.setOnAction(event -> util.clearCanvas());

        Button eraseButton = new Button("Cell Clear");
        eraseButton.setOnAction(event -> util.startErasingCells());

        // 풀스크린 버튼
        Button fullScreenButton = new Button("Full Screen");
        fullScreenButton.setOnAction(event -> stage.setFullScreen(true));
        buttonBox.getChildren().add(fullScreenButton);


        buttonBox.getChildren().addAll(drawLineButton, clearButton, eraseButton);

        // root VBox에 buttonBox를 첫 번째 자식으로 추가하고 canvasScrollPane을 그 아래에 추가
        root.getChildren().add(0, buttonBox);
        root.getChildren().add(1, canvasScrollPane);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setTitle("BluePrint");
        stage.setScene(scene);

        canvasScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                double zoomFactor = 1.2;
                double deltaY = event.getDeltaY();
                if (deltaY < 0) {
                    zoomFactor = 0.85;
                }
                double oldScale = canvas.getScaleX();
                double newScale = oldScale * zoomFactor;

                // Scale limit
                if (newScale < 0.5) {
                    newScale = 0.5;
                }
                canvas.setScaleX(newScale);
                canvas.setScaleY(newScale);

                // Adjust the ScrollPane's view position
                double f = (newScale / oldScale) - 1;
                double dx = (event.getX() - (canvasScrollPane.getViewportBounds().getWidth() / 2 + canvasScrollPane.getHvalue() * (canvas.getBoundsInParent().getWidth() - canvasScrollPane.getViewportBounds().getWidth())));
                double dy = (event.getY() - (canvasScrollPane.getViewportBounds().getHeight() / 2 + canvasScrollPane.getVvalue() * (canvas.getBoundsInParent().getHeight() - canvasScrollPane.getViewportBounds().getHeight())));

                // Move the scroll bar to keep the zoom centered around the mouse position
                canvasScrollPane.setHvalue(canvasScrollPane.getHvalue() - f * dx / canvas.getBoundsInParent().getWidth());
                canvasScrollPane.setVvalue(canvasScrollPane.getVvalue() - f * dy / canvas.getBoundsInParent().getHeight());

                // Adjust the size of the canvasContainer
                canvasContainer.setMinSize(screenWidth * newScale, screenHeight * newScale);

                event.consume();
            }
        });
        stage.setFullScreen(true);
        stage.show();
    }
}

// TODO: 사각형 그리면 몇 mm 인지 표시하는 기능
// TODO: 사각형을 그릴 때, 마우스를 누르고 드래그하면 사각형이 그려지도록
// TODO: 그려진 사각형을 드래그해서 크기를 조절할 수 있도록