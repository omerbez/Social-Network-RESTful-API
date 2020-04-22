package com.omer.socialapp.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RawPost
{
	@NotBlank(message = "post text must have at least 1 character")
	@Size(max = 250, message = "Post is to long (max length is 250)")
	private String text;
}
