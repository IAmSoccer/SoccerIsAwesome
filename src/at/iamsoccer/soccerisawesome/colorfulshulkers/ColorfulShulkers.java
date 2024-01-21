package at.iamsoccer.soccerisawesome.colorfulshulkers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.Arrays;
import java.util.EnumSet;

import static org.bukkit.Bukkit.getServer;

public class ColorfulShulkers {
    private static final EnumSet<Material> DYE_MATERIALS = EnumSet.noneOf(Material.class);

    static {
        //Add all the dyes to DYE_MATERIALS
        Arrays.stream(Material.values()).filter(m -> m.name().endsWith("_DYE")).forEach(DYE_MATERIALS::add);
    }

    public static boolean tryCreateColorfulShulkerRecipes() {
        //Try to make the recipes!
        try {
            for (var dye : DYE_MATERIALS) {
                var colorName = dye.name().substring(0, dye.name().lastIndexOf('_'));
                String nameKey = colorName + "_shulker_box_craft";
                ShapelessRecipe someRecipe = new ShapelessRecipe(NamespacedKey.minecraft(nameKey.toLowerCase()), new ItemStack(Material.valueOf(colorName.toUpperCase() + "_SHULKER_BOX")));
                someRecipe.addIngredient(Material.SHULKER_SHELL);
                someRecipe.addIngredient(Material.SHULKER_SHELL);
                someRecipe.addIngredient(Material.CHEST);
                someRecipe.addIngredient(dye);
                Bukkit.getServer().addRecipe(someRecipe);
            }
            return true;
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(String.valueOf(e));
            return false;
        }
    }

    public static boolean tryRemoveColorfulShulkerRecipes() {
        //Try to remove the recipes... :(
        try {
            for (var dye : DYE_MATERIALS) {
                var colorName = dye.name().substring(0, dye.name().lastIndexOf('_'));
                String nameKey = colorName + "_shulker_box_craft";
                Bukkit.getServer().removeRecipe(NamespacedKey.minecraft(nameKey.toLowerCase()));
            }
            return true;
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(String.valueOf(e));
            return false;
        }
    }
}
