package com.symaster.mrd.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class SolidColorDrawable extends BaseDrawable {

    private final Color color;
    private final TextureRegion textureRegion;

    public SolidColorDrawable(Color color) {
        this.color = new Color(color);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        textureRegion = new TextureRegion(new Texture(pixmap));
        pixmap.dispose(); // 立即释放Pixmap资源
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        Color originalColor = batch.getColor();
        batch.setColor(color);
        batch.draw(textureRegion, x, y, width, height);
        batch.setColor(originalColor); // 恢复原始颜色
    }
}
