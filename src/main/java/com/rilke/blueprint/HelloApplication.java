package com.rilke.blueprint;

import com.rilke.blueprint.util.Util;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    // TODO: 스크롤과 확대/축소 기능 분리
    // TODO: 여백 조정
    // TODO: 사각형 그리면 몇 mm 인지 표시하는 기능
    // TODO: 사각형을 그릴 때, 마우스를 누르고 드래그하면 사각형이 그려지도록
    // TODO: 그려진 사각형을 드래그해서 크기를 조절할 수 있도록


    private Canvas canvas;

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

        // Canvas 를 ScrollPane 에 넣고 중앙에 배치
        ScrollPane canvasScrollPane = new ScrollPane(canvas);
        canvasScrollPane.setPrefSize(screenWidth, screenHeight);
        canvasScrollPane.setLayoutX((screenWidth - canvas.getWidth()) / 2);
        canvasScrollPane.setLayoutY((screenHeight - canvas.getHeight()) / 2);
        root.getChildren().add(canvasScrollPane);

        // 버튼 생성 및 설정
        HBox buttonBox = new HBox(10); // 수평으로 버튼을 나열하는 HBox
        buttonBox.setAlignment(Pos.CENTER); // 가운데 정렬
        buttonBox.setPadding(new Insets(100)); // 여백 설정

        Button drawLineButton = new Button("Line");
        drawLineButton.setOnAction(event -> util.startFillingCells());

        Button clearButton = new Button("All Clear");
        clearButton.setOnAction(event -> util.clearCanvas());

        Button eraseButton = new Button("Cell Clear");
        eraseButton.setOnAction(event -> util.startErasingCells());

        buttonBox.getChildren().addAll(drawLineButton, clearButton, eraseButton);
        root.getChildren().add(buttonBox);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setTitle("BluePrint");
        stage.setScene(scene);

        // Canvas 의 확대/축소를 위한 스크롤 이벤트 핸들링
        canvasScrollPane.setOnScroll((event) -> {
            if (event.isControlDown()) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();

                if (deltaY < 0) {
                    zoomFactor = 0.95;
                }

                canvas.setScaleX(canvas.getScaleX() * zoomFactor);
                canvas.setScaleY(canvas.getScaleY() * zoomFactor);

                event.consume();
            }
        });

        stage.show();
    }


}

