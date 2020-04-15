package com.omer.socialapp.model;


/**
 * A common (most abstract) interface of all the concrete classes of User and UserDTO.
 * Any class that should be adapted to EntityModel/CollectionModel must implements this interface
 * in order to supply the reqiered methods for the links.
 * (So the ModelAdapter class will be able to adapt any DTO object and any concrete object!)
 */
public interface IUserLinksMethods {
	public Long getId();
}
