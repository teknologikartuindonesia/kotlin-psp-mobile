package id.co.pspmobile.data.network.responses.customapp

data class CustomAppResponse(
    val _id: String,
    val app_accent_color: String,
    val app_desktop_background_header_url: String,
    val app_display_name: String,
    var app_icon_url: String,
    val app_menu: List<AppMenu>,
    val app_mobile_background_header_url: String,
    val app_secondary_accent_color: String,
    val company_id: String,
    val create_time: String,
    val creator_id: String,
    val delete_by: Any,
    val delete_time: Any,
    val editor_id: String,
    val is_delete: Boolean,
    val update_time: String
)