/*
 * @file ModConfig.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Main class for module settings. Handles reading and
 * saving the config file.
 */
package wile.engineersdecor.detail;

import net.minecraft.block.Block;
import wile.engineersdecor.ModEngineersDecor;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import wile.engineersdecor.blocks.*;

import javax.annotation.Nullable;

@Config(modid = ModEngineersDecor.MODID)
@Config.LangKey("engineersdecor.config.title")
public class ModConfig
{
  @Config.Comment({"Allows disabling specific features."})
  @Config.Name("Feature opt-outs")
  public static final SettingsOptouts optout = new SettingsOptouts();
  public static final class SettingsOptouts
  {
    @Config.Comment({"Disable clinker bricks and derived blocks."})
    @Config.Name("Without clinker bricks")
    @Config.RequiresMcRestart
    public boolean without_clinker_bricks = false;

    @Config.Comment({"Disable slag bricks and derived blocks."})
    @Config.Name("Without slag bricks")
    @Config.RequiresMcRestart
    public boolean without_slag_bricks = false;

    @Config.Comment({"Disable rebar concrete and derived blocks."})
    @Config.Name("Without rebar concrete")
    @Config.RequiresMcRestart
    public boolean without_rebar_concrete = false;

    @Config.Comment({"Disable all mod wall blocks."})
    @Config.Name("Without walls")
    @Config.RequiresMcRestart
    public boolean without_walls = false;

    @Config.Comment({"Disable all mod stairs blocks."})
    @Config.Name("Without stairs")
    @Config.RequiresMcRestart
    public boolean without_stairs = false;

    @Config.Comment({"Disable IE concrete wall."})
    @Config.Name("Without concrete wall")
    @Config.RequiresMcRestart
    public boolean without_ie_concrete_wall = false;

    @Config.Comment({"Disable panzer glass and derived blocks."})
    @Config.Name("Without panzer glass")
    @Config.RequiresMcRestart
    public boolean without_panzer_glass = false;

    @Config.Comment({"Disable treated wood crafting table."})
    @Config.Name("Without crafting table")
    @Config.RequiresMcRestart
    public boolean without_crafting_table = false;

    @Config.Comment({"Disable small lab furnace."})
    @Config.Name("Without lab furnace")
    @Config.RequiresMcRestart
    public boolean without_lab_furnace = false;

    @Config.Comment({"Disable treated wood table, stool, windowsill, pole, etc."})
    @Config.Name("Without tr. wood furniture")
    @Config.RequiresMcRestart
    public boolean without_treated_wood_furniture = false;

    @Config.Comment({"Disable treated wood window, etc."})
    @Config.Name("Without windows")
    @Config.RequiresMcRestart
    public boolean without_windows = false;

    @Config.Comment({"Disable light sources"})
    @Config.Name("Without lights")
    @Config.RequiresMcRestart
    public boolean without_light_sources = false;

    @Config.Comment({"Disable ladders"})
    @Config.Name("Without ladders")
    @Config.RequiresMcRestart
    public boolean without_ladders = false;
  }

  @Config.Comment({
    "Settings for beta testing and trouble shooting. Some of the settings " +
    "may be moved to other categories after testing."
  })
  @Config.Name("Miscellaneous")
  public static final SettingsZTesting zmisc = new SettingsZTesting();
  public static final class SettingsZTesting
  {
    @Config.Comment({ "Enables experimental features. Use at own risk." })
    @Config.Name("With experimental")
    @Config.RequiresMcRestart
    public boolean with_experimental = false;

    @Config.Comment({ "Disable all internal recipes, allowing to use alternative pack recipes." })
    @Config.Name("Without recipes")
    @Config.RequiresMcRestart
    public boolean without_recipes = false;

    @Config.Comment({"Disable registration of opt'ed out blocks. That is normally not a good idea. Your choice."})
    @Config.Name("Without opt-out registration")
    @Config.RequiresMcRestart
    public boolean without_optout_registration = false;
  }

  @Config.Comment({"Tweaks and block behaviour adaptions."})
  @Config.Name("Tweaks")
  public static final SettingsTweaks tweaks = new SettingsTweaks();
  public static final class SettingsTweaks
  {
    @Config.Comment({
      "Smelts ores to nuggets that are normally smelted to ingots, " +
      "if detectable in the Forge ore dict. Prefers IE recipe results."
    })
    @Config.Name("Furnace: Nugget smelting")
    public boolean furnace_smelts_nuggets = false;
  }

  @SuppressWarnings("unused")
  @Mod.EventBusSubscriber(modid=ModEngineersDecor.MODID)
  private static final class EventHandler
  {
    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
      if(!event.getModID().equals(ModEngineersDecor.MODID)) return;
      ConfigManager.sync(ModEngineersDecor.MODID, Config.Type.INSTANCE);
      apply();
    }
  }

  @SuppressWarnings("unused")
  public static final void onPostInit(FMLPostInitializationEvent event)
  { apply(); }

  public static final boolean isWithoutOptOutRegistration()
  { return (zmisc!=null) && (zmisc.without_optout_registration); }

  public static final boolean isWithoutRecipes()
  { return (zmisc==null) || (zmisc.without_recipes); }

  public static final boolean isOptedOut(final @Nullable Block block)
  {
    if((block == null) || (optout==null)) return true;
    final String rn = block.getRegistryName().getPath();
    if(optout.without_clinker_bricks && rn.startsWith("clinker_brick_")) return true;
    if(optout.without_slag_bricks && rn.startsWith("slag_brick_")) return true;
    if(optout.without_rebar_concrete && rn.startsWith("rebar_concrete")) return true;
    if(optout.without_ie_concrete_wall && rn.startsWith("concrete_wall")) return true;
    if(optout.without_panzer_glass && rn.startsWith("panzerglass_")) return true;
    if(optout.without_crafting_table && (block instanceof BlockDecorCraftingTable)) return true;
    if(optout.without_lab_furnace && (block instanceof BlockDecorFurnace)) return true;
    if(optout.without_windows && rn.endsWith("_window")) return true;
    if(optout.without_light_sources && rn.endsWith("_light")) return true;
    if(optout.without_ladders && (block instanceof BlockDecorLadder)) return true;
    if(optout.without_walls && rn.endsWith("_wall")) return true;
    if(optout.without_stairs && rn.endsWith("_stairs")) return true;
    if(optout.without_treated_wood_furniture) {
      if(block instanceof BlockDecorChair) return true;
      if(rn.equals("treated_wood_pole")) return true;
      if(rn.equals("treated_wood_table")) return true;
      if(rn.equals("treated_wood_stool")) return true;
      if(rn.equals("treated_wood_windowsill")) return true;
      if(rn.equals("treated_wood_window")) return true;
    }
    return false;
  }

  public static final void apply()
  {
    ModRecipes.furnaceRecipeOverrideReset();
    if(tweaks.furnace_smelts_nuggets) ModRecipes.furnaceRecipeOverrideSmeltsOresToNuggets();
  }

}
