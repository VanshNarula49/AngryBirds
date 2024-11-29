package com.myPackage;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class Structure3 {
    private List<WoodBlock> blocks;

    public Structure3(World world, String texturePath) {
        blocks = new ArrayList<>();

        // Base Structure: Reuse Level 2 design
        blocks.add(new WoodBlock(world, 7.6f, 1f, 2f, 0.5f, 93f, texturePath)); // Left pillar
        blocks.add(new WoodBlock(world, 11.2f, 1f, 2f, 0.5f, 93f, texturePath)); // Middle pillar
        blocks.add(new WoodBlock(world, 14.8f, 1f, 2f, 0.5f, 93f, texturePath)); // Right pillar
        blocks.add(new WoodBlock(world, 13.3f, 3f, 4f, 0.2f, 3f, texturePath)); // Top horizontal crossbar
        blocks.add(new WoodBlock(world, 9.3f, 3f, 4f, 0.2f, 3f, texturePath));  // Lower horizontal crossbar

        // Additional Middle-Level Blocks
        blocks.add(new WoodBlock(world, 9.3f, 4f, 1.5f, 0.2f, 0f, texturePath)); // Small block left
        blocks.add(new WoodBlock(world, 13.3f, 4f, 1.5f, 0.2f, 0f, texturePath)); // Small block right

        // Upper Structure: Lighter blocks for stability
        blocks.add(new WoodBlock(world, 10f, 5f, 4f, 0.2f, 0f, texturePath));  // Horizontal crossbar above pillars
        blocks.add(new WoodBlock(world, 8.5f, 6f, 1f, 0.2f, 45f, texturePath)); // Slanted block left
        blocks.add(new WoodBlock(world, 11.5f, 6f, 1f, 0.2f, -45f, texturePath)); // Slanted block right

        // Add a small vertical block on top
        blocks.add(new WoodBlock(world, 10f, 6.5f, 0.5f, 1.5f, 0f, texturePath)); // Vertical block at the top
    }

    public void render(SpriteBatch batch) {
        for (WoodBlock block : blocks) {
            block.render(batch);
        }
    }

    public void dispose() {
        for (WoodBlock block : blocks) {
            block.dispose();
        }
    }
}
