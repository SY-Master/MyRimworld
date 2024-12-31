package com.symaster.mrd.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.utils.Disposable;

public class SolidColorDrawable extends BaseDrawable implements Disposable {

    private final Color color;
    private final TextureRegion textureRegion;
    private Texture texture;

    public SolidColorDrawable(Color color) {
        this.color = color;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        texture = new Texture(pixmap);
        textureRegion = new TextureRegion(texture);
        pixmap.dispose(); // 立即释放Pixmap资源
    }

    public SolidColorDrawable(TextureRegion textureRegion, Color color) {
        this.textureRegion = textureRegion;
        this.color = color;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        Color originalColor = batch.getColor();
        batch.setColor(color);
        batch.draw(textureRegion, x, y, width, height);
        batch.setColor(originalColor); // 恢复原始颜色
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
