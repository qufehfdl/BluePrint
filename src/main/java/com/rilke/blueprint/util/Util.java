package com.rilke.blueprint.util;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Util {
    private final double cellSize = 5; // 기본 셀 크기
    private final Canvas canvas;
    private final GraphicsContext gc;


    public Util(Canvas canvas, GraphicsContext gc) {
        this.canvas = canvas;
        this.gc = gc;
    }

    public void drawGrid(Canvas canvas, double screenWidth, double screenHeight) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // 캔버스 초기화

        // 중앙 기준으로 그리드를 계산
        double halfWidth = canvas.getWidth() / 2;
        double halfHeight = canvas.getHeight() / 2;

        for (double x = -halfWidth; x < halfWidth; x += cellSize) {
            gc.strokeLine(halfWidth + x, 0, halfWidth + x, canvas.getHeight());
        }

        for (double y = -halfHeight; y < halfHeight; y += cellSize) {
            gc.strokeLine(0, halfHeight + y, canvas.getWidth(), halfHeight + y);
        }
    }

    public void startFillingCells() {
        canvas.setOnMousePressed(event -> {
            fillCell(event.getX(), event.getY());

            canvas.setOnMouseDragged(e -> fillCell(e.getX(), e.getY()));
        });

        canvas.setOnMouseReleased(e -> {
            canvas.setOnMouseDragged(null);
            canvas.setOnMouseReleased(null);
        });
    }

    public void fillCell(double x, double y) {
        double cellX = Math.round(x / cellSize) * cellSize;
        double cellY = Math.round(y / cellSize) * cellSize;

        gc.setFill(Color.BLACK);
        gc.fillRect(cellX, cellY, cellSize, cellSize);
    }

    public void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawGrid(canvas, canvas.getWidth(), canvas.getHeight());
    }

    public void startErasingCells() {
        canvas.setOnMousePressed(event -> {
            eraseCell(event.getX(), event.getY());

            canvas.setOnMouseDragged(e -> eraseCell(e.getX(), e.getY()));
        });

        canvas.setOnMouseReleased(e -> {
            canvas.setOnMouseDragged(null);
            canvas.setOnMouseReleased(null);
        });
    }

    public void eraseCell(double x, double y) {
        // 클릭 또는 드래그한 위치의 셀 좌표 계산
        double cellX = Math.floor(x / cellSize) * cellSize;
        double cellY = Math.floor(y / cellSize) * cellSize;

        // 해당 셀만 지우기
        gc.clearRect(cellX, cellY, cellSize, cellSize);
        // 해당 셀만 격자 다시 그리기
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeRect(cellX, cellY, cellSize, cellSize);
    }


}
