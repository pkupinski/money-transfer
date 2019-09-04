package com.revolut.transfers.model;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.Objects;

/**
 * Represents user's account.
 */
public class Account {
    private final Integer id;
    private final String name;

    public Account(Integer id, String name) {
        Objects.requireNonNull(id);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name));

        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            final Account that = (Account) obj;
            return Objects.equals(that.id, this.id)
                    && Objects.equals(that.name, this.name);
        }
        return false;
    }
}
