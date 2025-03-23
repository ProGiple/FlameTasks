package org.satellite.dev.progiple.flametasks.menus.buttons;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public abstract class Button extends org.novasparkle.lunaspring.Menus.Items.buttons.Button {
    private List<String> defaultLore;
    public Button(Material material, String displayName, List<String> lore, int slot) {
        super(material, displayName, lore, 1, (byte) slot);
        this.defaultLore = new ArrayList<>(this.getLore());
    }

    public Button(ConfigurationSection section, int slot) {
        super(section, slot);
        this.defaultLore = new ArrayList<>(this.getLore());
    }
}
