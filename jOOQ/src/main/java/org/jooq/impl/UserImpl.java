package org.jooq.impl;

import org.jooq.User;

/**
 * A common implementation of the User type
 *
 * @author Timur Shaidullin
 */
public class UserImpl implements User {
    private String name;

    public UserImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
