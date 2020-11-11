import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SettingsScreen extends JPanel {

    SettingsSlider widthSlider;
    SettingsSlider heightSlider;
    SettingsSlider tileSizeSlider;
    SettingsRadioButtonGroup playerColorGroup;
    SettingsRadioButtonGroup tilesInARowGroup;
    JLabel warningLabel;
    JButton startGameButton;

    public SettingsScreen(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        widthSlider = AddSlider("Field width", 3, 25, Settings.widthInput, newValue -> {Settings.widthInput = newValue; Validate();});
        heightSlider = AddSlider("Field height", 3, 25, Settings.heightInput, newValue -> {Settings.heightInput = newValue; Validate();});
        tileSizeSlider = AddSlider("Tile Size(pixels)", 10,100, Settings.tileSize,newValue -> Settings.tileSize = newValue);
        playerColorGroup = AddRadioButtonGroup("Player Color", 0, newValue -> Settings.playerColor = newValue,"Black", "White");
        tilesInARowGroup = AddRadioButtonGroup("Tiles in a row to win", Settings.tilesInARowToWin-3, newValue -> { Settings.tilesInARowToWin = newValue+3; Validate(); }, "3", "4", "5");
        warningLabel = AddWarningLabel();
        startGameButton = AddButton("Start Game!", e -> FiveInARow.startPlaying() );

        setPreferredSize(new Dimension(getPreferredSize().width + 20, getPreferredSize().height + 20));
    }

    public SettingsSlider AddSlider(String name, int min, int max, int valueReference, ValueChangedListener listener) {
        SettingsSlider slider = new SettingsSlider(name, min, max, valueReference, listener);
        add(slider);
        return slider;
    }

    public SettingsRadioButtonGroup AddRadioButtonGroup(String name, int defaultPosition, ValueChangedListener listener, String... options){
        SettingsRadioButtonGroup group = new SettingsRadioButtonGroup(name, defaultPosition, listener, options);
        add(group);
        return group;
    }

    public JButton AddButton(String name, ActionListener listener){
        JPanel container = new JPanel();
        add(container);
        JButton button = new JButton(name);
        container.add(button);
        button.addActionListener(listener);
        return button;
    }

    public JLabel AddWarningLabel(){
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.LEFT));
        add(container);
        JLabel label = new JLabel(" ");
        label.setForeground(Color.red);
        container.add(label);
        return label;
    }

    public void Validate() {
        if (    ((Settings.heightInput<4||Settings.widthInput<4)&&Settings.tilesInARowToWin>=4)||
                ((Settings.heightInput<5||Settings.widthInput<5)&&Settings.tilesInARowToWin>=5)){
            warningLabel.setText("Increase playing field size or decrease tiles in a row to win");
            startGameButton.setEnabled(false);
        } else if(Settings.heightInput>=10&&Settings.widthInput>=10&&Settings.tilesInARowToWin<5) {
            warningLabel.setText("Decrease playing field size or increase tiles in a row to win");
            startGameButton.setEnabled(false);
        } else {
            warningLabel.setText(" ");
            startGameButton.setEnabled(true);
        }
    }
}
