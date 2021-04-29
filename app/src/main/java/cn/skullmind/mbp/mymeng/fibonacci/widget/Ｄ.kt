package cn.skullmind.mbp.mymeng.fibonacci.widget
import com.google.gson.annotations.SerializedName


data class Ｄ(
    @SerializedName("authorizations_url")
    val authorizationsUrl: String,
    @SerializedName("code_search_url")
    val codeSearchUrl: String,
    @SerializedName("commit_search_url")
    val commitSearchUrl: String,
    @SerializedName("current_user_authorizations_html_url")
    val currentUserAuthorizationsHtmlUrl: String,
    @SerializedName("current_user_repositories_url")
    val currentUserRepositoriesUrl: String,
    @SerializedName("current_user_url")
    val currentUserUrl: String,
    @SerializedName("emails_url")
    val emailsUrl: String,
    @SerializedName("emojis_url")
    val emojisUrl: String,
    @SerializedName("events_url")
    val eventsUrl: String,
    @SerializedName("feeds_url")
    val feedsUrl: String,
    @SerializedName("followers_url")
    val followersUrl: String,
    @SerializedName("following_url")
    val followingUrl: String,
    @SerializedName("gists_url")
    val gistsUrl: String,
    @SerializedName("hub_url")
    val hubUrl: String,
    @SerializedName("issue_search_url")
    val issueSearchUrl: String,
    @SerializedName("issues_url")
    val issuesUrl: String,
    @SerializedName("keys_url")
    val keysUrl: String,
    @SerializedName("label_search_url")
    val labelSearchUrl: String,
    @SerializedName("notifications_url")
    val notificationsUrl: String,
    @SerializedName("organization_repositories_url")
    val organizationRepositoriesUrl: String,
    @SerializedName("organization_teams_url")
    val organizationTeamsUrl: String,
    @SerializedName("organization_url")
    val organizationUrl: String,
    @SerializedName("public_gists_url")
    val publicGistsUrl: String,
    @SerializedName("rate_limit_url")
    val rateLimitUrl: String,
    @SerializedName("repository_search_url")
    val repositorySearchUrl: String,
    @SerializedName("repository_url")
    val repositoryUrl: String,
    @SerializedName("starred_gists_url")
    val starredGistsUrl: String,
    @SerializedName("starred_url")
    val starredUrl: String,
    @SerializedName("user_organizations_url")
    val userOrganizationsUrl: String,
    @SerializedName("user_repositories_url")
    val userRepositoriesUrl: String,
    @SerializedName("user_search_url")
    val userSearchUrl: String,
    @SerializedName("user_url")
    val userUrl: String
)