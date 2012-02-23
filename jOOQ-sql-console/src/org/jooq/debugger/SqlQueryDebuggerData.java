package org.jooq.debugger;

import java.io.Serializable;


/**
 * @author Christopher Deckers
 */
public class SqlQueryDebuggerData implements Serializable {

	private static volatile int nextID;
	
	private int id;
    private SqlQueryType queryType;
    private String[] queries;
    private Long preparationDuration;
    private Long bindingDuration;
    private long executionDuration;

    public SqlQueryDebuggerData(SqlQueryType queryType, String[] queries, Long preparationDuration, Long bindingDuration, long executionDuration) {
    	this.id = nextID++;
        this.queryType = queryType;
        this.queries = queries;
        this.preparationDuration = preparationDuration;
        this.bindingDuration = bindingDuration;
        this.executionDuration = executionDuration;
    }
    
    public int getID() {
		return id;
	}

    public SqlQueryType getQueryType() {
        return queryType;
    }

    public String[] getQueries() {
        return queries;
    }

    public Long getPreparedStatementPreparationDuration() {
		return preparationDuration;
	}
    
    public Long getPreparedStatementBindingDuration() {
		return bindingDuration;
	}
    
    public long getExecutionDuration() {
        return executionDuration;
    }

}
