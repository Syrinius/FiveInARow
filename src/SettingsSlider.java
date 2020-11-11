import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class SettingsSlider extends JPanel {

    JLabel nameLabel;
    JSlider slider;
    JLabel valueLabel;
    ValueChangedListener listener;

    public SettingsSlider(String name, int min, int max, int valueReference, ValueChangedListener listener){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        this.listener = listener;
        nameLabel = new JLabel(name);
        add(nameLabel);
        slider = new JSlider(min,max,valueReference);
        add(slider);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.addChangeListener(e -> {
            valueLabel.setText(slider.getValue()+"");
            listener.valueChanged(slider.getValue());
        });
        valueLabel = new JLabel(valueReference+"");
        add(valueLabel);
    }
}
