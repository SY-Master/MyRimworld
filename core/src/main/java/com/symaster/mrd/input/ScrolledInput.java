package com.symaster.mrd.input;

import com.badlogic.gdx.InputAdapter;
import com.symaster.mrd.api.ScrolledInputEvent;

/**
 * @author yinmiao
 * @since 2024/12/27
 */
public class ScrolledInput extends InputAdapter {

    private ScrolledInputEvent scrolledInputEvent;

    public ScrolledInputEvent getScrolledInputEvent() {
        return scrolledInputEvent;
    }

    public void setScrolledInputEvent(ScrolledInputEvent scrolledInputEvent) {
        this.scrolledInputEvent = scrolledInputEvent;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amountX the horizontal scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @param amountY the vertical scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (scrolledInputEvent == null) {
            return false;
        }

        return scrolledInputEvent.scrolled(amountX, amountY);
    }


}
