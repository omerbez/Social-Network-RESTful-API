package com.omer.socialapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.omer.socialapp.adapter.AbstractPostEntityModelAdapter;
import com.omer.socialapp.adapter.GroupPostEntityModelAdapter;
import com.omer.socialapp.adapter.PagePostEntityModelAdapter;
import com.omer.socialapp.exceptions.GeneralException;
import com.omer.socialapp.exceptions.GroupNotFoundException;
import com.omer.socialapp.exceptions.PageNotFoundException;
import com.omer.socialapp.exceptions.PostNotFoundException;
import com.omer.socialapp.exceptions.UserNotFoundException;
import com.omer.socialapp.model.AbstractPage;
import com.omer.socialapp.model.AbstractPost;
import com.omer.socialapp.model.Group;
import com.omer.socialapp.model.IPostLinksMethods;
import com.omer.socialapp.model.PostOfGroup;
import com.omer.socialapp.model.PostOfPage;
import com.omer.socialapp.model.RawPost;
import com.omer.socialapp.model.User;
import com.omer.socialapp.repository.GroupRepository;
import com.omer.socialapp.repository.PageRepository;
import com.omer.socialapp.repository.PostRepository;
import com.omer.socialapp.repository.UserRepository;

@Service
public class PostService implements IPostService
{
	private PostRepository postRepository;
	private PageRepository pageRepository;
	private UserRepository userRepository;
	private GroupRepository groupRepository;
	private PagePostEntityModelAdapter pagePostEntityAdapter;
	private GroupPostEntityModelAdapter groupPostEntityAdapter;
	private AbstractPostEntityModelAdapter abstractPostEntityAdapter; //for user entire posts
	
	
	@Autowired
	public PostService(PostRepository postRepo, PageRepository pageRepo, UserRepository userRepo, 
			PagePostEntityModelAdapter pageEntityAdapter, GroupRepository groupRepository, GroupPostEntityModelAdapter groupPostEntityAdapter,
			AbstractPostEntityModelAdapter abstractPostEntityAdapter) {
		postRepository = postRepo;
		pageRepository = pageRepo;
		userRepository = userRepo;
		pagePostEntityAdapter = pageEntityAdapter;
		this.groupRepository = groupRepository;
		this.groupPostEntityAdapter = groupPostEntityAdapter;
		this.abstractPostEntityAdapter = abstractPostEntityAdapter;
	}
	
	@Override
	public PostOfPage addPostOfPage(long pageId, RawPost rawPost) {
		Assert.noNullElements(new Object[] {rawPost, rawPost.getText(), rawPost.getPostedUserId()}, "Illegal post param");
		Assert.hasText(rawPost.getText(), "Post's text is illegal");
		
		long userId = rawPost.getPostedUserId();
		AbstractPage page = pageRepository.findById(pageId).orElseThrow(() -> new PageNotFoundException(pageId));
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		
		if(!user.isRegisteredIn(page))
			throw new GeneralException("User "+userId+" is not registered to page "+pageId+" and cannot post");
		
		return postRepository.save(new PostOfPage(rawPost.getText(), user, page));
	}

	@Override
	public EntityModel<IPostLinksMethods> toEntityModel(IPostLinksMethods post, EntityPostType type) {
		Assert.notNull(post, "Post resource must not be null");
		return type == EntityPostType.POST_OF_PAGE ? 
				pagePostEntityAdapter.toModel(post) : groupPostEntityAdapter.toModel(post);
	}

	@Override
	public CollectionModel<EntityModel<IPostLinksMethods>> toCollectionModel(Iterable<? extends IPostLinksMethods> posts, EntityPostType type) {
		Assert.notNull(posts, "Post resources must not be null");
		return type == EntityPostType.POST_OF_PAGE ? pagePostEntityAdapter.toCollectionModel(posts) 
				: type == EntityPostType.POST_OF_GROUP ? groupPostEntityAdapter.toCollectionModel(posts)
						: abstractPostEntityAdapter.toCollectionModel(posts);
	}

	@Override
	public List<PostOfPage> getUserPostsOfPage(long pageId, long userId) {
		if(!userRepository.existsById(userId))
			throw new UserNotFoundException(userId);
		
		if(!pageRepository.existsById(pageId))
			throw new PageNotFoundException(pageId);
		
		return postRepository.getUserPostsOfPage(userId, pageId);
	}

	@Override
	public List<PostOfPage> getAllPostsOfPage(long pageId) {
		if(!pageRepository.existsById(pageId))
			throw new PageNotFoundException(pageId);
		
		return postRepository.getPostsOfPage(pageId);
	}

	@Override
	public AbstractPost getPost(long postId) {
		return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
	}

	@Override
	public PostOfGroup addPostOfGroup(long groupId, RawPost rawPost) {
		Assert.noNullElements(new Object[] {rawPost, rawPost.getText(), rawPost.getPostedUserId()}, "Illegal post param");
		Assert.hasText(rawPost.getText(), "Post's text is illegal");
		
		long userId = rawPost.getPostedUserId();
		Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		
		if(!user.isRegisteredIn(group))
			throw new GeneralException("User "+userId+" is not registered to group "+group.getId()+" and cannot post");
			
		return postRepository.save(new PostOfGroup(rawPost.getText(), user, group));
	}

	@Override
	public List<PostOfGroup> getUserPostsOfGroup(long groupId, long userId) {
		if(!userRepository.existsById(userId))
			throw new UserNotFoundException(userId);
		
		if(!groupRepository.existsById(groupId))
			throw new GroupNotFoundException(groupId);
		
		return postRepository.getUserPostsOfGroup(userId, groupId);
	}

	@Override
	public List<PostOfGroup> getAllPostsOfGroup(long groupId) {
		if(!groupRepository.existsById(groupId))
			throw new GroupNotFoundException(groupId);
		
		return postRepository.getPostsOfGroup(groupId);
	}

	@Override
	public EntityPostType getPostType(long postId) {
		if(!postRepository.existsById(postId))
			throw new PostNotFoundException(postId);
		
		String type = postRepository.getPostType(postId);
		return type.equals(PostOfGroup.DISCRIMINATOR_VALUE) ? EntityPostType.POST_OF_PAGE : EntityPostType.POST_OF_GROUP;
	}

	@Override
	public List<AbstractPost> getUserPosts(long userId) {	
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		List<AbstractPost> result = new ArrayList<>();
		
		for(Group group : user.getGroups()) {
			result.addAll(getUserPostsOfGroup(group.getId(), userId));
		}
		
		for(AbstractPage page : user.getLikedPages()) {
			result.addAll(getUserPostsOfPage(page.getId(), userId));
		}
		
		return result;
	}
}
