package co.ryred.mcrkit.bungee;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropertiesFile {

    private Properties properties;
    private String fileName;

    public PropertiesFile(Properties properties1) {
        this.properties = properties1;
        this.fileName = null;
    }

    public void load() {
        try {
            this.properties.load(new FileInputStream(this.fileName));
        } catch (IOException ioexception) {
            System.err.println("Unnable to load " + this.fileName + ": " + ioexception.toString());
        }

    }

    public void save() {
        try {
            this.properties.store(new FileOutputStream(this.fileName), "Minecraft Remote Toolkit Properties File");
        } catch (IOException ioexception) {
            System.err.println("Unnable to save " + this.fileName + ": " + ioexception.toString());
        }

    }

    public boolean keyExists(String s) {
        return this.properties.containsKey(s);
    }

    public String getString(String s) {
        return this.properties.getProperty(s);
    }

    public String getString(String s, String s1) {
        if (this.properties.containsKey(s)) {
            return this.properties.getProperty(s);
        } else {
            this.setString(s, s1);
            return s1;
        }
    }

    public void setString(String s, String s1) {
        this.properties.setProperty(s, s1);
    }

    public int getInt(String s) {
        return Integer.parseInt(this.properties.getProperty(s));
    }

    public int getInt(String s, int i) {
        if (this.properties.containsKey(s)) {
            return Integer.parseInt(this.properties.getProperty(s));
        } else {
            this.setInt(s, i);
            return i;
        }
    }

    public void setInt(String s, int i) {
        this.properties.setProperty(s, String.valueOf(i));
    }

    public long getLong(String s) {
        return Long.parseLong(this.properties.getProperty(s));
    }

    public long getLong(String s, long l) {
        if (this.properties.containsKey(s)) {
            return Long.parseLong(this.properties.getProperty(s));
        } else {
            this.setLong(s, l);
            return l;
        }
    }

    public void setLong(String s, long l) {
        this.properties.setProperty(s, String.valueOf(l));
    }

    public boolean getBoolean(String s) {
        return Boolean.parseBoolean(this.properties.getProperty(s));
    }

    public boolean getBoolean(String s, boolean flag) {
        if (this.properties.containsKey(s)) {
            return Boolean.parseBoolean(this.properties.getProperty(s));
        } else {
            this.setBoolean(s, flag);
            return flag;
        }
    }

    public void setBoolean(String s, boolean flag) {
        this.properties.setProperty(s, String.valueOf(flag));
    }

    public Properties getProperties() {
        return this.properties;
    }
}
