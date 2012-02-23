package org.jooq.debugger;

import java.io.Serializable;

/**
 * @author Christopher Deckers
 */
public class SqlQueryDebuggerResultSetData implements Serializable {

	private static volatile int nextID;
	
	private int id;
    private long lifeTime;
    private final int readRows;
    private final int readCount;
    private final int writeCount;

    public SqlQueryDebuggerResultSetData(long lifeTime, final int readRows, final int readCount, final int writeCount) {
    	this.id = nextID++;
        this.lifeTime = lifeTime;
        this.readRows = readRows;
        this.readCount = readCount;
        this.writeCount = writeCount;
    }
    
    public int getId() {
		return id;
	}

    public long getLifeTime() {
        return lifeTime;
    }

    public int getReadRows() {
        return readRows;
    }

    public int getReadCount() {
        return readCount;
    }

    public int getWriteCount() {
        return writeCount;
    }

}
