package com.myPackage;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class Structure {
    private List<WoodBlock> blocks;

    public Structure(World world, String texturePath) {
        blocks = new ArrayList<>();

        // Example Structure:
        // Horizontal base
//        blocks.add(new WoodBlock(world, 6f, 1f, 2f, 0.2f, 0f, texturePath)); // Bottom base
//        blocks.add(new WoodBlock(world, 10f, 1f, 2f, 0.2f, 0f, texturePath)); // Bottom base

        // Vertical blocks on top
        blocks.add(new WoodBlock(world, 11.2f, 1f, 2f, 0.5f, 93f, texturePath)); // Left pillar
        blocks.add(new WoodBlock(world, 14.8f, 1f, 2f, 0.5f, 93f, texturePath)); // Right pillar

        // Horizontal top
//        blocks.add(new WoodBlock(world, 6f, 4f, 2f, 0.2f, 0f, texturePath)); // Top crossbar
        blocks.add(new WoodBlock(world, 13f, 3f, 4f, 0.2f, 3f, texturePath)); // Top crossbar
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
