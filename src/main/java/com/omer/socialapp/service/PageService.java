package com.omer.socialapp.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.omer.socialapp.adapter.PageEntityModelAdapter;
import com.omer.socialapp.exceptions.PageNotFoundException;
import com.omer.socialapp.model.AbstractPage;
import com.omer.socialapp.model.IPageLinksMethods;
import com.omer.socialapp.model.PlainPage;
import com.omer.socialapp.model.User;
import com.omer.socialapp.repository.PageRepository;
import com.omer.socialapp.repository.UserRepository;


@Service
public class PageService implements IPageService
{
	private PageRepository pageRepository;
	private PageEntityModelAdapter pageEntityAdapter;
	private UserRepository userRepository;
	
	
	
	@Autowired
	public PageService(PageRepository pageRepo, PageEntityModelAdapter modelAdapter, UserRepository userRepo) {
		pageRepository = pageRepo;
		pageEntityAdapter = modelAdapter;
		userRepository = userRepo;
	}
	
	@Override
	public Optional<AbstractPage> findById(long id) {
		return pageRepository.findById(id);
	}

	@Override
	public List<AbstractPage> getAllPages() {
		return pageRepository.findAll();
	}

	@Override
	public CollectionModel<EntityModel<IPageLinksMethods>> toCollectionModel(
			Iterable<? extends IPageLinksMethods> pages) {
		return pageEntityAdapter.toCollectionModel(pages);
	}

	@Override
	public EntityModel<IPageLinksMethods> toEntityModel(IPageLinksMethods page) {
		return pageEntityAdapter.toModel(page);
	}

	@Override
	public PlainPage addPlainPage(PlainPage page) {
		Assert.noNullElements(new Object[] {page, page.getName(), page.getDescription()}, 
				"Page name and description are madatory!");
		
		return pageRepository.save(page);
	}
	
	/**
	 * return all the users which liked this page
	 */
	@Override
	public List<User> getUsersOfPage(long pageId) {
		AbstractPage page = pageRepository.findById(pageId).orElseThrow(()-> new PageNotFoundException(pageId));
		return userRepository.findByLikedPages(page);
	}

	@Override
	@Transactional //For "all or none" DB writes + the returned entity will be in managed state.
	public void removePage(long pageId) {
		var page = findById(pageId).orElseThrow(() -> new PageNotFoundException(pageId));
		Set<User> likedUsers = page.getLikedUsers();
		if(likedUsers != null) {
			for(User user : likedUsers) {
				user.unlikePage(page);
			}
		}
		pageRepository.delete(page);
	}
}
