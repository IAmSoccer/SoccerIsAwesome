package at.iamsoccer.soccerisawesome.woodcutter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.StonecuttingRecipe;

import static org.bukkit.Bukkit.getServer;

public class WoodCutter {
    private static final String[] WOOD_TYPES = {
            "OAK_LOG",
            "OAK_WOOD",
            "SPRUCE_LOG",
            "SPRUCE_WOOD",
            "BIRCH_LOG",
            "BIRCH_WOOD",
            "JUNGLE_LOG",
            "JUNGLE_WOOD",
            "ACACIA_LOG",
            "ACACIA_WOOD",
            "DARK_OAK_LOG",
            "DARK_OAK_WOOD",
            "MANGROVE_LOG",
            "MANGROVE_WOOD",
            "CRIMSON_STEM",
            "CRIMSON_HYPHAE",
            "WARPED_STEM",
            "WARPED_HYPHAE",
            "CHERRY_LOG",
            "CHERRY_WOOD"
    };
    public static boolean tryCreateStonecutterRecipes() {
        try {
            for (String material : WOOD_TYPES) {
                String namekey = "STRIP" + material;
                StonecuttingRecipe recipe = new StonecuttingRecipe(NamespacedKey.minecraft(namekey.toLowerCase()), new ItemStack(Material.valueOf("STRIPPED_" + material)), Material.valueOf(material));
                Bukkit.getServer().addRecipe(recipe);
            }
            return true;
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(String.valueOf(e));
            return false;
        }
    }
    public static boolean tryRemoveStonecutterRecipes() {

        try {
            for (String material : WOOD_TYPES) {
                String namekey = "STRIP" + material;
                Bukkit.getServer().removeRecipe(NamespacedKey.minecraft(namekey.toLowerCase()));
            }
            return true;
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(String.valueOf(e));
            return false;
        }
    }
}
