package com.github.willyric17.githubsearcher.external.response

import com.google.gson.annotations.SerializedName

data class UserSearchResponse(
	@field:SerializedName("total_count")
	val totalCount: Int,
	@field:SerializedName("incomplete_results")
	val incomplete: Boolean,
	@field:SerializedName("items")
	val users: List<User>
)