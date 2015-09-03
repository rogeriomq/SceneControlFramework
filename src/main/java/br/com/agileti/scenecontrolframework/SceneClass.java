package br.com.agileti.scenecontrolframework;

import javafx.scene.*;

public class SceneClass
{
    private String titleStage;
    private String resourceScreen;
    private Parent loadedScreen;
    private Object controller;
    
    public SceneClass(final String titleStage, final String resourceScreen, final Parent loadedScreen, final Object controller) {
        this.titleStage = titleStage;
        this.resourceScreen = resourceScreen;
        this.loadedScreen = loadedScreen;
        this.controller = controller;
    }
    
    public String getTitleStage() {
        return this.titleStage;
    }
    
    public void setTitleStage(final String titleStage) {
        this.titleStage = titleStage;
    }
    
    public String getResourceScreen() {
        return this.resourceScreen;
    }
    
    public void setResourceScreen(final String resourceScreen) {
        this.resourceScreen = resourceScreen;
    }
    
    public Parent getLoadedScreen() {
        return this.loadedScreen;
    }
    
    public void setLoadedScreen(final Parent loadedScreen) {
        this.loadedScreen = loadedScreen;
    }
    
    public Object getController() {
        return this.controller;
    }
    
    public void setController(final Object controller) {
        this.controller = controller;
    }
    
    @Override
    public String toString() {
        return "SceneClass{titleStage=" + this.titleStage + ", resourceScreen=" + this.resourceScreen + ", loadedScreen=" + this.loadedScreen + ", controller=" + this.controller + '}';
    }
}
