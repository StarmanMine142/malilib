package fi.dy.masa.malilib.gui.wrappers;

import org.lwjgl.input.Keyboard;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.interfaces.ITextFieldListener;

public class TextFieldWrapper<T extends GuiTextFieldGeneric>
{
    private final T textField;
    private final ITextFieldListener<T> listener;
    
    public TextFieldWrapper(T textField, ITextFieldListener<T> listener)
    {
        this.textField = textField;
        this.listener = listener;
    }

    public T getTextField()
    {
        return this.textField;
    }

    public ITextFieldListener<T> getListener()
    {
        return this.listener;
    }

    public boolean isFocused()
    {
        return this.textField.isFocused();
    }

    public void setFocused(boolean isFocused)
    {
        this.textField.setFocused(isFocused);
    }

    public void onGuiClosed()
    {
        if (this.listener != null)
        {
            this.listener.onGuiClosed(this.textField);
        }
    }

    public void draw()
    {
        this.textField.drawTextBox();
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (this.textField.mouseClicked(mouseX, mouseY, mouseButton))
        {
            return true;
        }

        return false;
    }

    public boolean keyTyped(char typedChar, int keyCode)
    {
        String textPre = this.textField.getText();

        if (this.textField.isFocused() && this.textField.textboxKeyTyped(typedChar, keyCode))
        {
            if (this.listener != null &&
                (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_TAB ||
                 this.textField.getText().equals(textPre) == false))
            {
                this.listener.onTextChange(typedChar, keyCode, this.textField);
            }

            return true;
        }

        return false;
    }
}
