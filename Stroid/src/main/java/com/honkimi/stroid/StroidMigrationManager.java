package com.honkimi.stroid;

import java.util.ArrayList;
import java.util.List;

/**
 * Invoke StoroidMigration SQL class
 * @author kiminari.homma
 */
public final class StroidMigrationManager {
    /**
     * StroidConf. Implement this!
     */
    public interface StroidConf {
        /** get DB name */
        String getDBName();
        /** get DB version */
        int getDBVersion();
    }

    /** Singleton */
    private static StroidMigrationManager invoker;

    /** Migration implemented classes */
    private ArrayList<StroidMigration> commands = new ArrayList<StroidMigration>();

    /** Checklist not to multiple table names */
    private List<String> tableList = new ArrayList<String>();

    /** stroidConf */
    private StroidConf stroidConf;

    /**
     * Constructor
     */
    private StroidMigrationManager() {
        // nothing
    }

    /**
     * Singleton getInstance
     */
    public static synchronized StroidMigrationManager getInstance() {
        if (invoker == null) {
            invoker = new StroidMigrationManager();
        }
        return invoker;
    }

    /**
     * Add Migrations
     * @param command Migration implemented object
     */
    public void add(StroidMigration command) {
        if (!isContainedTableName(command.getTableName())) {
            commands.add(command);
            tableList.add(command.getTableName());
        }
    }

    /**
     * return command list from migration
     * @return commands List
     */
    public List<StroidMigration> getList() {
        return commands;
    }

    /**
     * is contained from the tableList
     * @param name tablename
     * @return true if contain
     */
    private boolean isContainedTableName(String name) {
        return tableList.contains(name);
    }

    /**
     * Set db configuration
     * @param conf
     */
    public void setConf(StroidConf conf) {
        this.stroidConf = conf;
    }

    /**
     * get db configuration
     * @return
     */
    public StroidConf getConf() {
        return stroidConf;
    }

}
