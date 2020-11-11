import java.util.EventListener;

public interface ValueChangedListener extends EventListener {

    void valueChanged(int newValue);
}
