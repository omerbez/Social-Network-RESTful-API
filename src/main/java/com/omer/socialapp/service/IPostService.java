package com.omer.socialapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.omer.socialapp.model.AbstractPost;
import com.omer.socialapp.model.IPostLinksMethods;
import com.omer.socialapp.model.PostOfGroup;
import com.omer.socialapp.model.PostOfPage;
import com.omer.socialapp.model.RawPost;

public interface IPostService {
	public PostOfPage addPostOfPage(long pageId, RawPost rawPost);
	public PostOfGroup addPostOfGroup(long groupId, RawPost rawPost);
	public AbstractPost getPost(long postId);
	public List<PostOfPage> getUserPostsOfPage(long pageId, long userId);
	public List<PostOfGroup> getUserPostsOfGroup(long groupId, long userId);
	public List<PostOfPage> getAllPostsOfPage(long pageId);
	public List<PostOfGroup> getAllPostsOfGroup(long groupId);
	public Optional<Long> getPostSubjectId(long postId);
	public EntityPostType getPostType(long postId);
	public EntityModel<IPostLinksMethods> toEntityModel(IPostLinksMethods post, EntityPostType type);
	public CollectionModel<EntityModel<IPostLinksMethods>> toCollectionModel(Iterable<? extends IPostLinksMethods> posts, EntityPostType type);
	
	
	public static enum EntityPostType {
		POST_OF_PAGE, POST_OF_GROUP;
	}
}
