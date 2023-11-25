package settlementexpansion;

import necesse.engine.Settings;
import necesse.engine.registries.MobRegistry;
import necesse.entity.mobs.HumanTextureFull;
import necesse.gfx.gameSound.GameSound;
import necesse.gfx.ui.ButtonIcon;

public class ModResources {

    public static GameSound safe_open;
    public static GameSound safe_close;
    public static ButtonIcon inventory_locked;
    public static ButtonIcon inventory_unlocked;
    public static HumanTextureFull architect;

    public static void loadTextures() {
        inventory_locked = new ButtonIcon(Settings.UI, "inventory_locked");
        inventory_unlocked = new ButtonIcon(Settings.UI, "inventory_unlocked");
    }

    public static void loadMobTextures() {
        architect = MobRegistry.Textures.humanTextureFull("humans/architect");
    }

    public static void loadSounds() {
        safe_open = GameSound.fromFile("safe_open");
        safe_close = GameSound.fromFile("safe_close");
    }

}
