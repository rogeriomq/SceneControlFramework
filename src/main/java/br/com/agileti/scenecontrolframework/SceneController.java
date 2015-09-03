package br.com.agileti.scenecontrolframework;

import java.util.*;
import javafx.stage.*;
import javafx.util.*;
import javafx.fxml.*;
import java.util.logging.*;
import javafx.beans.value.*;
import javafx.beans.property.*;
import javafx.scene.*;
import javafx.animation.*;
import javafx.event.*;

/**
 *
 * @author man1gold
 */
public class SceneController
{

    /**
     *
     */
    public static final boolean NEW_SCREEN_INSTANCE = true;

    /**
     *
     */
    public static final boolean CURRENT_SCREEN_INSTANCE = false;
    private final HashMap<String, SceneClass> screens;
    private final Stage stage;
    private Duration durationFade;
    
    /**
     *
     * @param stage
     * @param durationFade
     */
    public SceneController(final Stage stage, final Duration durationFade) {
        this.screens = new HashMap<>();
        this.stage = stage;
        this.durationFade = durationFade;
    }
    
    /**
     *
     * @return
     */
    public Duration getDurationFade() {
        return this.durationFade;
    }
    
    /**
     *
     * @param durationFade
     */
    public void setDurationFade(final Duration durationFade) {
        this.durationFade = durationFade;
    }
    
    /**
     *
     * @param key
     * @param titleStage
     * @param screenResource
     */
    public void addScreen(final String key, final String titleStage, final String screenResource) {
        this.screens.put(key, new SceneClass(titleStage, screenResource, null, null));
        System.out.println("LOG_INFOS:" + this.screens.get(key).toString());
    }
    
    /**
     *
     * @param key
     * @param sceneClass
     */
    public void addScreen(final String key, final SceneClass sceneClass) {
        this.screens.put(key, sceneClass);
        System.out.println("LOG_INFOS:" + this.screens.get(key).toString());
    }
    
    /**
     *
     * @param key
     * @return
     */
    public SceneClass getScreen(final String key) {
        return this.screens.get(key);
    }
    
    /**
     *
     * @param key
     * @return
     * @throws Exception
     */
    public SceneClass getScreenLoaded(final String key) throws Exception {
        if (this.screens.get(key).getLoadedScreen() != null) {
            return this.screens.get(key);
        }
        if (this.loadScreen(key)) {
            return this.screens.get(key);
        }
        throw new Exception("Conteiner n\u00e3o carregado!");
    }
    
    /**
     *
     * @param key
     * @return
     * @throws Exception
     */
    public SceneClass getScreenReloaded(final String key) throws Exception {
        if (this.screens.get(key).getLoadedScreen() != null) {
            final SceneClass old = this.screens.get(key);
            this.screens.remove(key);
            this.addScreen(key, old.getTitleStage(), old.getResourceScreen());
        }
        if (this.loadScreen(key)) {
            return this.screens.get(key);
        }
        return null;
    }
    
    private boolean loadScreen(final String name) {
        try {
            final FXMLLoader myLoader = new FXMLLoader(this.getClass().getResource(this.screens.get(name).getResourceScreen()));
            final Parent loadScreen = (Parent)myLoader.load();
            this.screens.get(name).setLoadedScreen(loadScreen);
            this.screens.get(name).setController(myLoader.getController());
            return true;
        }
        catch (Exception e) {
            Logger.getLogger(SceneController.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }
    
    /**
     * 
     * @param key
     * @param typeInstance
     * @return
     */
    public boolean setScreen(final String key, final boolean typeInstance) {
        boolean ok;
        if (typeInstance) {
            ok = this.loadScreen(key);
            System.out.println("Go set New: [" + key + "] result " + ok);
        }
        else {
            ok = (this.screens.get(key).getLoadedScreen() != null);
            System.out.println("Go set Loaded: [" + key + "] result " + ok);
        }
        final double width = this.stage.getWidth();
        final double height = this.stage.getHeight();
        if (this.screens.get(key) != null && ok) {
            final DoubleProperty opacity = this.stage.opacityProperty();
            final Timeline fade = new Timeline(new KeyFrame[] { new KeyFrame(Duration.ZERO, new KeyValue[] { new KeyValue((WritableValue)opacity, (Object)1.0) }), new KeyFrame(this.durationFade, (EventHandler)new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent t) {
                        stage.setScene((Scene)null);
                        if (!typeInstance) {
                            stage.setScene(SceneController.this.screens.get(key).getLoadedScreen().getScene());
                        }
                        else {
                            stage.setScene(new Scene(SceneController.this.screens.get(key).getLoadedScreen()));
                        }
                        final Timeline fadeIn = new Timeline(new KeyFrame[] { new KeyFrame(Duration.ZERO, new KeyValue[] { new KeyValue((WritableValue)opacity, (Object)0.0) }), new KeyFrame(SceneController.this.durationFade, new KeyValue[] { new KeyValue((WritableValue)opacity, (Object)1.0) })});
                        fadeIn.play();
                        stage.setWidth(width);
                        stage.setHeight(height);
                    }
                }, new KeyValue[] { new KeyValue((WritableValue)opacity, (Object)0.0) }) });
            stage.setTitle(this.screens.get(key).getTitleStage());
            fade.play();
            return true;
        }
        System.err.println("screen não foi carregado / inexistente no mapa de screens!!! \n");
        this.stage.setScene((Scene)null);
        return false;
    }
    
