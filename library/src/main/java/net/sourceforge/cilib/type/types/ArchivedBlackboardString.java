/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple {@code Blackboard} implementation.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public final class ArchivedBlackboardString<K, V extends Type> extends Blackboard<K, V> {

    private static final long serialVersionUID = -2589625146223946484L;
    //private final Map<K, V> typeBoard; // no need to store types, because all types are V.
    private final Map<K, String> fileBoard;
    

    /**
     * Create a new empty {@code Blackboard} container.
     */
    public ArchivedBlackboardString() {
        this.fileBoard = new HashMap<K, String>();
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public ArchivedBlackboardString(ArchivedBlackboardString<K, V> copy) {
        this.fileBoard = new ConcurrentHashMap<K, String>();
        for (Map.Entry<K, String> entry : copy.fileBoard.entrySet()) {
            K key = entry.getKey();
            @SuppressWarnings({"unchecked"})
            V value = copy.get(key);
            //System.out.println("Copying stuff!");
            //File value = (File) entry.getValue();
            this.put(key, value);
        }
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ArchivedBlackboardString<K, V> getClone() {
           //System.out.println("Copying stuff?");
        
        return new ArchivedBlackboardString<K, V>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (this.getClass() != obj.getClass())) {
            return false;
        }

        ArchivedBlackboardString<?, ?> other = (ArchivedBlackboardString<?, ?>) obj;
        return this.fileBoard.equals(other.fileBoard);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.fileBoard == null ? 0 : this.fileBoard.hashCode());
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.fileBoard.toString();
    }

    /**
     * Put the provided key / value pair into a temporary file, and put the temporary file into the {@code ArchivedBlackboard}.
     * @param key The key value for the pair.
     * @param value The value associated with the key.
     * @return The provided value.
     */
    @Override
    public V put(K key, V value) {
        try {
            File file;
            if(fileBoard.containsKey(key)) {
                file = new File(fileBoard.get(key));
            } else {
                file = File.createTempFile("blackboard", ".tmp"/*, new File("/mnt/nfs/anna/tmp/")*/);
                //System.out.println(file.getAbsolutePath());
                file.deleteOnExit();
            }
            // Write to the temp file now! Serialise the value!
            ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            objectOut.writeObject(value);
            objectOut.close();
            this.fileBoard.put(key, file.getPath()); //
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return value; //this.board.put(key, value);
    }

    /**
     * Get the value associated with the provided key, {@code null} otherwise. Read from file!
     * @param key The key to obtained the value of.
     * @return The associated value to the key.
     */
    @Override
    public V get(K key) {     
        try {   
            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(this.fileBoard.get(key)));
            V value = (V) (objectIn.readObject());
            objectIn.close();
            return value;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ArchivedBlackboardString.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }  
        return null;
    }
}
