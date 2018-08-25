package main;

import base.masterService.nodes.Address;
import base.utils.Message;
import base.utils.MetricsService;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import org.apache.logging.log4j.Logger;
import utils.MessageSystem.NodeMessageReceiver;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.masterService.MRegister;
import utils.ResourceSystem.ResourceFactory;
import utils.ResourceSystem.Resources.configs.ServerConfig;
import utils.logger.LoggerImpl;
import utils.logger.UncaughtExceptionLog4j2Handler;
import utils.tickSleeper.TickSleeper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class MetricServiceImpl implements MetricsService, Runnable {
    static MetricsService metricsService = null;
    private static Logger log;
    private final ResourceFactory resourceFactory;
    private final Address address = new Address();
    private final NodeMessageReceiver messageReceiver;
    private final Queue<Message> unhandledMessages = new LinkedBlockingQueue<>();
    private final ServerConfig serverConfig;
    private Map<String, Counter> counterMap = new HashMap<>();
    private Map<String, Gauge> gaugeMap = new HashMap<>();
    private Socket masterService;
    private boolean masterIsReady;
    private HTTPServer server;

    public MetricServiceImpl(String configPath) {

        //TODO Вынести в Utils
        this.resourceFactory = ResourceFactory.instance();

        this.serverConfig = (ServerConfig) resourceFactory.getResource(configPath);
        InetAddress address;
        try {
            address = InetAddress.getByName(serverConfig.getLobby().getIp());
            masterService = new Socket(serverConfig.getMaster().getIp(),
                    Integer.parseInt(serverConfig.getMaster().getMasterPort()), address,
                    Integer.parseInt(serverConfig.getMetrics().getMasterPort()));
        } catch (IOException e) {
            log.error(e);
        }
        int port = 0;
        try {
            port = Integer.parseInt(serverConfig.getMetrics().getSecondPort());
            if (port == 0) throw new UnknownHostException();
        } catch (UnknownHostException e) {
            log.fatal(String.format("Unable to start metric service, %s is NAN", serverConfig.getMetrics().getSecondPort()));
        }
        createPrometheusService(port);
        this.messageReceiver = new NodeMessageReceiver(unhandledMessages, masterService);
        NodeMessageSender.sendMessage(masterService, new MRegister(this.address, MetricsService.class, serverConfig.getMetrics().getIp(), serverConfig.getMetrics().getMasterPort()));
    }

    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "config.xml" : arg;
        log = LoggerImpl.getLogger("Metrics");
        MetricsService metricsService = new MetricServiceImpl(configPath);
        Thread metricsThread = new Thread(metricsService);
        metricsThread.setName("METRICS");
        metricsThread.setUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
        metricsThread.start();
    }

    private void createPrometheusService(int port) {
        try {
            server = new HTTPServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        TickSleeper tickSleeper = new TickSleeper();
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            execNodeMessages();
            tickSleeper.tickEnd();
        }
    }

    private void execNodeMessages() {
        while (!unhandledMessages.isEmpty()) {
            unhandledMessages.poll().exec(this);
        }
    }

    @Override
    public void incrementCounter(String name, String help) {
        Counter counter = getCounter(name, help);
        counter.inc();
        log.debug("Incremented " + name);
    }

    @Override
    public void incrementCounter(String name, String Help, double amt) {
        Counter counter = getCounter(name, Help);
        counter.inc(amt);
        log.debug(String.format("Incremented %s for amount %s", name, amt));
    }

    @Override
    public void incrementGauge(String name, String help) {
        Gauge gauge = getGauge(name, help);
        gauge.inc();
        log.debug("Gauge " + name);
    }

    @Override
    public void incrementGauge(String name, String Help, double amt) {
        Gauge gauge = getGauge(name, Help);
        gauge.inc(amt);
        log.debug(String.format("Gauge %s for amount %s", name, amt));
    }

    @Override
    public void decrementGauge(String name, String help) {
        Gauge gauge = getGauge(name, help);
        gauge.dec();
        log.debug(String.format("Decrement Gauge %s", name));
    }

    @Override
    public void decrementGauge(String name, String help, double amt) {
        Gauge gauge = getGauge(name, help);
        gauge.dec(amt);
        log.debug(String.format("Decrement Gauge %s for amount %s", name, amt));
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    private Counter getCounter(String name, String help) {
        Counter counter = counterMap.get(name);
        if (Objects.equals(counter, null)) {
            counter = Counter.build().name(name).help(help).register();
            counterMap.put(name, counter);
        }
        return counter;
    }

    private Gauge getGauge(String name, String help) {
        log.fatal(String.format("Name: %s, help: %s", name, help));
        Gauge gauge = gaugeMap.get(name);
        if (Objects.equals(gauge, null)) {
            gauge = Gauge.build().name(name).help(help).register();
            gaugeMap.put(name, gauge);
        }
        return gauge;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setMasterIsReady(boolean masterIsReady) {
        this.masterIsReady = masterIsReady;
    }

    @Override
    public Socket getMasterService() {
        return masterService;
    }
}
