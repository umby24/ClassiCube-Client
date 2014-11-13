package com.mojang.minecraft;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.net.MalformedURLException;
import java.net.URL;

import com.mojang.util.LogUtil;

public class MinecraftApplet extends Applet {

    private Canvas canvas;
    private Minecraft minecraft;

    private Thread thread = null;

    @Override
    public void destroy() {
        stopGameThread();
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL("http://minecraft.net:80/");
        } catch (MalformedURLException ex) {
            LogUtil.logError("Error getting applet code base", ex);
        }
        return null;
    }

    @Override
    public URL getDocumentBase() {
        try {
            return new URL("http://minecraft.net:80/play.jsp");
        } catch (MalformedURLException ex) {
            LogUtil.logError("Error getting applet document base", ex);
        }

        return null;
    }

    @Override
    public void init() {
        Container topParent = null;
        Container parent = this;
        // The natural thing would be to call getParent() until it returns
        // null, but then you would be looping for a long time, since
        // PluginEmbeddedFrame's getParent() returns itself.
        for (int k = 0; k < 10; k++) {
            topParent = parent;
            parent = parent.getParent();
            if (parent == null) {
                break;
            }
        }

        // If topParent isn't a KeyEventDispatcher then we must be in some
        // Plugin version that doesn't need the workaround.
        try {
            KeyEventDispatcher ked = (KeyEventDispatcher) topParent;
            KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            // You have to remove it twice, otherwise the problem isn't fixed
            kfm.removeKeyEventDispatcher(ked);
            kfm.removeKeyEventDispatcher(ked);
        } catch (ClassCastException e) {
        }

        canvas = new MinecraftCanvas(this);

        boolean fullscreen = false;

        if (getParameter("fullscreen") != null) {
            fullscreen = getParameter("fullscreen").equalsIgnoreCase("true");
        }

        minecraft = new Minecraft(canvas, this, getWidth(), getHeight(), fullscreen, true);

        minecraft.host = getDocumentBase().getHost();

        if (getDocumentBase().getPort() > 0) {
            minecraft.host = minecraft.host + ":" + getDocumentBase().getPort();
        }

        if (getParameter("username") != null && getParameter("sessionid") != null) {
            minecraft.session = new SessionData(getParameter("username"), getParameter("sessionid"));

            if (getParameter("mppass") != null) {
                minecraft.session.mppass = getParameter("mppass");
            }

            // TODO: Not tested.
            minecraft.session.haspaid = getParameter("haspaid").equalsIgnoreCase("true");
        }

        if (getParameter("loadmap_user") != null && getParameter("loadmap_id") != null) {
            minecraft.levelName = getParameter("loadmap_user");
            minecraft.levelId = Integer.parseInt(getParameter("loadmap_id"));
        } else if (getParameter("server") != null && getParameter("port") != null) {
            String server = getParameter("server");
            int port = Integer.parseInt(getParameter("port"));

            minecraft.server = server;
            minecraft.port = port;
        }

        minecraft.isLevelLoaded = true;

        setLayout(new BorderLayout());

        add(canvas, "Center");

        canvas.setFocusable(true);

        validate();
    }

    @Override
    public void start() {
        minecraft.isWaiting = false;
    }

    public void startGameThread() {
        if (thread == null) {
            thread = new Thread(minecraft, "GameLoop-Applet");

            thread.start();
        }
    }

    @Override
    public void stop() {
        minecraft.isWaiting = true;
    }

    public void stopGameThread() {
        if (thread != null) {
            minecraft.isRunning = false;

            try {
                thread.join(1000L);
            } catch (InterruptedException e) {
                minecraft.shutdown();
            }

            thread = null;
        }
    }
}
