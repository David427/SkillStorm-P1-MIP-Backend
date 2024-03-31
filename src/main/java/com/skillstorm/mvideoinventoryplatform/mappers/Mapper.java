package com.skillstorm.mvideoinventoryplatform.mappers;

public interface Mapper<O, D> {

    // Interface to provide flexibility in swapping the DTO <-> entity mapper library
    D mapTo(O o);

    O mapFrom(D d);

}
