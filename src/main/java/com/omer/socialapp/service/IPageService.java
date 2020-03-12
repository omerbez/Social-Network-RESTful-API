package com.omer.socialapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.omer.socialapp.model.AbstractPage;
import com.omer.socialapp.model.IPageLinksMethods;
import com.omer.socialapp.model.PlainPage;
import com.omer.socialapp.model.User;


public interface IPageService {
	public Optional<AbstractPage> findById(long id);
	public PlainPage addPlainPage(PlainPage page);
	public List<AbstractPage> getAllPages();
	public List<User> getUsersOfPage(long pageId);
	public void removePage(long pageId);
	public CollectionModel<EntityModel<IPageLinksMethods>> toCollectionModel(Iterable<? extends IPageLinksMethods> pages);
	public EntityModel<IPageLinksMethods> toEntityModel(IPageLinksMethods page);
}
