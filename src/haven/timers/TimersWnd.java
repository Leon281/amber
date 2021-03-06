package haven.timers;


import haven.*;
import haven.Button;
import haven.Window;

import java.awt.*;
import java.util.List;

public class TimersWnd extends Window {
    public final GameUI gui;
    public static final int WIDTH = 460;
    public final Scrollport port;
    private final static int MAX_ITEMS = 13;

    public TimersWnd(final GameUI gui) {
        super(Coord.z, "Timers");
        this.gui = gui;

        Button btna = new Button(50, "Add") {
            public void click() {
                parent.parent.add(new TimerEditWnd("Create New Timer", gui), new Coord(gui.sz.x / 2 - 200, gui.sz.y / 2 - 200));
            }
        };
        add(btna, new Coord(20, 10));

        CheckBox chkalarm = new CheckBox("Sound Alarm") {
            {
                a = Config.timersalarm;
            }

            public void set(boolean val) {
                Utils.setprefb("timersalarm", val);
                Config.timersalarm = val;
                a = val;
            }
        };
        add(chkalarm, new Coord(350, 15));

        List<TimerWdg> timers = Glob.timersThread.getall();
        if (timers.size() == 0)
            Glob.timersThread.load();
        timers = Glob.timersThread.getall();

        int portHeight = timers.size() > MAX_ITEMS ? TimerWdg.height * MAX_ITEMS : timers.size() * TimerWdg.height;
        port = new Scrollport(new Coord(WIDTH - 20 - 15, portHeight), TimerWdg.height) {
            @Override
            public void draw(GOut g) {
                g.chcolor(0, 0, 0, 128);
                g.frect(Coord.z, sz);
                g.chcolor();
                super.draw(g);
            }
        };
        add(port, new Coord(20, 50));

        for (int i = 0; i < timers.size(); i++)
            port.cont.add(timers.get(i), new Coord(0, i * TimerWdg.height));

        resize();
    }

    public void resize() {
        List<TimerWdg> timers = Glob.timersThread.getall();
        int portHeight = timers.size() > MAX_ITEMS ? TimerWdg.height * MAX_ITEMS : timers.size() * TimerWdg.height;
        port.resize(port.sz.x, portHeight);
        port.cont.update();
        port.bar.resize(portHeight);
        port.bar.changed();
        super.resize(WIDTH, portHeight + 60);
    }

    @Override
    public void wdgmsg(Widget sender, String msg, Object... args) {
        if (sender == cbtn) {
            hide();
        } else {
            super.wdgmsg(sender, msg, args);
        }
    }

    @Override
    public boolean type(char key, java.awt.event.KeyEvent ev) {
        if (key == 27) {
            hide();
            return true;
        }
        return super.type(key, ev);
    }
}
