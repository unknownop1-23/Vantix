package com.vtx.vantix.repo;

public class VNTXRepo {

    public static final String KEY_UPDATE = "ASMVersion";
    public static final String KEY_PLAYERSIZES = "playersizes";
    public static final String KEY_ENCHANTS = "enchants";
    public static final String KEY_TIMERS = "timers";
    public static final String KEY_TAGS = "tags";
    public static final String KEY_REPO = "repo";
    public static final String KEY_OTHER = "other";

    // --- Added for NEF Ports ---
    public static final String KEY_ACCESSORIES = "accessories";
    public static final String KEY_PETS = "pets";
    public static final String KEY_FAIRYSOULS = "fairysouls";

    private static final String BASE = "https://raw.githubusercontent.com/aetheria-org/Aetheria-REPO/main/";
    private static final String NEF_BASE = "https://raw.githubusercontent.com/davidbelesp/NotEnoughFakepixel-REPO/main/data/";

    private VNTXRepo() {
    }

    public static void init() {
        // Original Vantix Endpoints
        RepoHandler.register(KEY_UPDATE, BASE + "data/ASMVersion.json");
        RepoHandler.register(KEY_PLAYERSIZES, BASE + "data/playersizes.json");
        RepoHandler.register(KEY_ENCHANTS, BASE + "data/enchants.json");
        RepoHandler.register(KEY_TIMERS, BASE + "data/timers.json");
        RepoHandler.register(KEY_TAGS, BASE + "data/tags.json");
        RepoHandler.register(KEY_REPO, BASE + "data/repo.json");
        RepoHandler.register(KEY_OTHER, BASE + "data/other.json");

        // NEF Endpoints
        RepoHandler.register(KEY_ACCESSORIES, NEF_BASE + "accessories.json");
        RepoHandler.register(KEY_PETS, NEF_BASE + "pets.json");
        RepoHandler.register(KEY_FAIRYSOULS, NEF_BASE + "fairysouls.json");

        RepoHandler.warmupAll();
    }
}