    public Stage getCurrentStage(final String key) throws Exception {
        Parent p = screens.get(key).getLoadedScreen();
        if(p != null) {
            return (Stage)p.getScene().getWindow();
        } else{
            throw new Exception("Não existe stage associado ao loadedScreen!");
        }
    }
    
    public StageFX getCurrentStageFX(final String key) throws Exception {
        Parent p = screens.get(key).getLoadedScreen();
        if(p != null) {
            return (StageFX)p.getScene().getWindow();
        } else{
            throw new Exception("Não existe stage associado ao loadedScreen!");
        }
    }
    
    /**
     * Set Screen/SceneClass in Current StageFX( custom Efect in close and show ).
     * @param key - key do FXML a ser carregado e entregue em um novo Stage.
     * @param modality - Modality mode, use NULL caso seja o Stage Principal(PrimaryStage)
     * @return Stage, Caso o Scene não seja carregado, o stage será retornado sem nenhum senário
     */
    public StageFX buildScreenInNewStageFX(final String key, final Modality modality) {
        StageFX stageFX;
        stageFX = new StageFX();
        if(modality != null)
            stageFX.initModality(modality);
        boolean ok;
        ok = this.loadScreen(key);
        System.out.println("Go set New: [" + key + "] result " + ok);
        if (screens.get(key) != null && ok) {
            final DoubleProperty opacity = stageFX.opacityProperty();
            stageFX.setScene(new Scene(screens.get(key).getLoadedScreen()));
            stageFX.setTitle(screens.get(key).getTitleStage());
            final Timeline fadeIn = new Timeline(new KeyFrame[] { new KeyFrame(Duration.ZERO, new KeyValue[] { new KeyValue((WritableValue)opacity, (Object)0.0) }), new KeyFrame(SceneController.this.durationFade, new KeyValue[] { new KeyValue((WritableValue)opacity, (Object)1.0) }) });
            stageFX.onShowingProperty().addListener((javafx.beans.Observable observable) -> {
                fadeIn.play();
            });
            return stageFX;
        }
        System.err.println("screen n\u00e3o foi carregado / inexistente no mapa de screens!!! \n");
        stageFX.setTitle(screens.get(key).getTitleStage() + " #NOT LOADED...");
        stageFX.setWidth(400);
        stageFX.setScene(null);
        return stageFX;
    }
    
    public Stage buildScreenInNewStage(final String key, final Modality modality) {
        Stage st;
        st = new Stage();
        st.initModality(modality);
        boolean ok;
        ok = this.loadScreen(key);
        System.out.println("Go set New: [" + key + "] result " + ok);
        if (screens.get(key) != null && ok) {
            final DoubleProperty opacity = st.opacityProperty();
            stage.setScene(new Scene(screens.get(key).getLoadedScreen()));
            stage.setTitle(screens.get(key).getTitleStage());
            return stage;
        }
        System.err.println("screen n\u00e3o foi carregado / inexistente no mapa de screens!!! \n");
        stage.setTitle(screens.get(key).getTitleStage() + " #NOT LOADED...");
        stage.setWidth(400);
        stage.setScene(null);
        return stage;
    }
    
    /**
     *
     * @param key SceneClass
     * @return true if unloaded, false if not loaded.
     */
    public boolean unloadScreen(final String key) {
        if (this.screens.remove(key) == null) {
            System.out.println("Screen didn't exist");
            return false;
        }
        return true;
    }
    
    /**
     *
     * @return
     */
    public HashMap getMapScreens() {
        return this.screens;
    }   
}    

