package org.jooq;

/**
 * The User to be used by GRANT statement
 *
 * @author Timur Shaidullin
 */
public interface User {
    /**
     * The name of user
     */
    public Name getName();
}
