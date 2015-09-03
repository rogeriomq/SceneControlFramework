/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agileti.scenecontrolframework;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author man1gold
 */
public class StageFX extends Stage {

    public StageFX() {
    }

    public void closeWithEfect(Duration duratioFadeOut) {
        final DoubleProperty opacity = opacityProperty();
        setOpacity(1.0);
        opacity.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if(newValue.doubleValue() == 0.0) {
                super.hide();
            }
        });
        Timeline fadeOut = new Timeline(
            new KeyFrame(duratioFadeOut, new KeyValue(opacity, 0)));
        fadeOut.play();
    }

    public void showWithEfect(Duration durationFadeIn) {
        final DoubleProperty opacity = opacityProperty();
        setOpacity(0.0);
        Timeline fadeIn = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(opacity, 0.0)),
            new KeyFrame(durationFadeIn,
                new KeyValue(opacity, 1.0)));
        fadeIn.play();
        super.show();
    }

    public void showAndWaitWithEfect(Duration durationFadeIn) {
        final DoubleProperty opacity = opacityProperty();
        setOpacity(0.0);
        Timeline fadeIn = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(opacity, 0.0)),
            new KeyFrame(durationFadeIn,
                new KeyValue(opacity, 1.0)));
        fadeIn.play();
        super.showAndWait();
    }
}
