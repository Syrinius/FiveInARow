import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SettingsRadioButtonGroup extends JPanel {

    List<JRadioButton> buttons;
    ValueChangedListener listener;

    public SettingsRadioButtonGroup(String name, int defaultPosition, ValueChangedListener listener, String... options){
        this.listener = listener;
        JLabel label = new JLabel(name);
        add(label);
        ButtonGroup group = new ButtonGroup();
        buttons = new ArrayList<>();
        for(String option : options) {
            JRadioButton temp = new JRadioButton(option);
            buttons.add(temp);
            add(temp);
            group.add(temp);
            temp.addActionListener(e -> listener.valueChanged(buttons.indexOf((JRadioButton)e.getSource())));
        }
        buttons.get(defaultPosition).setSelected(true);
    }
}
