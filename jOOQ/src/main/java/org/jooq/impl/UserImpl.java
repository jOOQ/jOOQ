package org.jooq.impl;

import org.jooq.Name;
import org.jooq.User;

/**
 * A common implementation of the User type
 *
 * @author Timur Shaidullin
 */
public class UserImpl implements User {
    private Name name;

    public UserImpl(Name name) {
        this.name = name;
    }

    @Override
    public Name getName() {
        return name;
    }
}
