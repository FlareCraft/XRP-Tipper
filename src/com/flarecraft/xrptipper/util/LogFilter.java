package com.flarecraft.xrptipper.util;

import com.flarecraft.xrptipper.XRPTipper;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class LogFilter implements Filter {

    private final boolean debug;

    public LogFilter(XRPTipper plugin) {

        debug = plugin.getConfig().getBoolean("General.Verbose_Logging");
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        return !(record.getMessage().contains("[Debug]") && ! debug);
    }
}
