package org.redis.cmd;

import java.util.List;

public class CmdLine {
    private List<byte[]> commands;

    public CmdLine(List<byte[]> commands) {
        this.commands = commands;
    }

    public List<byte[]> getCommands() {
        return commands;
    }
}
