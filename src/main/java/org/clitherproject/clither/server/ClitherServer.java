package org.clitherproject.clither.server;

import org.clitherproject.clither.api.Clither;
import org.clitherproject.clither.api.Server;
import org.clitherproject.clither.api.plugin.Messenger;
import org.clitherproject.clither.api.plugin.PluginManager;
import org.clitherproject.clither.api.plugin.Scheduler;
import org.clitherproject.clither.server.command.Commands;
import org.clitherproject.clither.server.config.ClitherConfig;
import org.clitherproject.clither.server.config.Configuration;
import org.clitherproject.clither.server.gui.ServerCLI;
import org.clitherproject.clither.server.gui.ServerGUI;
import org.clitherproject.clither.server.net.NetworkManager;
import org.clitherproject.clither.server.tick.TickController;
import org.clitherproject.clither.server.tick.TickWorker;
import org.clitherproject.clither.server.tick.Tickable;
import org.clitherproject.clither.server.world.PlayerImpl;
import org.clitherproject.clither.server.world.WorldImpl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;
import java.util.logging.*;

public class ClitherServer implements Server {

    public static final Logger log = Logger.getGlobal();
    private static ClitherServer instance;
    private final PlayerList playerList = new PlayerList(this);
    private final String configurationFile = "server.properties";
    private final boolean debugMode = Boolean.getBoolean("debug");
    private final TickController tickController = new TickController(4);
    private final Messenger messenger = new Messenger();
    private Scheduler scheduler;
    private int tickThreads = Integer.getInteger("tickThreads", 1);
    private NetworkManager networkManager;
    private PluginManager pluginManager;
    private WorldImpl world;
    private ClitherConfig configuration;
    private long tick = 0L;
    private boolean running;

    public static void main(String[] args) throws Throwable {
        ClitherServer.instance = new ClitherServer();
        ClitherServer.instance.run();
    }

    public static ClitherServer getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return log;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public PlayerList getPlayerList() {
        return playerList;
    }

    public WorldImpl getWorld() {
        return world;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public boolean isDebugging() {
        return debugMode;
    }

    private void setupLogging() {
        log.setUseParentHandlers(false);

        LogFormatter formatter = new LogFormatter();

        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(formatter);
        if (isDebugging()) {
            log.setLevel(Level.FINEST);
            ch.setLevel(Level.FINEST);
        } else {
            log.setLevel(Level.INFO);
            ch.setLevel(Level.INFO);
        }
        log.addHandler(ch);

        try {
            FileHandler fh = new FileHandler("server.log");
            fh.setFormatter(formatter);
            if (isDebugging()) {
                fh.setLevel(Level.FINEST);
            } else {
                ch.setLevel(Level.INFO);
            }
            log.addHandler(fh);
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error while adding FileHandler to logger. Logs will not be output to a file.", ex);
        }

    }

    public void loadConfig() {
        this.configuration = Configuration.load(configurationFile);
    }

    public void saveConfig() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("server.properties", "UTF-8");
        } catch (Exception ex) {
            log.severe("An internal error has occured whilist generating server.properties.");
            shutdown();
        }
        writer.println("name=Unknown Server");
        writer.println("ip=localhost");
        writer.println("port=443");
        writer.println("maxPlayers=20");
        writer.println("maxMass=85500");
        writer.println("foodSize=1");
        writer.println("foodStartAmount=100");
        writer.println("startMass=35");
        writer.println("borderSize=6000");
        writer.close();
        loadConfig();
    }

    public ClitherConfig getConfig() {
        return configuration;
    }

    public long getTick() {
        return tick;
    }

    private void run() {
        if (ServerGUI.isHeadless()) {
            Thread thread = new Thread(new ServerCLI(this), "Console Command Handler");
            thread.setDaemon(true);
            thread.start();
        } else {
            ServerGUI.spawn(this);
        }
        Clither.setServer(this);
        pluginManager = new PluginManager(this);
        setupLogging();
        log.info("Clither is now starting.");
        if (debugMode) {
            log.info("Debug mode is enabled; additional information will be logged.");
        }
        // Create the tick workers
        if (tickThreads < 1) {
            tickThreads = 1;
        }
        if (tickThreads > 1) {
            log.warning("Use of multiple tick threads is experimental and may be unstable!");
        }
        for (int i = 0; i < tickThreads; i++) {
            //tickWorkers.add(new TickWorker());
        }
        if (!new File(configurationFile).isFile()) {
            saveConfig();
        } else {
            loadConfig();
        }
        world = new WorldImpl(this);
        log.info("Loading plugins.");
        try {
            File pluginDirectory = new File("plugins");
            if (!pluginDirectory.exists()) {
                pluginDirectory.mkdirs();
            }
            pluginManager.loadPlugins(pluginDirectory);
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Failed to load plugins", t);
        }
        log.info("Enabling plugins.");
        pluginManager.enablePlugins();
        log.info("Start listening on port " + getConfig().server.port);
        networkManager = new NetworkManager(this);
        try {
            networkManager.start();
        } catch (IOException | InterruptedException ex) {
            log.info("Failed to start server! " + ex.getMessage());
            ex.printStackTrace();
            if (ServerGUI.isSpawned()) {
                System.exit(1);
            } else {
                System.exit(1);
            }
        }
        //tickWorkers.forEach(TickWorker::start);
        running = true;
        while (running) {
            try {
                long startTime = System.currentTimeMillis();
                tick++;
                //world.tick(this::tick);
                for (PlayerImpl player : playerList.getAllPlayers()) {
                    //tick(player.getTracker()::updateNodes);
                }
                //tickWorkers.forEach(TickWorker::waitForCompletion);
                long tickDuration = System.currentTimeMillis() - startTime;
                if (tickDuration < 50) {
                    log.finer("Tick took " + tickDuration + "ms, sleeping for a bit");
                    Thread.sleep(50 - tickDuration);
                } else {
                    log.finer("Tick took " + tickDuration + "ms (which is >=50ms), no time for sleep");
                }
            } catch (InterruptedException ex) {
                break;
            }
        }
        networkManager.shutdown();
        log.info("Disabling plugins...");
        pluginManager.disablePlugins();
        log.info("Successfully stopped server!");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(-1);
    }

    public void handleCommand(String s) {
        s = s.trim();
        if (s.isEmpty()) {
            return;
        }
        Commands.onCommand(s);
    }

    public void shutdown() {
        running = false;
    }


    private static class LogFormatter extends Formatter {

        private static final DateFormat df = new SimpleDateFormat("HH:mm:ss");

        @Override
        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder(df.format(new Date(record.getMillis())));
            sb.append(" [");
            sb.append(record.getLevel());
            sb.append("] ");
            sb.append(formatMessage(record));
            sb.append('\n');
            return sb.toString();
        }
    }
}
    