package de.damcraft.serverseeker;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import de.damcraft.serverseeker.commands.ServerInfoCommand;
import de.damcraft.serverseeker.country.Countries;
import de.damcraft.serverseeker.country.Country;
import de.damcraft.serverseeker.country.CountrySetting;
import de.damcraft.serverseeker.hud.HistoricPlayersHud;
import de.damcraft.serverseeker.modules.BungeeSpoofModule;
import de.damcraft.serverseeker.utils.HistoricPlayersUpdater;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.gui.utils.SettingsWidgetFactory;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.minecraft.item.Items;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;

public class ServerSeeker extends MeteorAddon {
    /*
    Feature list for anticope.pages.dev:
    (creates features matching the RegEx '(?:add\(new )([^(]+)(?:\([^)]*)\)\)', as anticope checks for that.
    add(new Find servers with many parameters, for example: Cracked, MOTD, Loader, Player Count and MANY MORE!!!())
    add(new Server database with over 3,000,000 servers!())
    add(new Over 100 MILLION players tracked!())
    add(new Search for ANY server you want!())
     */
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("ServerSeeker", Items.SPYGLASS.getDefaultStack());
    public static final Map<String, Country> COUNTRY_MAP = new Object2ReferenceOpenHashMap<>();

    /*
    Hardcoded ServerSeeker API key
    Since the ServerSeeker authentication flow is currently unusable, hard coding a generously-donated user API key is
    the simplest option for all parties involved
     */
    public static final String API_KEY = "ZzOluD4Uj0TPrRPZuE94UtBuIVjYxNMt";

    public static final Gson gson = new Gson();

    @Override
    public void onInitialize() {
        LOG.info("Loaded the ServerSeeker addon!");

        // Load countries
        Countries.init();

        Modules.get().add( new BungeeSpoofModule() );
        Hud.get().register(HistoricPlayersHud.INFO);
        Commands.add( new ServerInfoCommand() );

        SettingsWidgetFactory.registerCustomFactory(CountrySetting.class, (theme) -> (table, setting) -> {
            CountrySetting.countrySettingW(table, (CountrySetting) setting, theme);
        });

        MeteorClient.EVENT_BUS.subscribe(HistoricPlayersUpdater.class);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "de.damcraft.serverseeker";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("Ogmur", "ServerSeeker");
    }

    @Override
    public String getWebsite() {
        return "https://serverseeker.net/";
    }

    @Override
    public String getCommit() {
        return Optional.ofNullable(FabricLoader
            .getInstance()
            .getModContainer("serverseeker")
            .orElseThrow().getMetadata()
            .getCustomValue("github:sha"))
            .map(CustomValue::getAsString)
            .map(String::trim)
            .orElse(null);
    }
}
